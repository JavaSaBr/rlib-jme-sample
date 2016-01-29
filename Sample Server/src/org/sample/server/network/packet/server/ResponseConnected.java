package org.sample.server.network.packet.server;

import org.sample.server.LocalObjects;
import org.sample.server.network.ServerPacket;
import org.sample.server.network.ServerPacketType;

/**
 * Реализация пакета уведомляющего об успешности подключения клиента к серверу.
 *
 * @author Ronn
 */
public class ResponseConnected extends ServerPacket {

    private static final ResponseConnected EXAMPLE = new ResponseConnected();

    public static ResponseConnected getInstance(final LocalObjects local) {
        final ResponseConnected packet = local.create(EXAMPLE);
        return packet;
    }

    @Override
    public ServerPacketType getPacketType() {
        return ServerPacketType.RESPONSE_CONNECTED;
    }
}
