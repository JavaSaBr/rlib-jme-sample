package com.ss.server.network.model;

import com.ss.server.network.EmptyCrypt;
import com.ss.server.network.Network;
import com.ss.server.network.ServerPacket;
import org.jetbrains.annotations.NotNull;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.server.AcceptHandler;
import rlib.network.server.ServerNetwork;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * The handler to accept new connections.
 *
 * @author JavaSaBr
 */
public class GameAcceptHandler extends AcceptHandler {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(GameAcceptHandler.class);

    @NotNull
    private static final GameAcceptHandler INSTANCE = new GameAcceptHandler();

    @NotNull
    public static GameAcceptHandler getInstance() {
        return INSTANCE;
    }

    @Override
    protected void onAccept(@NotNull final AsynchronousSocketChannel channel) {

        try {
            channel.setOption(StandardSocketOptions.SO_SNDBUF, 12000);
            channel.setOption(StandardSocketOptions.SO_RCVBUF, 24000);
        } catch (final IOException e) {
            LOGGER.warning(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        final Network network = Network.getInstance();
        final ServerNetwork serverNetwork = network.getServerNetwork();

        final GameConnection connect = new GameConnection(serverNetwork, channel, ServerPacket.class);
        final GameClient client = new GameClient(connect, EmptyCrypt.getInstance());

        connect.setClient(client);
        client.successfulConnection();
        connect.startRead();
    }

    @Override
    protected void onFailed(@NotNull final Throwable exc) {
        LOGGER.warning(this, exc);
    }
}
