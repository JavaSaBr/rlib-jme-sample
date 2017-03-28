package com.ss.server.network.packet.server;

import static java.util.Objects.requireNonNull;
import com.ss.server.network.ServerPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.network.packet.SendablePacketType;

import java.nio.ByteBuffer;

/**
 * The packet with data about result authentication.
 *
 * @author JavaSaBr
 */
public class AuthResultServerPacket extends ServerPacket {

    @NotNull
    private static final SendablePacketType<ServerPacket> AUTH_RESULT_TYPE =
            new SendablePacketType<>(AuthResultServerPacket.class, 1);

    public enum ResultType {
        SUCCESSFUL,
        INCORRECT_NAME,
        INCORRECT_PASSWORD;
    }

    /**
     * The example of this packet to create new packets of the type.
     */
    @NotNull
    private static final AuthResultServerPacket EXAMPLE = new AuthResultServerPacket();

    @NotNull
    public static AuthResultServerPacket getInstance(@NotNull final ResultType resultType) {

        final AuthResultServerPacket packet = EXAMPLE.newInstance();
        packet.resultType = resultType;

        return packet;
    }

    /**
     * The result type.
     */
    @Nullable
    private volatile ResultType resultType;

    @Override
    protected void writeImpl(@NotNull final ByteBuffer buffer) {
        super.writeImpl(buffer);
        writeByte(buffer, getResultType().ordinal());
    }

    /**
     * @return the result type.
     */
    @NotNull
    private ResultType getResultType() {
        return requireNonNull(resultType);
    }

    @NotNull
    @Override
    public SendablePacketType<ServerPacket> getPacketType() {
        return AUTH_RESULT_TYPE;
    }

    @Override
    public void free() {
        resultType = null;
    }

    @Override
    public String toString() {
        return "AuthResultServerPacket{" + "resultType=" + resultType + '}';
    }
}
