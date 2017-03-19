package com.ss.server.network;

import com.ss.server.network.model.GameClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.geom.Quaternion4f;
import rlib.geom.Vector3f;
import rlib.network.packet.impl.AbstractReusableSendablePacket;

import java.nio.ByteBuffer;

/**
 * The base implementation of the server packet.
 *
 * @author JavaSaBr
 */
public abstract class ServerPacket extends AbstractReusableSendablePacket {

    /**
     * Write a quaternion to the packet buffer.
     */
    public final void writeRotation(@NotNull final ByteBuffer buffer, @NotNull final Quaternion4f rotation) {
        writeFloat(buffer, rotation.getX());
        writeFloat(buffer, rotation.getY());
        writeFloat(buffer, rotation.getZ());
        writeFloat(buffer, rotation.getW());
    }

    /**
     * Write a vector to packet buffer.
     */
    public final void writeVector(@NotNull final ByteBuffer buffer, @NotNull final Vector3f vector) {
        writeFloat(buffer, vector.getX());
        writeFloat(buffer, vector.getY());
        writeFloat(buffer, vector.getZ());
    }

    @Nullable
    @Override
    public GameClient getOwner() {
        return (GameClient) super.getOwner();
    }
}
