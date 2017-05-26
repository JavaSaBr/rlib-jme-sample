package com.ss.client.network.server;

import static com.ss.client.util.LocalObjects.localObjects;
import static java.util.Objects.requireNonNull;
import com.ss.client.manager.VehicleTemplateManager;
import com.ss.client.model.player.Player;
import com.ss.client.model.player.PlayerVehicle;
import com.ss.client.network.ServerPacket;
import com.ss.client.template.EmptyPlayerTemplate;
import com.ss.client.template.PlayerVehicleTemplate;
import com.ss.client.ui.event.impl.LoadedHangarEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.ReadablePacketType;
import rlib.util.array.Array;

import java.nio.ByteBuffer;

/**
 * The packet to receive data about hangar information.
 *
 * @author JavaSaBr
 */
public class HangarInfoServerPacket extends ServerPacket {

    @NotNull
    private static final ReadablePacketType<ServerPacket> HANGAR_INFO_TYPE =
            new ReadablePacketType<>(new HangarInfoServerPacket(), 4);

    /**
     * The current player.
     */
    @Nullable
    private volatile Player player;

    @Override
    protected void readImpl(@NotNull final ByteBuffer buffer) {
        super.readImpl(buffer);

        final EmptyPlayerTemplate playerTemplate = EmptyPlayerTemplate.getInstance();
        final Player player = playerTemplate.takeInstance(Player.class, readLong(buffer));
        final PlayerVehicle currentVehicle = readVehicle(buffer);

        if(currentVehicle == null) {
            player.deleteMe(localObjects());
            return;
        }

        final Array<PlayerVehicle> availableVehicles = player.getAvailableVehicles();
        availableVehicles.add(currentVehicle);

        final int otherVehicles = readByte(buffer);

        for(int i = 0; i < otherVehicles; i++) {

            final PlayerVehicle vehicle = readVehicle(buffer);

            if(vehicle == null) {
                player.deleteMe(localObjects());
                return;
            }

            availableVehicles.add(vehicle);
        }

        player.setCurrentVehicle(currentVehicle);
    }

    @Nullable
    private PlayerVehicle readVehicle(@NotNull final ByteBuffer buffer) {

        final long objectId = readLong(buffer);
        final int templateId = readInt(buffer);

        final VehicleTemplateManager vehicleTemplateManager = VehicleTemplateManager.getInstance();
        final PlayerVehicleTemplate vehicleTemplate = vehicleTemplateManager.getPlayerTemplate(templateId);

        if (vehicleTemplate == null) {
            LOGGER.warning("not found vehicle template for the id " + templateId);
            return null;
        }

        return vehicleTemplate.takeInstance(PlayerVehicle.class, objectId);
    }

    @Override
    protected void runImpl() {
        FX_EVENT_MANAGER.notify(LoadedHangarEvent.newInstance(requireNonNull(player)));
    }

    @NotNull
    @Override
    public ReadablePacketType<? extends ReadablePacket> getPacketType() {
        return HANGAR_INFO_TYPE;
    }

    @Override
    public void free() {
        player = null;
    }
}
