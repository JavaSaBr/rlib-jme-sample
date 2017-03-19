package com.ss.server.database.sql.provider.mysql;

import com.ss.server.database.DbTables.AccountTable;
import com.ss.server.database.sql.provider.SqlProvider;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation with queries for MySQL.
 *
 * @author JavaSaBr
 */
public class MySQLSqlProvider implements SqlProvider {

    @NotNull
    static final String INSERT_ACCOUNT = "INSERT INTO `" + AccountTable.TABLE_NAME +
            "` (" + AccountTable.NAME + ", " + AccountTable.PASSWORD + ") VALUES (?, ?)";

    @NotNull
    static final String SELECT_ACCOUNT = "SELECT `" + AccountTable.ID + "`, `" + AccountTable.NAME + "`, `" +
            AccountTable.PASSWORD + "` FROM `" + AccountTable.TABLE_NAME + "` WHERE `" +
            AccountTable.NAME + "` = ? LIMIT 1";

    @NotNull
    @Override
    public String insertAccountQuery() {
        return INSERT_ACCOUNT;
    }

    @NotNull
    @Override
    public String selectAccountByNameQuery() {
        return SELECT_ACCOUNT;
    }
}
