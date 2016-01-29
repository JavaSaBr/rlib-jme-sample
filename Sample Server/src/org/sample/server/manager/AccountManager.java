package org.sample.server.manager;

import org.sample.server.Config;
import org.sample.server.LocalObjects;
import org.sample.server.ServerThread;
import org.sample.server.database.AccountDBManager;
import org.sample.server.model.impl.Account;
import org.sample.server.network.model.GameClient;
import org.sample.server.network.packet.server.ResponseAuthResult;

import rlib.concurrent.util.ThreadUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;
import rlib.util.SafeTask;
import rlib.util.StringUtils;
import rlib.util.dictionary.ConcurrentObjectDictionary;
import rlib.util.dictionary.DictionaryFactory;

/**
 * Менеджер для работы с аккаунтами пользователей.
 *
 * @author Ronn
 */
public final class AccountManager implements SafeTask {

    private static final Logger LOGGER = LoggerManager.getLogger(AccountManager.class);

    private static AccountManager instance;

    public static AccountManager getInstance() {

        if (instance == null) {
            instance = new AccountManager();
        }

        return instance;
    }

    /**
     * Таблица авторизованных аккаунтов.
     */
    private final ConcurrentObjectDictionary<String, Account> accounts;

    private AccountManager() {
        InitializeManager.valid(getClass());

        this.accounts = DictionaryFactory.newConcurrentAtomicObjectDictionary();

        final ServerThread thread = new ServerThread(this);
        thread.setName(getClass().getSimpleName() + "-checker");
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    /**
     * @return таблица авторизованных аккаунтов.
     */
    private ConcurrentObjectDictionary<String, Account> getAccounts() {
        return accounts;
    }

    public void auth(final GameClient client, final String name, final String password, final LocalObjects local) {

        final String lowerName = name.toLowerCase();
        final AccountDBManager dbManager = AccountDBManager.getInstance();

        final ConcurrentObjectDictionary<String, Account> accounts = getAccounts();
        accounts.writeLock();
        try {

            Account account = accounts.get(lowerName);

            if (account == null) {
                account = dbManager.restoreAccount(name);
            }

            if (account == null) {
                LOGGER.warning("incorrect login " + name);

                if (!Config.ACCOUNT_AUTO_REGISTER ||name.length() < 4) {
                    client.sendPacket(ResponseAuthResult.getInstance(ResponseAuthResult.ResultType.INCORRECT_NAME, local), true);
                    return;
                }

                LOGGER.warning("try auto create account for login " + name);

                account = dbManager.createAccount(name, password);

                if (account == null) {
                    client.sendPacket(ResponseAuthResult.getInstance(ResponseAuthResult.ResultType.INCORRECT_NAME, local), true);
                    return;
                }
            }

            if (account == null) {
                client.sendPacket(ResponseAuthResult.getInstance(ResponseAuthResult.ResultType.INCORRECT_NAME, local), true);
                return;
            }

            if (!StringUtils.equals(password, account.getPassword())) {
                client.sendPacket(ResponseAuthResult.getInstance(ResponseAuthResult.ResultType.INCORRECT_PASSWORD, local), true);
                return;
            }

            final GameClient prev = account.getClient();

            if (prev != null && prev != client) {

                prev.setAccount(null);
                prev.close();

                client.setAccount(account);
                account.setClient(client);

            } else if(prev == null){

                client.setAccount(account);
                account.setClient(client);

                accounts.put(account.getLowerName(), account);
            }

            client.sendPacket(ResponseAuthResult.getInstance(ResponseAuthResult.ResultType.SUCCESSFUL, local), true);

            LOGGER.info("auth user \"" + account.getName() + "\"");

        } finally {
            accounts.writeUnlock();
        }
    }

    @Override
    public void runImpl() {
        while (true) {
            ThreadUtils.sleep(60000);
            //TODO здесь должна быть логика очистки аккаунтов
        }
    }

    /**
     * Удаление аккаунта из активных.
     *
     * @param account удаляемый аккаунт.
     */
    public void removeAccount(final Account account) {
        final ConcurrentObjectDictionary<String, Account> accounts = getAccounts();
        accounts.writeLock();
        try {

            final Account removed = accounts.remove(account.getLowerName());

            if(removed != null) {
                removed.release();
            }

        } finally {
            accounts.writeUnlock();
        }
    }
}
