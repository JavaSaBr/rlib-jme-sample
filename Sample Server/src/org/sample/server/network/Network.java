package org.sample.server.network;

import org.sample.server.Config;
import org.sample.server.network.model.GameAcceptHandler;
import org.sample.server.network.model.GameNetworkConfig;

import java.net.InetSocketAddress;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;
import rlib.network.NetworkFactory;
import rlib.network.server.ServerNetwork;

/**
 * Сеть сервера.
 *
 * @author Ronn
 */
public final class Network {

    private static final Logger LOGGER = LoggerManager.getLogger(Network.class);

    /**
     * Экземпляр сети.
     */
    private static Network instance;

    public static Network getInstance() {

        if (instance == null) {
            instance = new Network();
        }

        return instance;
    }

    /**
     * Серверная сеть.
     */
    private final ServerNetwork serverNetwork;


    public Network() {
        InitializeManager.valid(getClass());

        try {

            ServerPacketType.init();
            ClientPacketType.init();

            serverNetwork = NetworkFactory.newDefaultAsynchronousServerNetwork(GameNetworkConfig.getInstance(), GameAcceptHandler.getInstance());
            serverNetwork.bind(new InetSocketAddress(Config.SERVER_PORT));

            LOGGER.info("initialized.");

        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @return сеть игрового сервера.
     */
    public ServerNetwork getServerNetwork() {
        return serverNetwork;
    }
}
