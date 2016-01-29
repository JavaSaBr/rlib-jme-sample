package org.sample.server.network.model;

import org.sample.server.network.ClientPacket;
import org.sample.server.network.ClientPacketType;
import org.sample.server.network.PacketFactory;
import org.sample.server.network.ServerPacket;
import rlib.network.server.ServerNetwork;
import rlib.network.server.client.impl.AbstractClientConnection;
import rlib.util.linkedlist.LinkedList;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Реализация асинхронного сетевого соединения между клиентом и сервером.
 *
 * @author Ronn
 */
public class GameConnection extends AbstractClientConnection<GameClient, ClientPacket, ServerPacket> {

    public static final int READ_PACKET_LIMIT = 1000;
    public static final int WAIT_SEGMENT_LIMIT = 10;
    public static final int SIZE_BYTES_SIZE = 2;

    private static final Consumer<ServerPacket> FINISH_PACKET_FUNCTION = ServerPacket::forceComplete;

    /**
     * Буффер для хранения кусков пакета.
     */
    private final ByteBuffer waitBuffer;

    /**
     * Кол-во ожидающих кусков пакета.
     */
    private final AtomicInteger waitCount;

    public GameConnection(final ServerNetwork network, final AsynchronousSocketChannel channel, final Class<ServerPacket> sendableType) {
        super(network, channel, sendableType);

        this.waitBuffer = network.getReadByteBuffer();
        this.waitCount = new AtomicInteger();
    }

    @Override
    protected void clearPackets(final LinkedList<ServerPacket> waitPackets) {
        waitPackets.forEach(FINISH_PACKET_FUNCTION);
        super.clearPackets(waitPackets);
    }

    @Override
    public void close() {
        super.close();

        final ServerNetwork network = getNetwork();
        network.putReadByteBuffer(getWaitBuffer());
    }

    /**
     * Получение пакета из буфера.
     */
    private ClientPacket getPacket(final ByteBuffer buffer, final GameClient client) {

        if (buffer.remaining() < SIZE_BYTES_SIZE) {
            return null;
        }

        final ClientPacketType type = ClientPacketType.getPacketType(buffer.getShort() & 0xFFFF);

        if (type == null) {
            return null;
        }

        final PacketFactory<GameClient, ClientPacket, ClientPacketType> factory = client.getPacketFactory();
        return factory.create(type);
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
     * @return адресс клиента.
     */
    public String getRemoteAddress() {

        final AsynchronousSocketChannel channel = getChannel();

        if (channel != null) {
            try {
                return String.valueOf(channel.getRemoteAddress());
            } catch (final IOException e) {
                LOGGER.warning(this, e);
            }
        }

        return "unknown";
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

    @Override
    protected ByteBuffer movePacketToBuffer(final ServerPacket packet, final ByteBuffer buffer) {

        final GameClient client = getClient();

        buffer.clear();

        packet.writePosition(buffer);
        packet.write(buffer);

        buffer.flip();

        packet.writeHeader(buffer, buffer.limit());

        client.encrypt(buffer, SIZE_BYTES_SIZE, buffer.limit() - SIZE_BYTES_SIZE);

        return buffer;
    }

    @Override
    protected void onWrote(final ServerPacket packet) {
        packet.complete();
    }

    @Override
    protected void readPacket(final ByteBuffer buffer) {

        final GameClient client = getClient();

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

            client.decrypt(buffer, buffer.position(), size - SIZE_BYTES_SIZE);

            final ClientPacket packet = getPacket(buffer, client);

            if (currentCount > 0) {
                waitCount.getAndSet(0);
                currentCount = 0;
            }


            if (packet != null) {
                client.readPacket(packet, buffer);
            }

            buffer.position(limit);
        }
    }

    @Override
    public String toString() {
        return "GameConnection{" +
                "waitBuffer=" + waitBuffer +
                ", waitCount=" + waitCount +
                "} " + super.toString();
    }
}
