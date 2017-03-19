package com.ss.client.network;

import com.jme3.math.Vector3f;
import com.ss.client.network.model.GameServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.network.packet.impl.AbstractReusableSendablePacket;

import java.nio.ByteBuffer;

/**
 * The base implementation of the client packet.
 *
 * @author JavaSaBr
 */
public abstract class ClientPacket extends AbstractReusableSendablePacket {

    public boolean isSynchronized() {
        return false;
    }

    /**
     * Write the vector to the buffer.
     */
    protected void writeVector(@NotNull final ByteBuffer buffer, @NotNull final Vector3f vector) {
        writeFloat(buffer, vector.getX());
        writeFloat(buffer, vector.getY());
        writeFloat(buffer, vector.getZ());
    }

    @Nullable
    @Override
    public GameServer getOwner() {
        return (GameServer) super.getOwner();
    }
}
