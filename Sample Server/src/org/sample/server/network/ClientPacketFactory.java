package org.sample.server.network;

import org.sample.server.network.model.GameClient;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.ClassUtils;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.PoolFactory;

/**
 * Реализация фабрики клиентских пакетов для игровых клиентов.
 *
 * @author Ronn
 */
public class ClientPacketFactory implements PacketFactory<GameClient, ClientPacket, ClientPacketType> {

    private static final Logger LOGGER = LoggerManager.getLogger(ClientPacketFactory.class);

    /**
     * Таблица пулов пакетов.
     */
    private final FoldablePool<ClientPacket>[] pools;

    @SuppressWarnings("unchecked")
    public ClientPacketFactory() {

        final ClientPacketType[] types = ClientPacketType.values();

        pools = new FoldablePool[types.length];

        for (int i = 0, length = types.length; i < length; i++) {
            pools[i] = PoolFactory.newAtomicFoldablePool(ClientPacket.class);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends ClientPacket> R create(final ClientPacketType type) {

        final FoldablePool<ClientPacket> pool = pools[type.ordinal()];

        ClientPacket packet = pool.take();

        if (packet == null) {

            packet = ClassUtils.newInstance(type.getPacketClass());
            packet.setPool(pool);

            if (LOGGER.isEnabledDebug()) {
                LOGGER.debug("create client packet: " + packet.getName());
            }
        }

        return (R) packet;
    }
}
