package com.ss.server.model.player;

import com.ss.server.model.vehicle.impl.AbstractVehicle;
import com.ss.server.template.PlayerVehicleTemplate;
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

    @Override
    public int getClassId() {
        return CLASS_ID;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }
}
