package org.sample.client.network;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.PoolFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Перечисление типов серверных пакетов.
 *
 * @author Ronn
 */
public enum ClientPacketType {
    ;

    private static final Logger LOGGER = LoggerManager.getLogger(ClientPacketType.class);

    /**
     * массив всех типов пакетов
     */
    public static final ClientPacketType[] VALUES = values();

    /**
     * кол-во всех типов пакетов
     */
    public static final int SIZE = VALUES.length;

    /**
     * Инициализация клиентских пакетов.
     */
    public static final void init() {

        final Set<Integer> set = new HashSet<Integer>();

        for (final ClientPacketType element : VALUES) {

            if (set.contains(element.opcode)) {
                LOGGER.warning("found duplicate opcode " + Integer.toHexString(element.opcode).toUpperCase());
            }

            set.add(element.opcode);
        }

        LOGGER.info("client packets prepared.");
    }

    /**
     * пул пакетов
     */
    private final FoldablePool<ClientPacket> pool;

    /**
     * опкод пакета
     */
    private int opcode;

    /**
     * @param opcode опкод пакета.
     */
    private ClientPacketType(final int opcode) {
        this.opcode = opcode;
        this.pool = PoolFactory.newAtomicFoldablePool(ClientPacket.class);
    }

    /**
     * @return опкод пакета.
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * @param opcode опкод пакета.
     */
    public void setOpcode(final int opcode) {
        this.opcode = opcode;
    }

    /**
     * @return пул соотвествующего пакета.
     */
    public FoldablePool<ClientPacket> getPool() {
        return pool;
    }
}
