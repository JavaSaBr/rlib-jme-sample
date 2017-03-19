package com.ss.server.network.model;

import com.ss.server.network.ClientPacket;
import com.ss.server.network.ClientPacketType;
import com.ss.server.network.PacketFactory;
import com.ss.server.network.ServerPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.network.server.ServerNetwork;
import rlib.network.server.client.impl.AbstractClientConnection;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * The implementation of connection between server and client.
 *
 * @author JavaSaBr
 */
public class GameConnection extends AbstractClientConnection<GameClient, ClientPacket, ServerPacket> {

    GameConnection(@NotNull final ServerNetwork network, @NotNull final AsynchronousSocketChannel channel,
                   @NotNull final Class<ServerPacket> sendableType) {
        super(network, channel, sendableType);
    }

    /**
     * Create a packet from the data buffer.
     */
    @Nullable
    protected ClientPacket getPacket(@NotNull final ByteBuffer buffer, @NotNull final GameClient client) {
        if (buffer.remaining() < SIZE_BYTES_SIZE) return null;

        final int packetTypeId = buffer.getShort() & 0xFFFF;
        final ClientPacketType type = ClientPacketType.getPacketType(packetTypeId);
        final PacketFactory<GameClient, ClientPacket, ClientPacketType> factory = client.getPacketFactory();

        return factory.create(type);
    }
}
