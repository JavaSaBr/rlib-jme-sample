package com.ss.server.database.sql.provider.mysql;

import com.ss.server.database.DbTables.AccountTable;
import com.ss.server.database.DbTables.PlayerTable;
import com.ss.server.database.DbTables.PlayerVehicleTable;
import com.ss.server.database.sql.provider.SqlProvider;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation with queries for MySQL.
 *
 * @author JavaSaBr
 */
public class MySQLSqlProvider implements SqlProvider {

    @NotNull
    static final String INSERT_ACCOUNT =
            "INSERT INTO `" + AccountTable.TABLE_NAME + "` (`" + AccountTable.NAME + "`, `" + AccountTable.PASSWORD +
                    "`) VALUES (?, ?)";

    @NotNull
    static final String SELECT_ACCOUNT =
            "SELECT `" + AccountTable.ID + "`, `" + AccountTable.NAME + "`, `" + AccountTable.PASSWORD + "` FROM `" +
                    AccountTable.TABLE_NAME + "` WHERE `" + AccountTable.NAME + "` = ? LIMIT 1";

    @NotNull
    static final String INSERT_PLAYER =
            "INSERT INTO `" + PlayerTable.TABLE_NAME + "` (`" + PlayerTable.ACCOUNT_ID + "`) VALUES (?)";

    @NotNull
    static final String UPDATE_PLAYER_CURRENT_VEHICLE =
            "UPDATE `" + PlayerTable.TABLE_NAME + "` SET `" + PlayerTable.CURRENT_VEHICLE_ID + "` = ? WHERE `" +
                    PlayerTable.ID + "` = ? LIMIT 1";

    @NotNull
    static final String DELETE_PLAYER =
            "DELETE FROM `" + PlayerTable.TABLE_NAME + "` WHERE `" + PlayerTable.ID + "` = ? LIMIT 1";

    @NotNull
    static final String INSERT_PLAYER_VEHICLE =
            "INSERT INTO `" + PlayerVehicleTable.TABLE_NAME + "` (`" + PlayerVehicleTable.PLAYER_ID + "`, `" +
                    PlayerVehicleTable.TEMPLATE_ID + "`) VALUES (?,?)";

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

    @NotNull
    @Override
    public String insertPlayerQuery() {
        return INSERT_PLAYER;
    }

    @NotNull
    @Override
    public String updatePlayerCurrentVehicleQuery() {
        return UPDATE_PLAYER_CURRENT_VEHICLE;
    }

    @NotNull
    @Override
    public String deletePlayerQuery() {
        return DELETE_PLAYER;
    }

    @NotNull
    @Override
    public String insertPlayerVehicleQuery() {
        return INSERT_PLAYER_VEHICLE;
    }
}
