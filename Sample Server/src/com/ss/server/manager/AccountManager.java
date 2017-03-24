package com.ss.server.manager;

import static java.util.Objects.requireNonNull;
import com.ss.server.Config;
import com.ss.server.LocalObjects;
import com.ss.server.ServerThread;
import com.ss.server.database.AccountDBManager;
import com.ss.server.model.Account;
import com.ss.server.model.player.Player;
import com.ss.server.network.model.GameClient;
import com.ss.server.network.packet.server.AuthResultServerPacket.ResultType;
import org.jetbrains.annotations.NotNull;
import rlib.concurrent.util.ThreadUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;
import rlib.util.StringUtils;
import rlib.util.dictionary.ConcurrentObjectDictionary;
import rlib.util.dictionary.DictionaryFactory;

/**
 * The manager to work with accounts.
 *
 * @author JavaSaBr
 */
public final class AccountManager implements Runnable {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(AccountManager.class);

    private static AccountManager instance;

    @NotNull
    public static AccountManager getInstance() {

        if (instance == null) {
            instance = new AccountManager();
        }

        return instance;
    }

    /**
     * The table of all authed users.
     */
    @NotNull
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
     * @return the table of all authed users.
     */
    @NotNull
    private ConcurrentObjectDictionary<String, Account> getAccounts() {
        return accounts;
    }

    /**
     * Try to auth the user.
     *
     * @param client   the current game client.
     * @param name     the account name.
     * @param password the password.
     */
    @NotNull
    public ResultType auth(@NotNull final GameClient client, @NotNull final String name, @NotNull final String password) {

        final LocalObjects local = LocalObjects.get();
        final String lowerName = name.toLowerCase();
        final AccountDBManager dbManager = AccountDBManager.getInstance();

        final ConcurrentObjectDictionary<String, Account> accounts = getAccounts();
        final long stamp = accounts.writeLock();
        try {

            Account account = accounts.get(lowerName);

            if (account == null) {
                account = dbManager.findByName(name);
            }

            if (account == null) {
                LOGGER.warning("incorrect login " + name);

                if (!Config.ACCOUNT_AUTO_REGISTER || name.length() < 4) {
                    return ResultType.INCORRECT_NAME;
                }

                LOGGER.warning("try auto create account for login " + name);

                // try to create a new account
                account = dbManager.create(name, password);

                // if account was created try to create a player for this
                final PlayerManager playerManager = PlayerManager.getInstance();
                final Player player = account == null ? null : playerManager.createNewPlayer(account, local);
                if (player != null) player.deleteMe(local);
            }

            if (account == null) {
                return ResultType.INCORRECT_NAME;
            } else if (!StringUtils.equals(password, account.getPassword())) {
                return ResultType.INCORRECT_PASSWORD;
            }

            final GameClient prev = account.getClient();

            // if we have an other user which authed with the same account, we should disconnect him.
            if (prev != null && prev != client) {

                prev.setAccount(null);
                prev.close();

                client.setAccount(account);
                account.setClient(client);

            } else if (prev == null) {

                client.setAccount(account);
                account.setClient(client);

                accounts.put(requireNonNull(account.getLowerName()), account);
            }

            LOGGER.info("auth user \"" + account.getName() + "\"");

            return ResultType.SUCCESSFUL;

        } finally {
            accounts.writeUnlock(stamp);
        }
    }

    @Override
    public void run() {
        while (true) {
            ThreadUtils.sleep(60000);
            //TODO here should be the logic of cleaning accounts.
        }
    }

    /**
     * Remove the account from authed.
     *
     * @param account the account.
     */
    public void removeAccount(@NotNull final Account account) {
        final ConcurrentObjectDictionary<String, Account> accounts = getAccounts();
        final long stamp = accounts.writeLock();
        try {
            final Account removed = accounts.remove(account.getLowerName());
            if (removed != null) removed.release();
        } finally {
            accounts.writeUnlock(stamp);
        }
    }
}
