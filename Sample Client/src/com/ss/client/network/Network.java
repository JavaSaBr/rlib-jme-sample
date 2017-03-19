package com.ss.client.network;

import com.ss.client.config.Config;
import com.ss.client.manager.ClassManager;
import com.ss.client.network.model.GameConnectHandler;
import com.ss.client.network.model.GameServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;
import rlib.network.NetworkFactory;
import rlib.network.client.ClientNetwork;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

import java.net.InetSocketAddress;

/**
 * The client network.
 *
 * @author JavaSaBr
 */
public final class Network {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(Network.class);

    private static Network instance;

    @NotNull
    public static Network getInstance() {

        if (instance == null) {
            instance = new Network();
        }

        return instance;
    }

    /**
     * The game network.
     */
    private final ClientNetwork gameNetwork;

    /**
     * The connected game server.
     */
    private GameServer gameServer;

    /**
     * The flag of connection to the server.
     */
    private boolean gameConnected;

    private Network() {
        InitializeManager.valid(getClass());

        final Array<Class<SendablePacket>> sendablePackets = ArrayFactory.newArray(Class.class);
        final Array<Class<ReadablePacket>> readablePackets = ArrayFactory.newArray(Class.class);

        final ClassManager classManager = ClassManager.getInstance();
        classManager.findImplements(sendablePackets, SendablePacket.class);
        classManager.findImplements(readablePackets, ReadablePacket.class);

        gameNetwork = NetworkFactory.newDefaultAsynchronousClientNetwork(Config.NETWORK_CONFIG, GameConnectHandler.getInstance());

        connectToServer();

        LOGGER.info("initialized " + sendablePackets.size() + " server packets and " + readablePackets.size() +
                " client packets.");
    }

    /**
     * Try to connect to the server.
     */
    private void connectToServer() {

        final InetSocketAddress targetAddress = Config.SERVER_SOCKET_ADDRESS;

        LOGGER.info(this, "connect to server " + targetAddress);

        gameNetwork.connect(targetAddress);
    }

    /**
     * @return the game network.
     */
    @NotNull
    public ClientNetwork getGameNetwork() {
        return gameNetwork;
    }

    /**
     * @return the game server.
     */
    @Nullable
    private GameServer getGameServer() {
        return gameServer;
    }

    /**
     * @param server the game server.
     */
    public void setGameServer(@Nullable final GameServer server) {
        this.gameServer = server;

        if (server == null) {
            LOGGER.info("close game server");
        }
    }

    /**
     * @return true if the network is connected to the server
     */
    public synchronized final boolean isGameConnected() {
        return gameConnected;
    }

    /**
     * @param connected the flag of connection to the server.
     */
    public synchronized final void setGameConnected(final boolean connected) {
        this.gameConnected = connected;

        if (!connected && gameServer != null) {
            gameServer.close();
        }
    }

    /**
     * Send the client packet to the server.
     *
     * @param packet the client packet.
     */
    public void sendPacketToGameServer(@NotNull final ClientPacket packet) {

        final GameServer server = getGameServer();

        if (server == null) {
            throw new RuntimeException("not found Game Server.");
        }

        server.sendPacket(packet);
    }
}
