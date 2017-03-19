package com.ss.server.network;

import org.jetbrains.annotations.NotNull;
import rlib.network.packet.Packet;

/**
 * The interface to implement a packet factory.
 *
 * @author JavaSaBr
 */
public interface PacketFactory<C, T extends Packet, K> {

    /**
     * Create a new packet by the type.
     *
     * @param type the type of a packet.
     * @return the new packet.
     */
    @NotNull
    <R extends T> R create(@NotNull K type);
}
