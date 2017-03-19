package com.ss.server.network;

import com.ss.server.LocalObjects;
import com.ss.server.network.model.GameClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.network.packet.impl.AbstractTaskReadablePacket;
import rlib.util.pools.ReusablePool;

/**
 * The base implementation of readable packet.
 *
 * @author JavaSaBr
 */
public abstract class ClientPacket extends AbstractTaskReadablePacket<GameClient, LocalObjects> {

    /**
     * The pool to store an used packet to reuse later.
     */
    @Nullable
    private ReusablePool<ClientPacket> pool;

    @Override
    protected void executeImpl(@NotNull final LocalObjects local, final long currentTime) {
    }

    @Override
    @Nullable
    protected final ReusablePool<ClientPacket> getPool() {
        return pool;
    }

    /**
     * @param pool the pool to store an used packet to reuse later.
     */
    final void setPool(@Nullable final ReusablePool<ClientPacket> pool) {
        this.pool = pool;
    }

    @Override
    protected void readImpl() {
    }
}
