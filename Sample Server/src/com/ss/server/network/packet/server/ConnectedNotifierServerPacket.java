package com.ss.server.network.packet.server;

import com.ss.server.network.ServerPacket;
import com.ss.server.LocalObjects;
import com.ss.server.network.ServerPacketType;
import org.jetbrains.annotations.NotNull;

/**
 * The packet to notify client about successful connect to the server.
 *
 * @author JavaSaBr
 */
public class ConnectedNotifierServerPacket extends ServerPacket {

    /**
     * The example of this packet to create new packets of the type.
     */
    @NotNull
    private static final ConnectedNotifierServerPacket EXAMPLE = new ConnectedNotifierServerPacket();

    @NotNull
    public static ConnectedNotifierServerPacket getInstance(@NotNull final LocalObjects local) {
        return local.create(EXAMPLE);
    }

    @NotNull
    @Override
    public ServerPacketType getPacketType() {
        return ServerPacketType.CONNECTED_NOTIFIER;
    }
}
