package com.ss.client.network;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

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
    public static void init() {

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
    private final ReusablePool<ClientPacket> pool;

    /**
     * опкод пакета
     */
    private int opcode;

    /**
     * @param opcode опкод пакета.
     */
    private ClientPacketType(final int opcode) {
        this.opcode = opcode;
        this.pool = PoolFactory.newConcurrentAtomicARSWLockReusablePool(ClientPacket.class);
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
    public ReusablePool<ClientPacket> getPool() {
        return pool;
    }
}
