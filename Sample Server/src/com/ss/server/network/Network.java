package com.ss.server.network;

import static java.util.Objects.requireNonNull;
import static rlib.network.NetworkFactory.newDefaultAsynchronousServerNetwork;
import com.ss.server.Config;
import com.ss.server.network.model.GameAcceptHandler;
import com.ss.server.network.model.GameNetworkConfig;
import org.jetbrains.annotations.NotNull;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;
import rlib.network.server.ServerNetwork;

import java.net.InetSocketAddress;

/**
 * The server network.
 *
 * @author JavaSaBr
 */
public final class Network {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(Network.class);

    /**
     * Thw network instance.
     */
    private static Network instance;

    @NotNull
    public static Network getInstance() {

        if (instance == null) {
            instance = new Network();
        }

        return instance;
    }

    /**
     * The implementation of server network.
     */
    @NotNull
    private final ServerNetwork serverNetwork;

    public Network() {
        InitializeManager.valid(getClass());

        try {

            serverNetwork = requireNonNull(newDefaultAsynchronousServerNetwork(GameNetworkConfig.getInstance(),
                    GameAcceptHandler.getInstance()));

            serverNetwork.bind(new InetSocketAddress(Config.SERVER_PORT));

            LOGGER.info("initialized.");

        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @return the implementation of server network.
     */
    @NotNull
    public ServerNetwork getServerNetwork() {
        return serverNetwork;
    }
}
