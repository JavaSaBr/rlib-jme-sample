package org.sample.server.network;

import org.sample.server.network.model.GameClient;

import java.nio.ByteBuffer;

import rlib.geom.Rotation;
import rlib.geom.Vector;
import rlib.graphics.color.ColorRGBA;
import rlib.network.packet.impl.AbstractReusableSendablePacket;
import rlib.util.ClassUtils;
import rlib.util.pools.FoldablePool;

/**
 * Базовая реализация серверного пакета.
 *
 * @author Ronn
 */
public abstract class ServerPacket extends AbstractReusableSendablePacket<GameClient> {

    /**
     * Тип пакета.
     */
    private final ServerPacketType type;

    /**
     * Переопределнный пул для складывания этого пакета.
     */
    private FoldablePool<ServerPacket> pool;

    public ServerPacket() {
        this.type = getPacketType();
    }

    @Override
    protected final void completeImpl() {
        getPool().put(this);
    }

    /**
     * Принудительное завершение пакета.
     */
    public void forceComplete() {
        counter.set(1);
        complete();
    }

    /**
     * @return тип пакета.
     */
    public abstract ServerPacketType getPacketType();

    /**
     * @return получение пула для соотвествующего пакета.
     */
    protected final FoldablePool<ServerPacket> getPool() {

        if (pool != null) {
            return pool;
        }

        return type.getPool();
    }

    /**
     * @param pool переопределнный пул для складывания этого пакета.
     */
    public final void setPool(final FoldablePool<ServerPacket> pool) {
        this.pool = pool;
    }

    /**
     * @return новый экземпляр пакета.
     */
    @SuppressWarnings("unchecked")
    public final <T extends ServerPacket> T newInstance() {

        final FoldablePool<ServerPacket> pool = getPool();

        ServerPacket packet = pool.take();

        if (packet == null) {
            packet = ClassUtils.newInstance(getClass());
        }

        return (T) packet;
    }

    /**
     * Запись временного буффера в указанный.
     *
     * @param buffer записываемый буффер.
     * @param temp   временный буффер.
     */
    protected void writeBuffer(final ByteBuffer buffer, final ByteBuffer temp) {
        buffer.put(temp.array(), temp.position(), temp.limit());
    }

    @Override
    public final void writeByte(final ByteBuffer buffer, final int value) {
        super.writeByte(buffer, value);
    }

    @Override
    public final void writeChar(final ByteBuffer buffer, final char value) {
        super.writeChar(buffer, value);
    }

    @Override
    public final void writeChar(final ByteBuffer buffer, final int value) {
        super.writeChar(buffer, value);
    }

    public final void writeColor(final ByteBuffer buffer, final ColorRGBA color) {
        writeFloat(buffer, color.getRed());
        writeFloat(buffer, color.getGreen());
        writeFloat(buffer, color.getBlue());
    }

    public final void writeDouble(final ByteBuffer buffer, final double value) {
        buffer.putDouble(value);
    }

    @Override
    public final void writeFloat(final ByteBuffer buffer, final float value) {
        super.writeFloat(buffer, value);
    }

    @Override
    public final void writeHeader(final ByteBuffer buffer, final int length) {
        buffer.putShort(0, (short) length);
    }

    @Override
    protected void writeImpl(final ByteBuffer buffer) {
        writeOpcode(buffer);
    }

    @Override
    public final void writeInt(final ByteBuffer buffer, final int value) {
        super.writeInt(buffer, value);
    }

    @Override
    public final void writeLong(final ByteBuffer buffer, final long value) {
        super.writeLong(buffer, value);
    }

    public final void writeOpcode(final ByteBuffer buffer) {
        writeShort(buffer, type.getOpcode());
    }

    @Override
    public final void writePosition(final ByteBuffer buffer) {
        buffer.position(2);
    }

    /**
     * Записать вектор.
     */
    public final void writeRotation(final ByteBuffer buffer, final Rotation rotation) {
        writeFloat(buffer, rotation.getX());
        writeFloat(buffer, rotation.getY());
        writeFloat(buffer, rotation.getZ());
        writeFloat(buffer, rotation.getW());
    }

    @Override
    public final void writeShort(final ByteBuffer buffer, final int value) {
        super.writeShort(buffer, value);
    }

    @Override
    public final void writeString(final ByteBuffer buffer, final String string) {
        super.writeString(buffer, string);
    }

    /**
     * Записать вектор.
     */
    public final void writeVector(final ByteBuffer buffer, final Vector vector) {
        writeFloat(buffer, vector.getX());
        writeFloat(buffer, vector.getY());
        writeFloat(buffer, vector.getZ());
    }
}
