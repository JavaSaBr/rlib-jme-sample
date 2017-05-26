package com.ss.server.network.packet.server;

import static java.util.Objects.requireNonNull;
import com.ss.server.model.player.Player;
import com.ss.server.model.player.PlayerVehicle;
import com.ss.server.network.ServerPacket;
import com.ss.server.template.ObjectTemplate;
import org.jetbrains.annotations.NotNull;
import rlib.network.packet.SendablePacketType;
import rlib.util.array.Array;

import java.nio.ByteBuffer;

/**
 * The packet with data about hangar state of a player.
 *
 * @author JavaSaBr
 */
public class HangarInfoServerPacket extends ServerPacket {

    @NotNull
    private static final SendablePacketType<ServerPacket> HANGAR_INFO_TYPE =
            new SendablePacketType<>(HangarInfoServerPacket.class, 4);

    /**
     * The example of this packet to create new packets of the type.
     */
    @NotNull
    private static final HangarInfoServerPacket EXAMPLE = new HangarInfoServerPacket();

    @NotNull
    public static HangarInfoServerPacket getInstance(@NotNull final Player player) {

        final HangarInfoServerPacket packet = EXAMPLE.newInstance();
        final ByteBuffer data = packet.data;
        try {

            final ObjectTemplate playerTemplate = player.getTemplate();
            final PlayerVehicle currentVehicle = requireNonNull(player.getCurrentVehicle());

            packet.writeInt(data, player.getObjectId());
            packet.writeInt(data, player.getClassId());

            writeVehicle(packet, data, currentVehicle);

            final Array<PlayerVehicle> availableVehicles = player.getAvailableVehicles();

            packet.writeByte(data, availableVehicles.size() - 1);

            for (final PlayerVehicle vehicle : availableVehicles.array()) {
                if (vehicle == null) break;
                if (vehicle == currentVehicle) continue;
                writeVehicle(packet, data, currentVehicle);
            }

        } finally {
            data.flip();
        }

        return packet;
    }

    private static void writeVehicle(@NotNull final ServerPacket packet, @NotNull final ByteBuffer data,
                                     @NotNull final PlayerVehicle vehicle) {
        packet.writeInt(data, vehicle.getObjectId());
        packet.writeInt(data, vehicle.getClassId());
        packet.writeInt(data, vehicle.getTemplate().getTemplateId());
    }

    /**
     * The data.
     */
    @NotNull
    private final ByteBuffer data;

    public HangarInfoServerPacket() {
        this.data = ByteBuffer.allocate(1024);
        this.data.clear();
    }

    @Override
    protected void writeImpl(@NotNull final ByteBuffer buffer) {
        super.writeImpl(buffer);
        writeBuffer(data, buffer);
    }

    @NotNull
    @Override
    public SendablePacketType<ServerPacket> getPacketType() {
        return HANGAR_INFO_TYPE;
    }

    @Override
    public void free() {
        data.clear();
    }

    @Override
    public String toString() {
        return "HangarInfoServerPacket{" + "data=" + data + '}';
    }
}
