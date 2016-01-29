package org.sample.server.network.model;

import org.sample.server.network.Network;
import org.sample.server.network.ServerPacket;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.server.AcceptHandler;
import rlib.network.server.ServerNetwork;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Реализация обработчика новых подключений от клиентов.
 *
 * @author Ronn
 */
public class GameAcceptHandler extends AcceptHandler {

    private static final Logger LOGGER = LoggerManager.getLogger(GameAcceptHandler.class);
    private static GameAcceptHandler instance;

    public static final GameAcceptHandler getInstance() {

        if (instance == null) {
            instance = new GameAcceptHandler();
        }

        return instance;
    }

    @Override
    protected void onAccept(final AsynchronousSocketChannel channel) {

        try {
            channel.setOption(StandardSocketOptions.SO_SNDBUF, 12000);
            channel.setOption(StandardSocketOptions.SO_RCVBUF, 24000);
        } catch (final IOException e) {
            LOGGER.warning(e.getMessage(), e);
        }

        final Network network = Network.getInstance();
        final ServerNetwork serverNetwork = network.getServerNetwork();

        final GameConnection connect = new GameConnection(serverNetwork, channel, ServerPacket.class);
        final GameClient client = GameClient.create(connect);

        connect.setClient(client);
        client.successfulConnection();
        connect.startRead();
    }

    @Override
    protected void onFailed(final Throwable exc) {
        LOGGER.warning(this, exc);
    }
}
