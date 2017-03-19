package com.ss.server.network.packet.server;

import com.ss.server.network.ServerPacket;
import org.jetbrains.annotations.NotNull;
import rlib.network.packet.SendablePacketType;

/**
 * The packet to notify client about successful connect to the server.
 *
 * @author JavaSaBr
 */
public class ConnectedNotifierServerPacket extends ServerPacket {

    @NotNull
    private static final SendablePacketType<ServerPacket> CONNECTED_NOTIFIER_TYPE = new SendablePacketType<>(ConnectedNotifierServerPacket.class, 2);

    /**
     * The example of this packet to create new packets of the type.
     */
    @NotNull
    private static final ConnectedNotifierServerPacket EXAMPLE = new ConnectedNotifierServerPacket();

    @NotNull
    public static ConnectedNotifierServerPacket getInstance() {
        return EXAMPLE.newInstance();
    }

    @NotNull
    @Override
    public SendablePacketType<ServerPacket> getPacketType() {
        return CONNECTED_NOTIFIER_TYPE;
    }
}
