package com.ss.client.network.client;

import com.ss.client.network.ClientPacket;
import org.jetbrains.annotations.NotNull;
import rlib.network.packet.SendablePacket;
import rlib.network.packet.SendablePacketType;

/**
 * The packet to send request to get data of a player vehicle template.
 *
 * @author JavaSaBr
 */
public class RequestPlayerVehicleTemplateClientPacket extends RequestObjectTemplateClientPacket {

    @NotNull
    private static final SendablePacketType<ClientPacket> REQUEST_PLAYER_VEHICLE_TYPE =
            new SendablePacketType<>(RequestPlayerVehicleTemplateClientPacket.class, 2);

    /**
     * The example of this packet to create new packets of the type.
     */
    @NotNull
    private static final RequestPlayerVehicleTemplateClientPacket EXAMPLE = new RequestPlayerVehicleTemplateClientPacket();

    @NotNull
    public static RequestPlayerVehicleTemplateClientPacket getInstance(final int templateId) {
        final RequestPlayerVehicleTemplateClientPacket packet = EXAMPLE.newInstance();
        packet.templateId = templateId;
        return packet;
    }

    @NotNull
    @Override
    public SendablePacketType<? extends SendablePacket> getPacketType() {
        return REQUEST_PLAYER_VEHICLE_TYPE;
    }
}
