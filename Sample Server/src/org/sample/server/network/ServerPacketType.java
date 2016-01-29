package org.sample.server.network;

import java.util.HashSet;
import java.util.Set;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.PoolFactory;

/**
 * Перечисление типов отправляемых гейм сервером пакетов на логин сервер.
 *
 * @author Ronn
 */
public enum ServerPacketType {
    RESPONSE_CONNECTED(1),
    RESPONSE_AUTH_RESULT(2),
   ;

    private static final Logger LOGGER = LoggerManager.getLogger(ServerPacketType.class);

    /**
     * Массив всех типов пакетов.
     */
    public static final ServerPacketType[] VALUES = values();

    /**
     * Кол-во всех типов пакетов.
     */
    public static final int SIZE = VALUES.length;

    /**
     * Инициализация серверных пакетов.
     */
    public static final void init() {

        final Set<Integer> set = new HashSet<>();

        for (final ServerPacketType element : VALUES) {

            if (set.contains(element.opcode)) {
                LOGGER.warning("found duplicate opcode " + Integer.toHexString(element.opcode).toUpperCase());
            }

            set.add(element.opcode);
        }

        LOGGER.info("server packets prepared.");
    }

    /**
     * Пул пакетов.
     */
    private final FoldablePool<ServerPacket> pool;

    /**
     * Опкод пакета.
     */
    private final int opcode;

    /**
     * @param opcode опкод пакета.
     */
    private ServerPacketType(final int opcode) {
        this.opcode = opcode;
        this.pool = PoolFactory.newAtomicFoldablePool(ServerPacket.class);
    }

    /**
     * @return опкод пакета.
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * @return пул пакетов.
     */
    public final FoldablePool<ServerPacket> getPool() {
        return pool;
    }
}
