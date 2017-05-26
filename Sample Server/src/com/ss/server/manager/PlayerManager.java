package com.ss.server.manager;

import com.ss.server.LocalObjects;
import com.ss.server.database.PlayerDBManager;
import com.ss.server.database.PlayerVehicleDBManager;
import com.ss.server.model.Account;
import com.ss.server.model.player.Player;
import com.ss.server.model.player.PlayerVehicle;
import com.ss.server.template.PlayerVehicleTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;

/**
 * The manager to manage players.
 *
 * @author JavaSaBr
 */
public class PlayerManager {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(PlayerManager.class);

    private static final int START_TEMPLATE_ID = 1;

    private static PlayerManager instance;

    @NotNull
    public static PlayerManager getInstance() {

        if (instance == null) {
            instance = new PlayerManager();
        }

        return instance;
    }

    private PlayerManager() {
        InitializeManager.valid(getClass());
    }

    /**
     * Create a new player.
     *
     * @param account the account.
     * @param local   the local objects.
     * @return the new player or null.
     */
    @Nullable
    public Player createNewPlayer(@NotNull final Account account, @NotNull final LocalObjects local) {

        final PlayerDBManager playerDBManager = PlayerDBManager.getInstance();
        final PlayerVehicleDBManager vehicleDBManager = PlayerVehicleDBManager.getInstance();

        final VehicleTemplateManager templateManager = VehicleTemplateManager.getInstance();
        final PlayerVehicleTemplate vehicleTemplate = templateManager.getPlayerTemplate(START_TEMPLATE_ID);
        if (vehicleTemplate == null) return null;

        final Player player = playerDBManager.create(account);
        if (player == null) return null;

        final PlayerVehicle vehicle = vehicleDBManager.create(player, vehicleTemplate);

        if (vehicle == null) {
            playerDBManager.delete(player.getObjectId());
            player.deleteMe(local);
            return null;
        }

        player.addAvailableVehicle(vehicle);
        player.setCurrentVehicle(vehicle);

        playerDBManager.updateCurrentVehicle(player);

        return player;
    }
}
