package com.ss.server.network;

import com.ss.server.network.model.GameClient;
import org.jetbrains.annotations.NotNull;
import rlib.network.packet.impl.AbstractReusableSendablePacket;
import rlib.util.ArrayUtils;
import rlib.util.ClassUtils;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

import java.util.function.BiFunction;

/**
 * The implementation of server packet factory.
 *
 * @author JavaSaBr
 */
public class ServerPacketFactory implements PacketFactory<GameClient, ServerPacket, ServerPacket> {

    @NotNull
    private static final BiFunction<ReusablePool<AbstractReusableSendablePacket<GameClient>>, Class<? extends ServerPacket>,
            AbstractReusableSendablePacket<GameClient>> CREATE_PACKET_FUNCTION = (pool, type) -> {
        final ServerPacket result = ClassUtils.newInstance(type);
        result.setPool(pool);
        return result;
    };

    /**
     * The pool table.
     */
    @NotNull
    private final ReusablePool<AbstractReusableSendablePacket<GameClient>>[] pools;

    public ServerPacketFactory() {

        final ServerPacketType[] types = ServerPacketType.values();

        pools = ArrayUtils.create(ReusablePool.class, types.length);

        for (int i = 0, length = types.length; i < length; i++) {
            pools[i] = PoolFactory.newConcurrentAtomicARSWLockReusablePool(ServerPacket.class);
        }
    }

    @NotNull
    @Override
    public <R extends ServerPacket> R create(@NotNull final ServerPacket example) {
        final ServerPacketType type = example.getPacketType();
        final ReusablePool<AbstractReusableSendablePacket<GameClient>> pool = pools[type.ordinal()];
        return ClassUtils.unsafeCast(pool.take(pool, example.getClass(), CREATE_PACKET_FUNCTION));
    }
}
