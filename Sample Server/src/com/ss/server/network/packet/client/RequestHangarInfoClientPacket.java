package com.ss.server.network.packet.client;

import static java.util.Objects.requireNonNull;
import com.ss.server.LocalObjects;
import com.ss.server.manager.PlayerManager;
import com.ss.server.model.Account;
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

    private static final PlayerManager PLAYER_MANAGER = PlayerManager.getInstance();

    @Override
    protected void executeImpl(@NotNull final LocalObjects local, final long currentTime) {

        final GameClient owner = getOwner();

        if (owner == null) {
            LOGGER.warning("not found a client.");
            return;
        }

        if (owner.getOwner() == null) {
            final Account account = requireNonNull(owner.getAccount());
            final Player loadedPlayer = PLAYER_MANAGER.loadPlayer(account, local);
            if (loadedPlayer != null) loadedPlayer.setClient(owner);
            owner.setOwner(loadedPlayer);
        }

        final Player player = getPlayer();

        if (player == null) {
            LOGGER.warning("not found a player for the client " + this.owner);
            return;
        }

        player.sendPacket(HangarInfoServerPacket.getInstance(player), local, true);
    }

    @NotNull
    @Override
    public ReadablePacketType<? extends ReadablePacket> getPacketType() {
        return REQUEST_HANGAR_INFO_TYPE;
    }
}
