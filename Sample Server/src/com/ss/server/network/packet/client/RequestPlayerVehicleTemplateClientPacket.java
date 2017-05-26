package com.ss.server.network.packet.client;

import static java.util.Objects.requireNonNull;
import com.ss.server.LocalObjects;
import com.ss.server.manager.VehicleTemplateManager;
import com.ss.server.network.ClientPacket;
import com.ss.server.network.model.GameClient;
import com.ss.server.network.packet.server.PlayerVehicleTemplateServerPacket;
import com.ss.server.template.PlayerVehicleTemplate;
import org.jetbrains.annotations.NotNull;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.ReadablePacketType;

import java.nio.ByteBuffer;

/**
 * The client packet to get data of a player vehicle template.
 *
 * @author JavaSaBr
 */
public class RequestPlayerVehicleTemplateClientPacket extends ClientPacket {

    @NotNull
    private static final ReadablePacketType<ClientPacket> PLAYER_VEHICLE_TEMPLATE_TYPE =
            new ReadablePacketType<>(new RequestPlayerVehicleTemplateClientPacket(), 2);

    /**
     * The template id.
     */
    private int templateId;

    @Override
    protected void readImpl(@NotNull final ByteBuffer buffer) {
        super.readImpl(buffer);
        templateId = readInt(buffer);
    }

    @Override
    protected void executeImpl(@NotNull final LocalObjects local, final long currentTime) {

        final VehicleTemplateManager templateManager = VehicleTemplateManager.getInstance();
        final PlayerVehicleTemplate template = templateManager.getPlayerTemplate(templateId);

        final GameClient owner = requireNonNull(getOwner());
        owner.sendPacket(PlayerVehicleTemplateServerPacket.getInstance(template, templateId), true);
    }

    @NotNull
    @Override
    public ReadablePacketType<? extends ReadablePacket> getPacketType() {
        return PLAYER_VEHICLE_TEMPLATE_TYPE;
    }

    @Override
    public void free() {
        templateId = 0;
    }

    @Override
    public String toString() {
        return "RequestPlayerVehicleTemplateClientPacket{" + "templateId=" + templateId + '}';
    }
}
