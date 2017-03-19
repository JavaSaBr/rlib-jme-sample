package com.ss.client.network.model;

import com.ss.client.manager.ExecutorManager;
import com.ss.client.network.ClientPacket;
import com.ss.client.network.Network;
import com.ss.client.network.ServerPacket;
import org.jetbrains.annotations.NotNull;
import rlib.network.client.server.impl.AbstractServer;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;

/**
 * The implementation of a network game server.
 *
 * @author JavaSaBr
 */
public class GameServer extends AbstractServer {

    @NotNull
    private static final ExecutorManager EXECUTOR_MANAGER = ExecutorManager.getInstance();

    GameServer(@NotNull final GameConnection connection) {
        super(connection, EmptyCrypt.getInstance());
    }

    @Override
    public void close() {
        super.close();

        final Network network = Network.getInstance();
        network.setGameServer(null);

        LOGGER.info(this, "closed connection.");
    }

    @Override
    protected void execute(@NotNull final ReadablePacket packet) {

        final ServerPacket serverPacket = (ServerPacket) packet;

        if (serverPacket.isWaitable()) {
            EXECUTOR_MANAGER.waitableExecute(serverPacket);
        } else if (serverPacket.isSynchronized()) {
            EXECUTOR_MANAGER.syncExecute(serverPacket);
        } else {
            EXECUTOR_MANAGER.asyncExecute(serverPacket);
        }
    }

    @Override
    public void sendPacket(@NotNull final SendablePacket packet) {

        final ClientPacket clientPacket = (ClientPacket) packet;
        clientPacket.increaseSends();

        super.sendPacket(packet);
    }
}
