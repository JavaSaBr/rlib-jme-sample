package com.ss.server.network;

import com.ss.server.LocalObjects;
import com.ss.server.network.model.GameClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.network.packet.impl.AbstractTaskReadablePacket;

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
    protected void readImpl() {
    }

    @Nullable
    @Override
    public GameClient getOwner() {
        return (GameClient) super.getOwner();
    }
}
