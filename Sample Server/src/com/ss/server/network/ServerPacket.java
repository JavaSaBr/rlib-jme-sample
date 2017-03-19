package com.ss.server.network;

import com.ss.server.network.model.GameClient;
import org.jetbrains.annotations.NotNull;
import rlib.geom.Quaternion4f;
import rlib.geom.Vector3f;
import rlib.network.packet.impl.AbstractReusableSendablePacket;
import rlib.util.pools.ReusablePool;

import java.nio.ByteBuffer;

/**
 * The base implementation of the server packet.
 *
 * @author JavaSaBr
 */
public abstract class ServerPacket extends AbstractReusableSendablePacket<GameClient> {

    /**
     * The type of the packet.
     */
    @NotNull
    private final ServerPacketType type;

    public ServerPacket() {
        this.type = getPacketType();
    }

    /**
     * @return the type of the packet.
     */
    @NotNull
    public abstract ServerPacketType getPacketType();

    /**
     * @return the pool to store used packet.
     */
    @NotNull
    private ReusablePool<AbstractReusableSendablePacket<GameClient>> getPool() {
        if (pool != null) return pool;
        return type.getPool();
    }

    @Override
    protected void writeImpl(@NotNull final ByteBuffer buffer) {
        writePacketTypeId(buffer);
    }

    protected final void writePacketTypeId(@NotNull final ByteBuffer buffer) {
        writeShort(buffer, type.getId());
    }

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
}
