package org.sample.server.network.model;

import org.sample.server.LocalObjects;
import org.sample.server.manager.AccountManager;
import org.sample.server.manager.ExecutorManager;
import org.sample.server.model.impl.Account;
import org.sample.server.model.tank.PlayerTank;
import org.sample.server.network.ClientPacket;
import org.sample.server.network.ClientPacketFactory;
import org.sample.server.network.ClientPacketType;
import org.sample.server.network.EmptyCrypt;
import org.sample.server.network.PacketFactory;
import org.sample.server.network.ServerPacket;
import org.sample.server.network.packet.server.ResponseConnected;

import rlib.network.server.client.impl.AbstractClient;
import rlib.util.pools.Foldable;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.PoolFactory;

/**
 * Модель клиента игрока.
 *
 * @author Ronn
 */
public class GameClient extends AbstractClient<Account, PlayerTank, GameConnection, EmptyCrypt, ClientPacket, ServerPacket> implements Foldable {

    private static final FoldablePool<GameClient> POOL = PoolFactory.newAtomicFoldablePool(GameClient.class);

    public static final GameClient create(final GameConnection connection) {

        final GameClient client = POOL.take();

        if (client == null) {
            return new GameClient(connection, EmptyCrypt.getInstance());
        }

        client.switchTo(connection);

        return client;
    }

    /**
     * Фабрика клиентских пакетов.
     */
    private final PacketFactory<GameClient, ClientPacket, ClientPacketType> packetFactory;

    public GameClient(final GameConnection connection, final EmptyCrypt crypt) {
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

        POOL.put(this);
    }

    @Override
    protected void execute(final ClientPacket packet) {

        final ExecutorManager executor = ExecutorManager.getInstance();

        if (packet.isSynchronized()) {
            executor.runSyncPacket(packet);
        } else {
            executor.runAsyncPacket(packet);
        }
    }

    @Override
    public void finalyze() {
        setOwner(null);
        setAccount(null);
    }

    @Override
    public String getHostAddress() {

        final GameConnection connection = getConnection();

        if (connection != null && !connection.isClosed()) {
            return connection.getRemoteAddress();
        }

        return "unknown";
    }

    /**
     * @return название клиента.
     */
    public String getName() {

        final Account account = getAccount();

        if (account != null) {
            return account.getName();
        }

        return getHostAddress();
    }

    /**
     * @return фабрика клиентских пакетов.
     */
    public PacketFactory<GameClient, ClientPacket, ClientPacketType> getPacketFactory() {
        return packetFactory;
    }

    @Override
    public void reinit() {
        setClosed(false);
    }

    /**
     * Отправка пакета.
     *
     * @param packet       отправляемый пакет.
     * @param increaseSend нужно ли увеличивать счетчик отправок.
     */
    public final void sendPacket(final ServerPacket packet, final boolean increaseSend) {

        if (increaseSend) {
            packet.increaseSends();
        }

        sendPacket(packet);
    }

    @Override
    public void successfulConnection() {
        sendPacket(ResponseConnected.getInstance(LocalObjects.get()), true);
    }
}
