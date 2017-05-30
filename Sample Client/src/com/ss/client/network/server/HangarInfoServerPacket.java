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
import java.nio.ByteOrder;

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
     * The received data.
     */
    @NotNull
    private final ByteBuffer data;

    public HangarInfoServerPacket() {
        this.data = ByteBuffer.allocate(1024).order(ByteOrder.LITTLE_ENDIAN);
    }

    @Override
    protected void readImpl(@NotNull final ByteBuffer buffer) {
        super.readImpl(buffer);

        data.clear();
        data.put(buffer);
        data.flip();
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

        return vehicleTemplate.takeInstance(objectId);
    }

    @Override
    public boolean isSynchronized() {
        return true;
    }

    @Override
    protected void runImpl() {

        final EmptyPlayerTemplate playerTemplate = EmptyPlayerTemplate.getInstance();
        final Player player = playerTemplate.takeInstance(readLong(data));
        final PlayerVehicle currentVehicle = readVehicle(data);

        if(currentVehicle == null) {
            player.deleteMe(localObjects());
            return;
        }

        final Array<PlayerVehicle> availableVehicles = player.getAvailableVehicles();
        availableVehicles.add(currentVehicle);

        final int otherVehicles = readByte(data);

        for(int i = 0; i < otherVehicles; i++) {

            final PlayerVehicle vehicle = readVehicle(data);

            if(vehicle == null) {
                player.deleteMe(localObjects());
                return;
            }

            availableVehicles.add(vehicle);
        }

        player.setCurrentVehicle(currentVehicle);

        FX_EVENT_MANAGER.notify(LoadedHangarEvent.newInstance(requireNonNull(player)));
    }

    @NotNull
    @Override
    public ReadablePacketType<? extends ReadablePacket> getPacketType() {
        return HANGAR_INFO_TYPE;
    }
}
