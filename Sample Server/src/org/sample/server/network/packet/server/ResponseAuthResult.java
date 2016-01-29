package org.sample.server.network.packet.server;

import org.sample.server.LocalObjects;
import org.sample.server.network.ServerPacket;
import org.sample.server.network.ServerPacketType;

import java.nio.ByteBuffer;

/**
 * Реализация пакета для отправки результата попытки авторизации.
 *
 * @author Ronn
 */
public class ResponseAuthResult extends ServerPacket {

    public static enum ResultType {
        SUCCESSFUL,
        INCORRECT_NAME,
        INCORRECT_PASSWORD;
    }

    private static final ResponseAuthResult EXAMPLE = new ResponseAuthResult();

    public static ResponseAuthResult getInstance(final ResultType resultType, final LocalObjects local) {

        final ResponseAuthResult packet = local.create(EXAMPLE);
        packet.resultType = resultType;

        return packet;
    }

    /**
     * Тип результата попытки авторизоваться.
     */
    private volatile ResultType resultType;


    @Override
    protected void writeImpl(ByteBuffer buffer) {
        writeOpcode(buffer);
        writeByte(buffer, resultType.ordinal());
    }

    @Override
    public ServerPacketType getPacketType() {
        return ServerPacketType.RESPONSE_AUTH_RESULT;
    }

    @Override
    public void finalyze() {
        resultType = null;
    }
}
