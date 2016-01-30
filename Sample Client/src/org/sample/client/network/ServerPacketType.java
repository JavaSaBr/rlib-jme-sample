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
public enum ServerPacketType {
    ;

    private static final Logger LOGGER = LoggerManager.getLogger(ServerPacketType.class);

    /**
     * Массив всех типов.
     */
    public static final ServerPacketType[] VALUES = values();

    /**
     * Кол-во всех типов.
     */
    public static final int SIZE = VALUES.length;

    /**
     * Массив пакетов.
     */
    private static ServerPacket[] packets;

    /**
     * Возвращает новый экземпляр пакета в соответствии с опкодом
     *
     * @param opcode опкод пакета.
     * @return экземпляр нужного пакета.
     */
    public static ServerPacket getPacketForOpcode(final int opcode) {
        final ServerPacket packet = packets[opcode];
        return packet == null ? null : packet.newInstance();
    }

    /**
     * Инициализация клиентских пакетов.
     */
    public static void init() {

        final Set<Integer> set = new HashSet<Integer>();

        for (final ServerPacketType packet : values()) {

            final int index = packet.getOpcode();

            if (set.contains(index)) {
                LOGGER.warning("found duplicate opcode " + index + " or " + Integer.toHexString(packet.getOpcode()) + "!");
            }

            set.add(index);
        }

        set.clear();

        packets = new ServerPacket[Short.MAX_VALUE * 2];

        for (final ServerPacketType packet : values()) {
            packets[packet.getOpcode()] = packet.getPacket();
        }

        LOGGER.info("server packets prepared.");
    }

    /**
     * Пул пакетов.
     */
    private final FoldablePool<ServerPacket> pool;

    /**
     * Экземпляр пакета.
     */
    private final ServerPacket packet;

    /**
     * Опкод пакета.
     */
    private final int opcode;

    /**
     * @param opcode опкод пакета.
     * @param packet экземпляр пакета.
     */
    private ServerPacketType(final int opcode, final ServerPacket packet) {
        this.opcode = opcode;
        this.packet = packet;
        this.packet.setPacketType(this);
        this.pool = PoolFactory.newAtomicFoldablePool(ServerPacket.class);
    }

    /**
     * @return опкод пакета.
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * @return экземпляр пакета.
     */
    public ServerPacket getPacket() {
        return packet;
    }

    /**
     * @return пул пакетов.
     */
    public final FoldablePool<ServerPacket> getPool() {
        return pool;
    }
}