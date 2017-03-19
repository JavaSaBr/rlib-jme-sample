package com.ss.server.network.packet.server;

import static java.util.Objects.requireNonNull;
import com.ss.server.LocalObjects;
import com.ss.server.network.ServerPacket;
import com.ss.server.network.ServerPacketType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * The packet with data about result authentication.
 *
 * @author JavaSaBr
 */
public class AuthResultServerPacket extends ServerPacket {

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
    public static AuthResultServerPacket getInstance(@NotNull final ResultType resultType,
                                                     @NotNull final LocalObjects local) {

        final AuthResultServerPacket packet = local.create(EXAMPLE);
        packet.resultType = resultType;

        return packet;
    }

    /**
     * The result type.
     */
    @Nullable
    private volatile ResultType resultType;

    @Override
    protected void writeImpl(@NotNull ByteBuffer buffer) {
        super.writeImpl(buffer);
        writeByte(buffer, requireNonNull(resultType).ordinal());
    }

    @NotNull
    @Override
    public ServerPacketType getPacketType() {
        return ServerPacketType.AUTH_RESULT;
    }

    @Override
    public void free() {
        resultType = null;
    }
}
