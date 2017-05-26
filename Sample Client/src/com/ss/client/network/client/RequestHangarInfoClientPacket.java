package com.ss.client.network.client;

import com.ss.client.network.ClientPacket;
import org.jetbrains.annotations.NotNull;
import rlib.network.packet.SendablePacket;
import rlib.network.packet.SendablePacketType;

/**
 * The packet to request information about a player's hangar.
 *
 * @author JavaSaBr
 */
public class RequestHangarInfoClientPacket extends ClientPacket {

    @NotNull
    private static final SendablePacketType<ClientPacket> REQUEST_HANGAR_INFO_TYPE =
            new SendablePacketType<>(RequestHangarInfoClientPacket.class, 3);

    /**
     * The example of this packet to create new packets of the type.
     */
    @NotNull
    private static final RequestHangarInfoClientPacket EXAMPLE = new RequestHangarInfoClientPacket();

    @NotNull
    public static RequestHangarInfoClientPacket getInstance() {
        return EXAMPLE.newInstance();
    }

    @NotNull
    @Override
    public SendablePacketType<? extends SendablePacket> getPacketType() {
        return REQUEST_HANGAR_INFO_TYPE;
    }
}
