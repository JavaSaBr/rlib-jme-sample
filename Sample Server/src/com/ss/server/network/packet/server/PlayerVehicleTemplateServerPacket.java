package com.ss.server.network.packet.server;

import com.ss.server.network.ServerPacket;
import com.ss.server.template.PlayerVehicleTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.network.packet.SendablePacketType;

/**
 * The packet to notify client about successful connect to the server.
 *
 * @author JavaSaBr
 */
public class PlayerVehicleTemplateServerPacket extends ObjectTemplateServerPacket<PlayerVehicleTemplate> {

    @NotNull
    private static final SendablePacketType<ServerPacket> PLAYER_VEHICLE_TEMPLATE_TYPE =
            new SendablePacketType<>(PlayerVehicleTemplateServerPacket.class, 3);

    /**
     * The example of this packet to create new packets of the type.
     */
    @NotNull
    private static final PlayerVehicleTemplateServerPacket EXAMPLE = new PlayerVehicleTemplateServerPacket();

    @NotNull
    public static PlayerVehicleTemplateServerPacket getInstance(@Nullable final PlayerVehicleTemplate template,
                                                                final int templateId) {

        final PlayerVehicleTemplateServerPacket packet = EXAMPLE.newInstance();
        packet.templateId = templateId;
        packet.template = template;

        return packet;
    }

    @NotNull
    @Override
    public SendablePacketType<ServerPacket> getPacketType() {
        return PLAYER_VEHICLE_TEMPLATE_TYPE;
    }
}
