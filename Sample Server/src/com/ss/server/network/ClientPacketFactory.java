package com.ss.server.network;

import static rlib.util.ClassUtils.unsafeCast;
import com.ss.server.network.model.GameClient;
import org.jetbrains.annotations.NotNull;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.ArrayUtils;
import rlib.util.ClassUtils;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

import java.util.function.BiFunction;

/**
 * The implementation of factory to create client packets.
 *
 * @author JavaSaBr
 */
public class ClientPacketFactory implements PacketFactory<GameClient, ClientPacket, ClientPacketType> {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(ClientPacketFactory.class);

    @NotNull
    private static final BiFunction<ClientPacketType, ReusablePool<ClientPacket>, ClientPacket> CREATE_PACKET_FUNCTION = (packetType, pool) -> {

        final ClientPacket newPacket = ClassUtils.newInstance(packetType.getPacketClass());
        newPacket.setPool(pool);

        if (LOGGER.isEnabledDebug()) {
            LOGGER.debug("create client packet: " + newPacket.getName());
        }

        return newPacket;
    };

    /**
     * The list of all packet pools.
     */
    @NotNull
    private final ReusablePool<ClientPacket>[] pools;

    public ClientPacketFactory() {

        final ClientPacketType[] types = ClientPacketType.values();

        pools = ArrayUtils.create(ReusablePool.class, types.length);

        for (int i = 0, length = types.length; i < length; i++) {
            pools[i] = PoolFactory.newConcurrentAtomicARSWLockReusablePool(ClientPacket.class);
        }
    }

    @NotNull
    @Override
    public <R extends ClientPacket> R create(@NotNull final ClientPacketType type) {
        final ReusablePool<ClientPacket> pool = pools[type.ordinal()];
        return unsafeCast(pool.take(type, pool, CREATE_PACKET_FUNCTION));
    }
}
