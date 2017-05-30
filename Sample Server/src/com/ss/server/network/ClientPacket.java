package com.ss.server.network;

import com.ss.server.LocalObjects;
import com.ss.server.model.player.Player;
import com.ss.server.network.model.GameClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.network.packet.impl.AbstractTaskReadablePacket;

import java.nio.ByteBuffer;

/**
 * The base implementation of readable packet.
 *
 * @author JavaSaBr
 */
public abstract class ClientPacket extends AbstractTaskReadablePacket<LocalObjects> {

    @Override
    protected void executeImpl(@NotNull final LocalObjects local, final long currentTime) {
    }

    @Override
    protected void readImpl(@NotNull final ByteBuffer buffer) {
        super.readImpl(buffer);
    }

    @Nullable
    @Override
    public GameClient getOwner() {
        return (GameClient) super.getOwner();
    }

    @Nullable
    protected Player getPlayer() {

        final GameClient owner = getOwner();

        if (owner == null) {
            LOGGER.warning("not found a client for the packet " + this);
            return null;
        }

        return owner.getOwner();
    }
}
