package com.ss.client.network.server;

import com.ss.client.config.Config;
import com.ss.client.network.ServerPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.ReadablePacketType;
import rlib.util.StringUtils;

import java.nio.ByteBuffer;

/**
 * The packet to receive notification about successful connect to the server.
 *
 * @author JavaSaBr
 */
public class ConnectedNotifierServerPacket extends ServerPacket {

    @NotNull
    private static final ReadablePacketType<ServerPacket> CONNECTED_NOTIFIER_TYPE =
            new ReadablePacketType<>(new ConnectedNotifierServerPacket(), 2);

    /**
     * The server version.
     */
    @Nullable
    private String serverVersion;

    @Override
    protected void readImpl(@NotNull final ByteBuffer buffer) {
        super.readImpl(buffer);
        serverVersion = readString(buffer);
    }

    @Override
    protected void runImpl() {

        if (StringUtils.equals(serverVersion, Config.GAME_VERSION)) {
            LOGGER.warning(this, "incorrect server version...");
        }

        LOGGER.info("The client was connected to the server of the version " + serverVersion);
    }

    @NotNull
    @Override
    public ReadablePacketType<? extends ReadablePacket> getPacketType() {
        return CONNECTED_NOTIFIER_TYPE;
    }

    @Override
    public void free() {
        serverVersion = null;
    }
}
