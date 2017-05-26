package com.ss.server.network.packet.client;

import com.ss.server.LocalObjects;
import com.ss.server.model.player.Player;
import com.ss.server.network.ClientPacket;
import com.ss.server.network.model.GameClient;
import com.ss.server.network.packet.server.HangarInfoServerPacket;
import org.jetbrains.annotations.NotNull;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.ReadablePacketType;

/**
 * The packet to request information about a player's hangar.
 *
 * @author JavaSaBr
 */
public class RequestHangarInfoClientPacket extends ClientPacket {

    @NotNull
    private static final ReadablePacketType<ClientPacket> REQUEST_HANGAR_INFO_TYPE =
            new ReadablePacketType<>(new RequestHangarInfoClientPacket(), 3);

    @Override
    protected void executeImpl(@NotNull final LocalObjects local, final long currentTime) {

        final GameClient owner = getOwner();
        final Player player = owner.getOwner();

        if (player == null) {
            LOGGER.warning("not found a player for the client " + owner);
            return;
        }

        owner.sendPacket(HangarInfoServerPacket.getInstance(player), true);
    }

    @NotNull
    @Override
    public ReadablePacketType<? extends ReadablePacket> getPacketType() {
        return REQUEST_HANGAR_INFO_TYPE;
    }
}
