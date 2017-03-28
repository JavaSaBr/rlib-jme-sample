package com.ss.client.network.server;

import com.ss.client.manager.VehicleTemplateManager;
import com.ss.client.network.ServerPacket;
import com.ss.client.template.PlayerVehicleTemplate;
import org.jetbrains.annotations.NotNull;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.ReadablePacketType;

/**
 * The packet to receive notification about successful connect to the server.
 *
 * @author JavaSaBr
 */
public class PlayerVehicleTemplateServerPacket extends ObjectTemplateServerPacket<PlayerVehicleTemplate> {

    @NotNull
    private static final ReadablePacketType<ServerPacket> PLAYER_VEHICLE_TEMPLATE_TYPE =
            new ReadablePacketType<>(new PlayerVehicleTemplateServerPacket(), 3);

    @Override
    protected void runImpl() {
        final VehicleTemplateManager templateManager = VehicleTemplateManager.getInstance();
        templateManager.register(getTemplateId(), getTemplate());
    }

    @Override
    protected PlayerVehicleTemplate createTemplate(final int templateId) {
        return new PlayerVehicleTemplate(templateId);
    }

    @NotNull
    @Override
    public ReadablePacketType<? extends ReadablePacket> getPacketType() {
        return PLAYER_VEHICLE_TEMPLATE_TYPE;
    }
}
