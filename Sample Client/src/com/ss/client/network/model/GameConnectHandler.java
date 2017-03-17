package com.ss.client.network.model;

import com.ss.client.network.ClientPacket;
import com.ss.client.network.Network;
import com.ss.client.network.ClientPacket;
import com.ss.client.network.Network;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.client.ConnectHandler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Обработчик подключения к серверу.
 *
 * @author Ronn
 */
public class GameConnectHandler implements ConnectHandler {

    private static final Logger LOGGER = LoggerManager.getLogger(GameConnectHandler.class);

    private static GameConnectHandler instance;

    public static GameConnectHandler getInstance() {

        if (instance == null) {
            instance = new GameConnectHandler();
        }

        return instance;
    }

    @Override
    public void onConnect(final AsynchronousSocketChannel channel) {

        try {
            channel.setOption(StandardSocketOptions.SO_RCVBUF, 12000);
            channel.setOption(StandardSocketOptions.SO_SNDBUF, 24000);
        } catch (final IOException e) {
            LOGGER.warning(e.getMessage(), e);
        }

        final Network network = Network.getInstance();
        final NetConnection connection = new NetConnection(network.getGameNetwork(), channel, ClientPacket.class);
        final NetServer server = new NetServer(connection, ServerType.GAME_SERVER);

        network.setGameServer(server);

        connection.setServer(server);
        connection.startRead();
    }

    @Override
    public void onFailed(final Throwable exc) {

        if (exc instanceof ConnectException) {
            LOGGER.info(this, "невозможно подключиться.");
        } else {
            LOGGER.warning(this, new Exception(exc));
        }
    }
}
