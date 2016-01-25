package org.sample.client.network.model;

import org.sample.client.manager.ExecutorManager;
import org.sample.client.network.ClientPacket;
import org.sample.client.network.Network;
import org.sample.client.network.ServerPacket;
import rlib.network.GameCrypt;
import rlib.network.client.server.impl.AbstractServer;

import java.time.ZoneId;

/**
 * Реализация сервера к которому подключается клиент.
 *
 * @author Ronn
 */
public class NetServer extends AbstractServer<NetConnection, GameCrypt, ServerPacket, ClientPacket> {

    private static final ExecutorManager EXECUTOR_MANAGER = ExecutorManager.getInstance();

    /**
     * Тип сервера.
     */
    private final ServerType type;

    /**
     * Ид зоны, в которой находится сервер.
     */
    private ZoneId zoneId;

    public NetServer(final NetConnection connection, final ServerType type) {
        super(connection, EmptyCrypt.getInstance());

        this.type = type;
        this.zoneId = ZoneId.systemDefault();
    }

    @Override
    public void close() {
        super.close();

        final Network network = Network.getInstance();
        network.setGameServer(null);

        LOGGER.info(this, "close " + type + ".");
    }

    @Override
    protected void execute(final ServerPacket packet) {

        if (packet.isWaiteable()) {
            EXECUTOR_MANAGER.waitableExecute(packet);
        } else if (packet.isSynchronized()) {
            EXECUTOR_MANAGER.syncExecute(packet);
        } else {
            EXECUTOR_MANAGER.asyncExecute(packet);
        }
    }

    /**
     * @return тип сервера.
     */
    public ServerType getType() {
        return type;
    }

    /**
     * @return ид зоны, в которой находится сервер.
     */
    public ZoneId getZoneId() {
        return zoneId;
    }

    /**
     * @param zoneId ид зоны, в которой находится сервер.
     */
    public void setZoneId(final ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public void sendPacket(final ClientPacket packet) {
        packet.increaseSends();
        super.sendPacket(packet);
    }

    @Override
    public String toString() {
        return "NetServer{" +
                "type=" + type +
                ", zoneId=" + zoneId +
                "} " + super.toString();
    }
}
