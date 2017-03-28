package com.ss.client.model.player;

import com.ss.client.model.vehicle.impl.AbstractVehicle;
import com.ss.client.template.PlayerVehicleTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of a player's vehicle.
 *
 * @author JavaSaBr
 */
public class PlayerVehicle extends AbstractVehicle<PlayerVehicleTemplate> {

    public static final int CLASS_ID = 2;

    /**
     * The player.
     */
    @Nullable
    private volatile Player player;

    /**
     * @param template the template.
     */
    public PlayerVehicle(@NotNull final PlayerVehicleTemplate template) {
        super(template);
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }
}
