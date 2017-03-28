package com.ss.client.network.server;

import com.ss.client.network.ServerPacket;
import com.ss.client.ui.event.impl.AuthResultEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.ReadablePacketType;

import java.nio.ByteBuffer;

/**
 * The packet to receive data about result authentication.
 *
 * @author JavaSaBr
 */
public class AuthResultServerPacket extends ServerPacket {

    @NotNull
    private static final ReadablePacketType<ServerPacket> AUTH_RESULT_TYPE =
            new ReadablePacketType<>(new AuthResultServerPacket(), 1);

    public enum ResultType {
        SUCCESSFUL,
        INCORRECT_NAME,
        INCORRECT_PASSWORD;

        @NotNull
        private static final ResultType[] VALUES = values();

        @NotNull
        private static ResultType valueOf(final int index) {
            return VALUES[index];
        }
    }

    /**
     * The result type.
     */
    @Nullable
    private volatile ResultType resultType;

    @Override
    protected void readImpl(@NotNull final ByteBuffer buffer) {
        super.readImpl(buffer);
        resultType = ResultType.valueOf(readByte(buffer));
    }

    @Override
    protected void runImpl() {
        FX_EVENT_MANAGER.notify(AuthResultEvent.newInstance(resultType));
    }

    @NotNull
    @Override
    public ReadablePacketType<? extends ReadablePacket> getPacketType() {
        return AUTH_RESULT_TYPE;
    }

    @Override
    public void free() {
        resultType = null;
    }
}
