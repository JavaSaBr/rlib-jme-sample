package com.ss.server.database;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import com.ss.server.database.sql.provider.SqlProvider;
import com.ss.server.manager.DataBaseManager;
import com.ss.server.model.player.Player;
import com.ss.server.model.player.PlayerVehicle;
import com.ss.server.template.PlayerVehicleTemplate;
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
 * The manager to work with player vehicles in DB.
 *
 * @author JavaSaBr
 */
public class PlayerVehicleDBManager implements DbTables {

    @NotNull
    protected static final Logger LOGGER = LoggerManager.getLogger(PlayerVehicleDBManager.class);

    private static PlayerVehicleDBManager instance;

    @NotNull
    public static PlayerVehicleDBManager getInstance() {

        if (instance == null) {
            instance = new PlayerVehicleDBManager();
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

    private PlayerVehicleDBManager() {
        final DataBaseManager dbManager = DataBaseManager.getInstance();
        this.connectFactory = dbManager.getConnectFactory();
        this.sqlProvider = dbManager.getSqlProvider();
    }

    /**
     * Create a new player vehicle.
     *
     * @param player   the player.
     * @param template the template.
     * @return the player vehicle or null.
     */
    @Nullable
    public PlayerVehicle create(@NotNull final Player player, @NotNull final PlayerVehicleTemplate template) {

        PreparedStatement statement = null;
        Connection con = null;
        ResultSet rset = null;

        try {

            con = connectFactory.getConnection();

            statement = con.prepareStatement(sqlProvider.insertPlayerVehicleQuery(), RETURN_GENERATED_KEYS);
            statement.setInt(1, player.getObjectId());
            statement.setInt(2, template.getTemplateId());
            statement.execute();

            rset = statement.getGeneratedKeys();

            if (!rset.next()) {
                LOGGER.warning("Can't insert new player vehicle template for the " + player);
                return null;
            }

            final int id = rset.getInt(1);

            final PlayerVehicle vehicle = template.takeInstance(PlayerVehicle.class, id);

            return vehicle;

        } catch (final SQLException e) {
            LOGGER.warning(e);
        } finally {
            DBUtils.close(con, statement, rset);
        }

        return null;
    }
}
