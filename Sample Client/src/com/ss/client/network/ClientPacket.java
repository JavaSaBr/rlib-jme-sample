package com.ss.client.network;

import com.jme3.math.Vector3f;
import com.ss.client.network.model.NetServer;
import org.jetbrains.annotations.NotNull;
import rlib.network.packet.impl.AbstractReusableSendablePacket;
import rlib.util.ClassUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.ReusablePool;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;

/**
 * Базовая модель отправляемого пакета клиентом серверу.
 *
 * @author Ronn
 */
public abstract class ClientPacket extends AbstractReusableSendablePacket<NetServer> {

    private static final Array<ClientPacket> DEBUG = ArrayFactory.newArray(ClientPacket.class);

    /**
     * Тип пакета.
     */
    private final ClientPacketType type;

    /**
     * Получаем конструктор пакета.
     */
    private final Constructor<? extends ClientPacket> constructor;

    public ClientPacket() {
        this.type = getPacketType();
        this.constructor = ClassUtils.getConstructor(getClass());
    }

    @Override
    protected void completeImpl() {
        final ReusablePool<ClientPacket> pool = getPool();
        pool.put(this);
    }

    /**
     * @return тип пакета.
     */
    public abstract ClientPacketType getPacketType();

    /**
     * @return получение пула для соотвествующего пакета.
     */
    protected ReusablePool<ClientPacket> getPool() {
        return type.getPool();
    }

    public boolean isSynchronized() {
        return false;
    }

    /**
     * @return новый экземпляр пакета.
     */
    @SuppressWarnings("unchecked")
    public final <T extends ClientPacket> T newInstance() {

        final ReusablePool<ClientPacket> pool = getPool();

        ClientPacket packet = pool.take();

        if (packet == null) {
            packet = ClassUtils.newInstance(constructor);
        }

        return (T) packet;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    /**
     * Запись буффера в буффер.
     */
    protected void writeBuffer(final ByteBuffer buffer, final ByteBuffer other) {
        buffer.put(other.array(), 0, other.limit());
    }

    @Override
    public final void writeHeader(final ByteBuffer buffer, final int length) {
        buffer.putShort(0, (short) length);
    }

    /**
     * Запись опкода пакета.
     */
    protected final void writeOpcode(final ByteBuffer buffer) {
        writeShort(buffer, type.getOpcode());
    }

    @Override
    public void prepareWritePosition(@NotNull final ByteBuffer buffer) {
        buffer.position(2);
    }

    /**
     * Запись вектора в буффер.
     */
    protected void writeVector(final ByteBuffer buffer, final rlib.geom.Vector3f vector) {
        writeFloat(buffer, vector.getX());
        writeFloat(buffer, vector.getY());
        writeFloat(buffer, vector.getZ());
    }

    /**
     * Запись вектора в буффер.
     */
    protected void writeVector(final ByteBuffer buffer, final Vector3f vector) {
        writeFloat(buffer, vector.getX());
        writeFloat(buffer, vector.getY());
        writeFloat(buffer, vector.getZ());
    }
}
