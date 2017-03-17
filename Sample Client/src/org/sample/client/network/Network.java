package org.sample.client.network;

import org.sample.client.config.Config;
import org.sample.client.network.model.GameConnectHandler;
import org.sample.client.network.model.NetServer;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;
import rlib.network.NetworkFactory;
import rlib.network.client.ClientNetwork;

import java.net.InetSocketAddress;

/**
 * Сеть клиента.
 *
 * @author Ronn
 */
public final class Network {

    private static final Logger LOGGER = LoggerManager.getLogger(Network.class);

    private static Network instance;

    public static Network getInstance() {

        if (instance == null) {
            instance = new Network();
        }

        return instance;
    }

    /**
     * Селектор для подключения к гейм сервере.
     */
    private final ClientNetwork gameNetwork;

    /**
     * Игровой сервер.
     */
    private NetServer gameServer;

    /**
     * Флаг завершенного подключения к гейм серверу.
     */
    private boolean gameConnected;

    private Network() {
        InitializeManager.valid(getClass());

        ServerPacketType.init();
        ClientPacketType.init();

        gameNetwork = NetworkFactory.newDefaultAsynchronousClientNetwork(Config.NETWORK_CONFIG, GameConnectHandler.getInstance());

        connectToServer();

        LOGGER.info("initialized.");
    }

    /**
     * Запусть процесс подключения к игровому серверу.
     */
    public void connectToServer() {

        final InetSocketAddress targetAddress = Config.SERVER_SOCKET_ADDRESS;

        LOGGER.info(this, "connect to server " + targetAddress);

        gameNetwork.connect(targetAddress);
    }

    public ClientNetwork getGameNetwork() {
        return gameNetwork;
    }

    /**
     * @return игровому сервер.
     */
    public NetServer getGameServer() {
        return gameServer;
    }

    /**
     * @param server игровой сервер.
     */
    public void setGameServer(final NetServer server) {
        this.gameServer = server;

        if (server == null) {
            LOGGER.info("close game server");
        }
    }

    /**
     * @return флаг подключения к игровому серверу.
     */
    public final boolean isGameConnected() {
        return gameConnected;
    }

    /**
     * @param connected флаг подключения к игровому серверу.
     */
    public synchronized final void setGameConnected(final boolean connected) {
        this.gameConnected = connected;

        if (!connected && gameServer != null) {
            gameServer.close();
        }
    }

    /**
     * Отправка пакета игровому серверу.
     *
     * @param packet отправляемый пакет.
     */
    public void sendPacketToGameServer(final ClientPacket packet) {

        final NetServer server = getGameServer();

        if (server == null) {
            throw new RuntimeException("not found Game Server.");
        }

        server.sendPacket(packet);
    }
}
