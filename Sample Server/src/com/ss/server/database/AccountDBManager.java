package com.ss.server.database;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import com.ss.server.database.sql.provider.SqlProvider;
import com.ss.server.manager.DataBaseManager;
import com.ss.server.model.Account;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.database.ConnectFactory;
import rlib.database.DBUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The manager to work with account in DB.
 *
 * @author JavaSaBr
 */
public class AccountDBManager implements DbTables {

    @NotNull
    protected static final Logger LOGGER = LoggerManager.getLogger(AccountDBManager.class);

    private static AccountDBManager instance;

    @NotNull
    public static AccountDBManager getInstance() {

        if (instance == null) {
            instance = new AccountDBManager();
        }

        return instance;
    }

    /**
     * The connect factory.
     */
    @NotNull
    private final ConnectFactory connectFactory;

    /**
     * The SQL provider.
     */
    @NotNull
    private final SqlProvider sqlProvider;

    private AccountDBManager() {
        final DataBaseManager dbManager = DataBaseManager.getInstance();
        this.connectFactory = dbManager.getConnectFactory();
        this.sqlProvider = dbManager.getSqlProvider();
    }

    /**
     * Create a new account.
     *
     * @param name     the account name.
     * @param password the account password.
     * @return the new account or null.
     */
    @Nullable
    public Account createAccount(@NotNull final String name, @NotNull final String password) {

        PreparedStatement statement = null;
        Connection con = null;
        ResultSet rset = null;

        try {

            con = connectFactory.getConnection();

            statement = con.prepareStatement(sqlProvider.insertAccountQuery(), RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, password);
            statement.execute();

            rset = statement.getGeneratedKeys();
            rset.next();

            final long id = rset.getLong(1);

            final Account account = Account.newInstance();
            account.setId(id);
            account.setName(name);
            account.setLowerName(name.toLowerCase());
            account.setPassword(password);

            return account;

        } catch (final SQLException e) {
            LOGGER.warning(e);
        } finally {
            DBUtils.close(con, statement, rset);
        }

        return null;
    }

    /**
     * Try to find an account by the account name.
     *
     * @param name the account name.
     * @return the found account or null.
     */
    @Nullable
    public Account findAccount(@NotNull final String name) {

        PreparedStatement statement = null;
        Connection con = null;
        ResultSet rset = null;

        try {

            con = connectFactory.getConnection();

            statement = con.prepareStatement(sqlProvider.selectAccountByNameQuery());
            statement.setString(1, name);

            rset = statement.executeQuery();
            if (!rset.next()) return null;

            final long id = rset.getLong(1);
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
            DBUtils.close(con, statement, rset);
        }

        return null;
    }
}
