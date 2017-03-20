package com.ss.server.network.packet.server;

import static java.util.Objects.requireNonNull;
import com.ss.server.network.ServerPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.network.packet.SendablePacketType;

import java.nio.ByteBuffer;

/**
 * The packet to notify client about successful connect to the server.
 *
 * @author JavaSaBr
 */
public class ConnectedNotifierServerPacket extends ServerPacket {

    @NotNull
    private static final SendablePacketType<ServerPacket> CONNECTED_NOTIFIER_TYPE =
            new SendablePacketType<>(ConnectedNotifierServerPacket.class, 2);

    /**
     * The example of this packet to create new packets of the type.
     */
    @NotNull
    private static final ConnectedNotifierServerPacket EXAMPLE = new ConnectedNotifierServerPacket();

    /**
     * The server version.
     */
    @Nullable
    private volatile String serverVersion;

    @NotNull
    public static ConnectedNotifierServerPacket getInstance(@NotNull final String serverVersion) {
        final ConnectedNotifierServerPacket packet = EXAMPLE.newInstance();
        packet.serverVersion = serverVersion;
        return packet;
    }

    @Override
    protected void writeImpl(@NotNull final ByteBuffer buffer) {
        super.writeImpl(buffer);
        writeString(buffer, requireNonNull(serverVersion));
    }

    @Override
    public void free() {
        serverVersion = null;
    }

    @NotNull
    @Override
    public SendablePacketType<ServerPacket> getPacketType() {
        return CONNECTED_NOTIFIER_TYPE;
    }

    @Override
    public String toString() {
        return "ConnectedNotifierServerPacket{" + "serverVersion='" + serverVersion + '\'' + '}';
    }
}
