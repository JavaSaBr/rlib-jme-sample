package org.sample.server.network;

import org.sample.server.network.packet.client.RequestAuth;

import java.util.HashSet;
import java.util.Set;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * Перечисление всех клиентских пакетов.
 *
 * @author Ronn
 */
public enum ClientPacketType {
    REQUEST_AUTH(1, new RequestAuth()),
    ;

    private static final Logger LOGGER = LoggerManager.getLogger(ClientPacketType.class);

    /**
     * Массив типов пакетов.
     */
    private static ClientPacketType[] types;

    /**
     * Возвращает тип пакета указанного опкода.
     *
     * @param opcode опкод пакета.
     * @return тип клиентского пакета.
     */
    public static ClientPacketType getPacketType(final int opcode) {
        return types[opcode];
    }

    /**
     * Инициализация клиентских пакетов.
     */
    public static void init() {

        final Set<Integer> set = new HashSet<>();

        for (final ClientPacketType packet : values()) {

            final int index = packet.getOpcode();

            if (set.contains(index)) {
                LOGGER.warning("found duplicate opcode " + index + " or " + Integer.toHexString(packet.getOpcode()) + "!");
            }

            set.add(index);
        }

        set.clear();

        types = new ClientPacketType[Short.MAX_VALUE * 2];

        for (final ClientPacketType type : values()) {
            types[type.getOpcode()] = type;
        }

        LOGGER.info("client packets prepared.");
    }

    /**
     * Экземпляр пакета.
     */
    private final ClientPacket packet;

    /**
     * Опкод пакета.
     */
    private final int opcode;

    private ClientPacketType(final int opcode, final ClientPacket packet) {
        this.opcode = opcode;
        this.packet = packet;
    }

    /**
     * @return опкод пакета.
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * @return класс реализации пакета этого типа.
     */
    public Class<? extends ClientPacket> getPacketClass() {
        return packet.getClass();
    }
}
