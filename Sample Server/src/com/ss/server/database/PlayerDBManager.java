package com.ss.server.database;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import com.ss.server.database.sql.provider.SqlProvider;
import com.ss.server.manager.DataBaseManager;
import com.ss.server.model.Account;
import com.ss.server.model.player.Player;
import com.ss.server.model.player.PlayerVehicle;
import com.ss.server.template.EmptyPlayerTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.database.ConnectFactory;
import rlib.database.DBUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

import java.sql.*;

/**
 * The manager to work with player vehicles in DB.
 *
 * @author JavaSaBr
 */
public class PlayerDBManager implements DbTables {

    @NotNull
    protected static final Logger LOGGER = LoggerManager.getLogger(PlayerDBManager.class);

    private static PlayerDBManager instance;

    @NotNull
    public static PlayerDBManager getInstance() {

        if (instance == null) {
            instance = new PlayerDBManager();
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

    private PlayerDBManager() {
        final DataBaseManager dbManager = DataBaseManager.getInstance();
        this.connectFactory = dbManager.getConnectFactory();
        this.sqlProvider = dbManager.getSqlProvider();
    }

    /**
     * Create a new player.
     *
     * @param account the account.
     * @return the new player or null.
     */
    @Nullable
    public Player create(@NotNull final Account account) {

        PreparedStatement statement = null;
        Connection con = null;
        ResultSet rset = null;

        try {

            con = connectFactory.getConnection();

            statement = con.prepareStatement(sqlProvider.insertPlayerQuery(), RETURN_GENERATED_KEYS);
            statement.setInt(1, account.getId());
            statement.execute();

            rset = statement.getGeneratedKeys();

            if (!rset.next()) {
                LOGGER.warning("Can't insert a new player for the " + account.getName());
                return null;
            }

            final int id = rset.getInt(1);

            final EmptyPlayerTemplate template = EmptyPlayerTemplate.getInstance();
            return template.takeInstance(Player.class, id);

        } catch (final SQLException e) {
            LOGGER.warning(e);
        } finally {
            DBUtils.close(con, statement, rset);
        }

        return null;
    }

    /**
     * Update a current vehicle of the player in the DB.
     *
     * @param player the player.
     */
    public boolean updateCurrentVehicle(@NotNull final Player player) {

        PreparedStatement statement = null;
        Connection con = null;

        try {

            con = connectFactory.getConnection();

            final PlayerVehicle currentVehicle = player.getCurrentVehicle();

            statement = con.prepareStatement(sqlProvider.updatePlayerCurrentVehicleQuery());

            if (currentVehicle == null) {
                statement.setNull(1, JDBCType.INTEGER.ordinal());
            } else {
                statement.setInt(1, currentVehicle.getObjectId());
            }

            statement.setInt(2, player.getObjectId());

            final int result = statement.executeUpdate();

            return result > 0;

        } catch (final SQLException e) {
            LOGGER.warning(e);
        } finally {
            DBUtils.close(con, statement);
        }

        return false;
    }

    /**
     * Delete a player with the object id.
     *
     * @param objectId the object id of a player.
     * @return true if the player was deleted from a DB.
     */
    public boolean delete(final int objectId) {

        PreparedStatement statement = null;
        Connection con = null;

        try {

            con = connectFactory.getConnection();

            statement = con.prepareStatement(sqlProvider.deletePlayerQuery());
            statement.setInt(1, objectId);

            final int result = statement.executeUpdate();

            return result > 0;

        } catch (final SQLException e) {
            LOGGER.warning(e);
        } finally {
            DBUtils.close(con, statement);
        }

        return false;
    }
}
