package org.sample.server.network;

import org.sample.server.LocalObjects;
import org.sample.server.network.model.GameClient;
import rlib.network.packet.impl.AbstractTaskReadablePacket;
import rlib.util.pools.FoldablePool;

/**
 * Базовая реализация клиентского пакета.
 *
 * @author Ronn
 */
public abstract class ClientPacket extends AbstractTaskReadablePacket<GameClient, LocalObjects> {

    /**
     * Пул для хранения этого пакета после использования.
     */
    private FoldablePool<ClientPacket> pool;

    @Override
    protected void executeImpl(final LocalObjects local, final long currentTime) {
    }

    @Override
    protected final FoldablePool<ClientPacket> getPool() {
        return pool;
    }

    /**
     * @param pool пул для хранения этого пакета.
     */
    public final void setPool(final FoldablePool<ClientPacket> pool) {
        this.pool = pool;
    }

    @Override
    protected void readImpl() {
    }
}
