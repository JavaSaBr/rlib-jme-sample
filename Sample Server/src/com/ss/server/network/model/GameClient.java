package com.ss.server.network.model;

import com.ss.server.LocalObjects;
import com.ss.server.manager.AccountManager;
import com.ss.server.manager.ExecutorManager;
import com.ss.server.model.Account;
import com.ss.server.model.tank.PlayerTank;
import com.ss.server.network.*;
import com.ss.server.network.packet.server.ConnectedNotifierServerPacket;
import org.jetbrains.annotations.NotNull;
import rlib.network.server.client.impl.AbstractClient;

/**
 * The implementation of a game client.
 *
 * @author JavaSaBr
 */
public class GameClient extends
        AbstractClient<Account, PlayerTank, GameConnection, EmptyCrypt, ClientPacket, ServerPacket> {

    /**
     * The client packet factory.
     */
    @NotNull
    private final PacketFactory<GameClient, ClientPacket, ClientPacketType> packetFactory;

    public GameClient(@NotNull final GameConnection connection, @NotNull final EmptyCrypt crypt) {
        super(connection, crypt);
        this.packetFactory = new ClientPacketFactory();
    }

    @Override
    public void close() {

        final String name = getName();

        final AccountManager accountManager = AccountManager.getInstance();
        final Account account = getAccount();

        if (account != null) {
            accountManager.removeAccount(account);
        }

        LOGGER.info(this, "close client \"" + name + "\".");

        super.close();
    }

    @Override
    protected void execute(@NotNull final ClientPacket packet) {

        final ExecutorManager executor = ExecutorManager.getInstance();

        if (packet.isSynchronized()) {
            executor.runSyncPacket(packet);
        } else {
            executor.runAsyncPacket(packet);
        }
    }

    @NotNull
    @Override
    public String getHostAddress() {

        final GameConnection connection = getConnection();

        if (!connection.isClosed()) {
            return connection.getRemoteAddress();
        }

        return "unknown";
    }

    /**
     * @return the client name.
     */
    public String getName() {

        final Account account = getAccount();
        if (account != null) return account.getName();

        return getHostAddress();
    }

    /**
     * @return the client packet factory.
     */
    @NotNull
    PacketFactory<GameClient, ClientPacket, ClientPacketType> getPacketFactory() {
        return packetFactory;
    }

    /**
     * Send a packet to game client.
     *
     * @param packet       the packet.
     * @param increaseSend true if need to increase send counter of the packet.
     */
    public final void sendPacket(@NotNull final ServerPacket packet, final boolean increaseSend) {
        if (increaseSend) packet.increaseSends();
        sendPacket(packet);
    }

    @Override
    public void successfulConnection() {
        sendPacket(ConnectedNotifierServerPacket.getInstance(LocalObjects.get()), true);
    }
}
