package com.ss.server.network;

import static java.util.Objects.requireNonNull;
import com.ss.server.network.packet.client.AuthCredentialsClientPacket;
import org.jetbrains.annotations.NotNull;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

import java.util.HashSet;
import java.util.Set;

/**
 * The list of available TYPES of client packets.
 *
 * @author JavaSaBr
 */
public enum ClientPacketType {
    REQUEST_AUTH(1, new AuthCredentialsClientPacket()),;

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(ClientPacketType.class);

    /**
     * The array of packet type id to packet type.
     */
    @NotNull
    private final static ClientPacketType[] TYPES;

    static {

        final Set<Integer> set = new HashSet<>();

        for (final ClientPacketType packet : values()) {

            final int index = packet.getId();

            if (set.contains(index)) {
                LOGGER.warning("found duplicate id " + index + " or " + Integer.toHexString(packet.getId()) + "!");
            }

            set.add(index);
        }

        set.clear();

        TYPES = new ClientPacketType[Short.MAX_VALUE * 2];

        for (final ClientPacketType type : values()) {
            TYPES[type.getId()] = type;
        }

        LOGGER.info("client packets prepared.");
    }

    /**
     * Get the type of packet by its packet type id.
     *
     * @param id the packet id.
     * @return the type of the packet.
     */
    @NotNull
    public static ClientPacketType getPacketType(final int id) {
        return requireNonNull(TYPES[id]);
    }

    /**
     * The client packet example.
     */
    @NotNull
    private final ClientPacket packet;

    /**
     * The id of packet type.
     */
    private final int id;

    ClientPacketType(final int id, @NotNull final ClientPacket packet) {
        this.id = id;
        this.packet = packet;
    }

    /**
     * @return the id of packet type.
     */
    public int getId() {
        return id;
    }

    /**
     * @return the class of packet implementation.
     */
    @NotNull
    public Class<? extends ClientPacket> getPacketClass() {
        return packet.getClass();
    }
}
