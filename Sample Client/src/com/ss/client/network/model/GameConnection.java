package com.ss.client.network.model;

import org.jetbrains.annotations.NotNull;
import rlib.network.client.ClientNetwork;
import rlib.network.client.server.impl.AbstractServerConnection;
import rlib.network.packet.SendablePacket;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * The implementation of connection from a client to a server.
 *
 * @author JavaSaBr
 */
class GameConnection extends AbstractServerConnection {

    GameConnection(@NotNull final ClientNetwork network, @NotNull final AsynchronousSocketChannel channel,
                   @NotNull final Class<? extends SendablePacket> sendableType) {
        super(network, channel, sendableType);
    }
}
