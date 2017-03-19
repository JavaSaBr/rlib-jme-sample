package com.ss.server.network.model;

import org.jetbrains.annotations.NotNull;
import rlib.network.packet.SendablePacket;
import rlib.network.server.ServerNetwork;
import rlib.network.server.client.impl.AbstractClientConnection;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * The implementation of connection between server and client.
 *
 * @author JavaSaBr
 */
class GameConnection extends AbstractClientConnection {
    GameConnection(@NotNull final ServerNetwork network, @NotNull final AsynchronousSocketChannel channel,
                   @NotNull final Class<? extends SendablePacket> sendableType) {
        super(network, channel, sendableType);
    }
}
