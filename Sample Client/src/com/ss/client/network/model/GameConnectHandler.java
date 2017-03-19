package com.ss.client.network.model;

import com.ss.client.network.ClientPacket;
import com.ss.client.network.Network;
import org.jetbrains.annotations.NotNull;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.client.ConnectHandler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * The connection handler.
 *
 * @author JavaSaBr
 */
public class GameConnectHandler implements ConnectHandler {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(GameConnectHandler.class);

    private static GameConnectHandler instance;

    @NotNull
    public static GameConnectHandler getInstance() {

        if (instance == null) {
            instance = new GameConnectHandler();
        }

        return instance;
    }

    @Override
    public void onConnect(@NotNull final AsynchronousSocketChannel channel) {

        try {
            channel.setOption(StandardSocketOptions.SO_RCVBUF, 12000);
            channel.setOption(StandardSocketOptions.SO_SNDBUF, 24000);
        } catch (final IOException e) {
            LOGGER.warning(e.getMessage(), e);
        }

        final Network network = Network.getInstance();
        final GameConnection connection = new GameConnection(network.getGameNetwork(), channel, ClientPacket.class);
        final GameServer server = new GameServer(connection);

        network.setGameServer(server);

        connection.setOwner(server);
        connection.startRead();
    }

    @Override
    public void onFailed(@NotNull final Throwable exc) {

        if (exc instanceof ConnectException) {
            LOGGER.info(this, "невозможно подключиться.");
        } else {
            LOGGER.warning(this, new Exception(exc));
        }
    }
}
