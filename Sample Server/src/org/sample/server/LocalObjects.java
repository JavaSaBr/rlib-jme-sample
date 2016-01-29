package org.sample.server;

import org.sample.server.network.ServerPacket;
import org.sample.server.network.ServerPacketFactory;
import org.sample.server.util.FastVectorBuffer;
import rlib.geom.Rotation;
import rlib.geom.Vector;
import rlib.geom.VectorBuffer;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.random.Random;
import rlib.util.random.RandomFactory;

/**
 * Контейнер локальных объектов для серверного потока.
 *
 * @author Ronn
 */
public final class LocalObjects implements VectorBuffer {

    private static final int DEFAULT_BUFFER_SIZE = 80;

    public static final LocalObjects get() {
        return ((ServerThread) Thread.currentThread()).getLocal();
    }

    /**
     * Фабрика серверных пакетов
     */
    private final ServerPacketFactory packetFactory;

    /**
     * Пток, за которым закреплен контейнер
     */
    private final Thread thread;

    /**
     * Локальный рандоминайзер.
     */
    private final Random random;

    /**
     * Набор дополнительных векторов.
     */
    private final Vector[] vectors;

    /**
     * Набор дополнительных разворотов.
     */
    private final Rotation[] rotations;

    /**
     * Массив векторных буферов.
     */
    private final VectorBuffer[] vectorBuffers;

    /**
     * Набор списков векторов.
     */
    private final Array<Vector>[] vectorLists;

    /**
     * Лимит буфера.
     */
    private final int limit;

    /**
     * Номер след. вектора.
     */
    private int vectorOrder;

    /**
     * Номер след. разворота.
     */
    private int rotationOrder;

    /**
     * Индекс следующего свободного списка векторов.
     */
    private int vectorListOrder;

    /**
     * Индекс следующего векторного буфера.
     */
    private int vectorBufferOrder;

    @SuppressWarnings("unchecked")
    public LocalObjects(final Thread thread) {
        this.thread = thread;
        this.limit = DEFAULT_BUFFER_SIZE - 2;
        this.random = RandomFactory.newRealRandom();
        this.packetFactory = new ServerPacketFactory();

        this.vectors = new Vector[DEFAULT_BUFFER_SIZE];
        this.rotations = new Rotation[DEFAULT_BUFFER_SIZE];
        this.vectorLists = new Array[DEFAULT_BUFFER_SIZE];
        this.vectorBuffers = new VectorBuffer[DEFAULT_BUFFER_SIZE];

        for (int i = 0, length = vectors.length; i < length; i++) {
            vectors[i] = Vector.newInstance();
            rotations[i] = Rotation.newInstance();
            vectorLists[i] = ArrayFactory.newArray(Vector.class);
            vectorBuffers[i] = new FastVectorBuffer();
        }
    }

    /**
     * Создание нового серверного пакета соотвествующего класса указанному.
     *
     * @param example пример создаваемого пакета.
     * @return новый пакет соотвествующего класса.
     */
    public <R extends ServerPacket> R create(final ServerPacket example) {
        return packetFactory.create(example);
    }

    /**
     * @return след. свободный вспомогательный класс.
     */
    public Rotation getNextRotation() {

        if (rotationOrder > limit) {
            rotationOrder = 0;
        }

        return rotations[rotationOrder++];
    }

    /**
     * @return след. свободный вспомогательный класс.
     */
    @Override
    public Vector getNextVector() {

        if (vectorOrder > limit) {
            vectorOrder = 0;
        }

        return vectors[vectorOrder++];
    }

    /**
     * @return следующий свободный буффер.
     */
    public VectorBuffer getNextVectorBuffer() {

        if (vectorBufferOrder > limit) {
            vectorBufferOrder = 0;
        }

        return vectorBuffers[vectorBufferOrder++];
    }

    /**
     * @return след. свободный список векторов.
     */
    public Array<Vector> getNextVectorList() {

        if (vectorListOrder > limit) {
            vectorListOrder = 0;
        }

        return vectorLists[vectorListOrder++].clear();
    }

    /**
     * @return локальный рандоминайзер.
     */
    public Random getRandom() {
        return random;
    }

    /**
     * @return поток, за которым закреплен контейнер.
     */
    public Thread getThread() {
        return thread;
    }
}
