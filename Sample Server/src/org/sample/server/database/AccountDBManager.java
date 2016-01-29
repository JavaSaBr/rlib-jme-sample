package org.sample.server.database;

import org.sample.server.Config;
import org.sample.server.model.impl.Account;
import rlib.database.ConnectFactories;
import rlib.database.ConnectFactory;
import rlib.database.DBUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * Менеджер для работы с БД.
 *
 * @author Ronn
 */
public class AccountDBManager implements TableNames {

    protected static final Logger LOGGER = LoggerManager.getLogger(AccountDBManager.class);

    private static final String INSERT_ACCOUNT = "INSERT INTO `" + AccountTable.TABLE_NAME + "` (" + AccountTable.NAME + ", " + AccountTable.PASSWORD + ") VALUES (?, ?)";
    private static final String SELECT_ACCOUNT = "SELECT `" + AccountTable.ID + "`, `" + AccountTable.NAME + "`, `" + AccountTable.PASSWORD + "` FROM `" + AccountTable.TABLE_NAME + "` WHERE `" + AccountTable.NAME + "` = ? LIMIT 1";

    private static AccountDBManager instance;

    public static AccountDBManager getInstance() {

        if (instance == null) {
            instance = new AccountDBManager();
        }

        return instance;
    }

    /**
     * Фабрика подключений к БД.
     */
    private final ConnectFactory connectFactory;

    private AccountDBManager() {
        this.connectFactory = ConnectFactories.newBoneCPConnectFactory(Config.DATA_BASE_CONFIG, Config.DATA_BASE_DRIVER);
    }

    /**
     * Создание в БД нового аккаунта.
     *
     * @param name имя аккаунта.
     * @param password пароль от аккаунта.
     * @return созданный аккаунт либо <code>null</code> если создать не
     * получилось.
     */
    public Account createAccount(final String name, final String password) {

        PreparedStatement statement = null;
        Connection con = null;
        ResultSet rset = null;

        try {

            con = connectFactory.getConnection();

            statement = con.prepareStatement(INSERT_ACCOUNT, RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, password);
            statement.execute();

            rset = statement.getGeneratedKeys();
            rset.next();

            final int id = rset.getInt(1);

            final Account account = Account.newInstance();
            account.setName(name);
            account.setLowerName(name.toLowerCase());
            account.setPassword(password);

            return account;

        } catch (final SQLException e) {
            LOGGER.warning(e);
        } finally {
            DBUtils.closeDatabaseCSR(con, statement, rset);
        }

        return null;
    }

    /**
     * Загрузка из БД аккаунта по его имени.
     *
     * @param name имя аккаунта.
     * @return загруженный аккаунт либо <code>null</code> если такого не будет.
     */
    public Account restoreAccount(final String name) {

        PreparedStatement statement = null;
        Connection con = null;
        ResultSet rset = null;

        try {

            con = connectFactory.getConnection();

            statement = con.prepareStatement(SELECT_ACCOUNT);
            statement.setString(1, name);

            rset = statement.executeQuery();

            if (!rset.next()) {
                return null;
            }

            final int id = rset.getInt(1);

            final String accountName = rset.getString(2);
            final String password = rset.getString(3);

            final Account account = Account.newInstance();
            account.setPassword(password);
            account.setId(id);
            account.setLowerName(accountName.toLowerCase());
            account.setName(accountName);

            return account;

        } catch (final SQLException e) {
            LOGGER.warning(e);
        } finally {
            DBUtils.closeDatabaseCS(con, statement);
        }

        return null;
    }
}
