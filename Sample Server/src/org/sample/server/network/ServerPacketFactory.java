package org.sample.server.network;

import org.sample.server.network.model.GameClient;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.ClassUtils;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.PoolFactory;

/**
 * Реализация фабрики серверных пакетов.
 *
 * @author Ronn
 */
public class ServerPacketFactory implements PacketFactory<GameClient, ServerPacket, ServerPacket> {

    private static final Logger LOGGER = LoggerManager.getLogger(ServerPacketFactory.class);

    /**
     * Таблица пулов пакетов.
     */
    private final FoldablePool<ServerPacket>[] pools;

    @SuppressWarnings("unchecked")
    public ServerPacketFactory() {

        final ServerPacketType[] types = ServerPacketType.values();

        pools = new FoldablePool[types.length];

        for (int i = 0, length = types.length; i < length; i++) {
            pools[i] = PoolFactory.newAtomicFoldablePool(ServerPacket.class);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends ServerPacket> R create(final ServerPacket example) {

        final ServerPacketType type = example.getPacketType();
        final FoldablePool<ServerPacket> pool = pools[type.ordinal()];

        ServerPacket packet = pool.take();

        if (packet == null) {
            packet = ClassUtils.newInstance(example.getClass());
            packet.setPool(pool);
        }

        return (R) packet;
    }
}
