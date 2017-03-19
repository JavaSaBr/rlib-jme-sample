package com.ss.client.network;

import com.ss.client.network.model.GameServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.geom.Vector3f;
import rlib.network.packet.impl.AbstractRunnableReadablePacket;

/**
 * The base implementation of the packet from Server.
 *
 * @author JavaSaBr
 */
public abstract class ServerPacket extends AbstractRunnableReadablePacket {

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

    @Override
    protected void readImpl() {
    }

    /**
     * Read a vector value.
     *
     * @return the vector value.
     */
    @NotNull
    protected final Vector3f readVector() {
        return Vector3f.newInstance(readFloat(), readFloat(), readFloat());
    }

    @Nullable
    @Override
    public GameServer getOwner() {
        return (GameServer) super.getOwner();
    }
}
