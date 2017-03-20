package com.ss.client.network;

import com.ss.client.manager.FXEventManager;
import com.ss.client.network.model.GameServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.geom.Vector3f;
import rlib.network.packet.impl.AbstractRunnableReadablePacket;

import java.nio.ByteBuffer;

/**
 * The base implementation of the packet from Server.
 *
 * @author JavaSaBr
 */
public abstract class ServerPacket extends AbstractRunnableReadablePacket {

    @NotNull
    protected static final FXEventManager FX_EVENT_MANAGER = FXEventManager.getInstance();

    @Override
    public boolean isSynchronized() {
        return false;
    }

    /**
     * @return true if this packet is waitable.
     */
    public boolean isWaitable() {
        return false;
    }

    /**
     * Read a vector value.
     *
     * @param buffer the buffer with data.
     * @return the vector value.
     */
    @NotNull
    protected final Vector3f readVector(@NotNull final ByteBuffer buffer) {
        return Vector3f.newInstance(readFloat(buffer), readFloat(buffer), readFloat(buffer));
    }

    @Nullable
    @Override
    public GameServer getOwner() {
        return (GameServer) super.getOwner();
    }
}
