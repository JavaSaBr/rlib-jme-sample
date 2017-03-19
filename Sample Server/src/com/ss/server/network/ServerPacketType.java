package com.ss.server.network;

import com.ss.server.network.model.GameClient;
import org.jetbrains.annotations.NotNull;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.packet.impl.AbstractReusableSendablePacket;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

import java.util.HashSet;
import java.util.Set;

/**
 * The list of available types of server packets.
 *
 * @author JavaSaBr
 */
public enum ServerPacketType {
    CONNECTED_NOTIFIER(1),
    AUTH_RESULT(2),;

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(ServerPacketType.class);

    /**
     * The list of all types.
     */
    @NotNull
    public static final ServerPacketType[] TYPES = values();

    static {

        final Set<Integer> set = new HashSet<>();

        for (final ServerPacketType element : TYPES) {

            if (set.contains(element.id)) {
                LOGGER.warning("found duplicate id " + Integer.toHexString(element.id).toUpperCase());
            }

            set.add(element.id);
        }

        LOGGER.info("server packets prepared.");
    }

    /**
     * The pool of packets to reuse them.
     */
    @NotNull
    private final ReusablePool<AbstractReusableSendablePacket<GameClient>> pool;

    /**
     * The packet type id.
     */
    private final int id;

    /**
     * @param id the packet type id.
     */
    ServerPacketType(final int id) {
        this.id = id;
        this.pool = PoolFactory.newConcurrentAtomicARSWLockReusablePool(ServerPacket.class);
    }

    /**
     * @return опкод пакета.
     */
    public int getId() {
        return id;
    }

    /**
     * @return the pool of packets to reuse them.
     */
    @NotNull
    public final ReusablePool<AbstractReusableSendablePacket<GameClient>> getPool() {
        return pool;
    }
}
