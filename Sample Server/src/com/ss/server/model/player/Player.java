package com.ss.server.model.player;

import com.ss.server.LocalObjects;
import com.ss.server.model.impl.AbstractSpawnableObject;
import com.ss.server.network.ServerPacket;
import com.ss.server.network.model.GameClient;
import com.ss.server.template.EmptyPlayerTemplate;
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

    private static final int CLASS_ID = 1;

    /**
     * The list of available vehicles.
     */
    @NotNull
    private final Array<PlayerVehicle> availableVehicles;

    /**
     * The client of the player.
     */
    @Nullable
    private volatile GameClient client;

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

    @Override
    public int getClassId() {
        return CLASS_ID;
    }

    @Override
    public void sendPacket(@NotNull final ServerPacket packet, @NotNull final LocalObjects local) {

        final GameClient client = getClient();

        if (client == null) {
            LOGGER.warning(this, "not found a client to send a packet.");
            return;
        }

        client.sendPacket(packet);
    }

    @Override
    public void broadcastPacket(@NotNull final ServerPacket packet, @NotNull final LocalObjects local) {
        WORLD.broadcastPacket(this, packet, local);
    }

    /**
     * Set a new client to the player.
     *
     * @param client the client.
     */
    public void setClient(@Nullable final GameClient client) {
        this.client = client;
    }

    @Nullable
    private GameClient getClient() {
        return client;
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
