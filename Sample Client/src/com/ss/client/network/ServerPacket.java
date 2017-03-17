package com.ss.client.network;

import com.ss.client.network.model.NetServer;
import rlib.geom.Vector3f;
import rlib.network.packet.impl.AbstractRunnableReadablePacket;
import rlib.util.ClassUtils;
import rlib.util.pools.ReusablePool;

/**
 * Модель серверного пакета, присылаемого сервером.
 *
 * @author Ronn
 */
public abstract class ServerPacket extends AbstractRunnableReadablePacket<NetServer> {

    /**
     * Типа пакета.
     */
    private ServerPacketType type;

    @Override
    protected final ReusablePool<ServerPacket> getPool() {
        return type.getPool();
    }

    @Override
    public boolean isSynchronized() {
        return false;
    }

    /**
     * @return ожидвающий ли пакет.
     */
    public boolean isWaiteable() {
        return false;
    }

    public final ServerPacket newInstance() {

        final ReusablePool<ServerPacket> pool = getPool();
        ServerPacket packet = pool.take();

        if (packet == null) {
            packet = ClassUtils.newInstance(getClass());
            packet.type = type;
        }

        return packet;
    }

    /**
     * Чтение 8х байтов в виде double из буфера.
     */
    protected final double readDouble() {
        return buffer.getDouble();
    }

    @Override
    protected void readImpl() {
    }

    /**
     * Чтение вектора из 3х float.
     */
    protected final Vector3f readVector() {
        return Vector3f.newInstance(readFloat(), readFloat(), readFloat());
    }

    /**
     * @param type тип пакета.
     */
    public final void setPacketType(final ServerPacketType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
