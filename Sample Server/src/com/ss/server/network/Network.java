package com.ss.server.network;

import static java.util.Objects.requireNonNull;
import static rlib.network.NetworkFactory.newDefaultAsynchronousServerNetwork;
import com.ss.server.Config;
import com.ss.server.manager.ClassManager;
import com.ss.server.network.model.GameAcceptHandler;
import com.ss.server.network.model.GameNetworkConfig;
import org.jetbrains.annotations.NotNull;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;
import rlib.network.server.ServerNetwork;
import rlib.util.ClassUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

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

            final Array<Class<SendablePacket>> sendablePackets = ArrayFactory.newArray(Class.class);
            final Array<Class<ReadablePacket>> readablePackets = ArrayFactory.newArray(Class.class);

            final ClassManager classManager = ClassManager.getInstance();
            classManager.findImplements(sendablePackets, SendablePacket.class);
            classManager.findImplements(readablePackets, ReadablePacket.class);

            sendablePackets.forEach(ClassUtils::newInstance);
            readablePackets.forEach(ClassUtils::newInstance);

            serverNetwork = requireNonNull(newDefaultAsynchronousServerNetwork(GameNetworkConfig.getInstance(),
                    GameAcceptHandler.getInstance()));

            serverNetwork.bind(new InetSocketAddress(Config.SERVER_PORT));

            LOGGER.info("initialized " + sendablePackets.size() + " server packets and " +
                    readablePackets.size() + " client packets.");

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
