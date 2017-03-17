package com.ss.client.network.model;

import com.ss.client.network.ClientPacket;
import com.ss.client.network.ServerPacket;
import com.ss.client.network.ServerPacketType;
import org.jetbrains.annotations.NotNull;
import rlib.network.client.ClientNetwork;
import rlib.network.client.server.impl.AbstractServerConnection;
import rlib.network.packet.impl.AbstractReusableSendablePacket;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Модель подключения к серверу.
 *
 * @author Ronn
 */
public class NetConnection extends AbstractServerConnection<NetServer, ServerPacket, ClientPacket> {

    public static final int READ_PACKET_LIMIT = 1000000;
    public static final int WAIT_SEGMENT_LIMIT = 100000;

    public static final int SIZE_BYTES_SIZE = 2;

    /**
     * Буффер для хранения кусков пакета.
     */
    private final ByteBuffer waitBuffer;

    /**
     * Кол-во ожидающих кусков пакета.
     */
    private final AtomicInteger waitCount;

    public NetConnection(final ClientNetwork network, final AsynchronousSocketChannel channel,
                         final Class<ClientPacket> sendableType) {
        super(network, channel, sendableType);

        this.waitBuffer = network.takeReadBuffer();
        this.waitCount = new AtomicInteger();
    }

    @Override
    protected void clearWaitPackets() {
        getWaitPackets().forEach(AbstractReusableSendablePacket::complete);
        super.clearWaitPackets();
    }

    @Override
    public void close() {
        super.close();

        final ClientNetwork network = getNetwork();
        network.putReadBuffer(getWaitBuffer());
    }

    /**
     * Получение пакета из буфера.
     */
    private ServerPacket getPacket(final ByteBuffer buffer, final NetServer server) {

        if (buffer.remaining() < SIZE_BYTES_SIZE) {
            return null;
        }

        return ServerPacketType.getPacketForOpcode(buffer.getShort() & 0xFFFF);
    }

    /**
     * Получение размера следующего пакета в буфере.
     *
     * @param buffer буфер с данными пакетов.
     * @return размер пакета.
     */
    private final int getPacketSize(final ByteBuffer buffer) {
        return buffer.getShort() & 0xFFFF;
    }

    /**
     * @return буффер для хранения кусков пакета.
     */
    private ByteBuffer getWaitBuffer() {
        return waitBuffer;
    }

    /**
     * @return кол-во ожидающих кусков пакета.
     */
    private AtomicInteger getWaitCount() {
        return waitCount;
    }

    @NotNull
    @Override
    protected ByteBuffer writePacketToBuffer(@NotNull final ClientPacket packet, @NotNull final ByteBuffer buffer) {
        final NetServer server = getServer();

        buffer.clear();
        packet.prepareWritePosition(buffer);
        packet.write(buffer);
        buffer.flip();

        packet.writeHeader(buffer, buffer.limit());

        server.encrypt(buffer, SIZE_BYTES_SIZE, buffer.limit() - SIZE_BYTES_SIZE);

        return buffer;
    }

    @Override
    protected void completed(@NotNull final ClientPacket packet) {
        super.completed(packet);
        packet.complete();
    }

    @Override
    protected void readPacket(final ByteBuffer buffer) {

        final NetServer server = getServer();

        final ByteBuffer waitBuffer = getWaitBuffer();
        final AtomicInteger waitCount = getWaitCount();

        int currentCount = waitCount.get();

        if (currentCount > WAIT_SEGMENT_LIMIT) {
            waitBuffer.clear();
            LOGGER.error(this, "crowded limit segments.");
        }

        // если есть кусок пакета ожидающего
        if (waitBuffer.position() > 0) {

            // вливаем весь новый буффер
            waitBuffer.put(buffer.array(), buffer.position(), buffer.limit() - buffer.position());
            waitBuffer.flip();

            // очищаем пришедший буффер и вливаем в него новый итоговый буффер
            buffer.clear();
            buffer.put(waitBuffer.array(), 0, waitBuffer.limit());
            buffer.flip();

            // очищаем ожидающий буффер
            waitBuffer.clear();
        }

        for (int i = 0, limit = 0, size = 0; buffer.remaining() >= SIZE_BYTES_SIZE && i < READ_PACKET_LIMIT; i++) {

            size = getPacketSize(buffer);

            limit += size;

            // если пакет не вместился в этот буффер, складываем его в ожидающий
            // и выходим из цикла
            if (limit > buffer.limit()) {

                final int offset = buffer.position() - SIZE_BYTES_SIZE;
                final int length = buffer.limit() - offset;

                waitBuffer.put(buffer.array(), offset, length);
                waitCount.incrementAndGet();
                return;
            }

            server.decrypt(buffer, buffer.position(), size - SIZE_BYTES_SIZE);

            final ServerPacket packet = getPacket(buffer, server);

            if (currentCount > 0) {
                waitCount.getAndSet(0);
                currentCount = 0;
            }

            if (packet != null) {
                server.readPacket(packet, buffer);
            }

            buffer.position(limit);
        }
    }
}
