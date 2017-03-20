package com.ss.server.network.model;

import com.ss.server.Config;
import com.ss.server.manager.AccountManager;
import com.ss.server.manager.ExecutorManager;
import com.ss.server.model.Account;
import com.ss.server.model.tank.PlayerTank;
import com.ss.server.network.ClientPacket;
import com.ss.server.network.EmptyCrypt;
import com.ss.server.network.ServerPacket;
import com.ss.server.network.packet.server.ConnectedNotifierServerPacket;
import org.jetbrains.annotations.NotNull;
import rlib.network.packet.ReadablePacket;
import rlib.network.server.client.impl.AbstractClient;

/**
 * The implementation of a game client.
 *
 * @author JavaSaBr
 */
public class GameClient extends AbstractClient<Account, PlayerTank> {

    GameClient(@NotNull final GameConnection connection, @NotNull final EmptyCrypt crypt) {
        super(connection, crypt);
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
    protected void execute(@NotNull final ReadablePacket packet) {

        final ClientPacket clientPacket = (ClientPacket) packet;
        final ExecutorManager executor = ExecutorManager.getInstance();

        if (clientPacket.isSynchronized()) {
            executor.runSyncPacket(clientPacket);
        } else {
            executor.runAsyncPacket(clientPacket);
        }
    }

    /**
     * @return the client name.
     */
    public String getName() {

        final Account account = getAccount();
        if (account != null) return account.getName();

        return "Unknown";
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
        sendPacket(ConnectedNotifierServerPacket.getInstance(Config.SERVER_VERSION), true);
    }

    @Override
    public String toString() {
        return "GameClient{}";
    }
}
