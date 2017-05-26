package com.ss.client.model.player;

import com.ss.client.model.impl.AbstractSpawnableObject;
import com.ss.client.template.EmptyPlayerTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * The implementation of a player.
 *
 * @author JavaSaBr
 */
public class Player extends AbstractSpawnableObject<EmptyPlayerTemplate> {

    /**
     * The list of available vehicles.
     */
    @NotNull
    private final Array<PlayerVehicle> availableVehicles;

    /**
     * The current vehicle.
     */
    @Nullable
    private volatile PlayerVehicle currentVehicle;

    /**
     * @param template the template.
     */
    public Player(@NotNull final EmptyPlayerTemplate template) {
        super(template);
        this.availableVehicles = ArrayFactory.newArray(PlayerVehicle.class);
    }

    /**
     * @return the current vehicle.
     */
    @Nullable
    public PlayerVehicle getCurrentVehicle() {
        return currentVehicle;
    }

    /**
     * @param currentVehicle the current vehicle.
     */
    public void setCurrentVehicle(@Nullable final PlayerVehicle currentVehicle) {
        this.currentVehicle = currentVehicle;
    }

    @NotNull
    public Array<PlayerVehicle> getAvailableVehicles() {
        return availableVehicles;
    }

    /**
     * Add the new available vehicle.
     *
     * @param vehicle the vehicle.
     */
    public void addAvailableVehicle(@NotNull final PlayerVehicle vehicle) {
        availableVehicles.add(vehicle);
    }

    /**
     * Remove the old available vehicle.
     *
     * @param vehicle the vehicle.
     */
    public void removeAvailableVehicle(@NotNull final PlayerVehicle vehicle) {
        availableVehicles.fastRemove(vehicle);
    }
}
