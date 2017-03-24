package com.ss.server;

import com.ss.server.model.GameObject;
import org.jetbrains.annotations.NotNull;
import rlib.geom.Quaternion4f;
import rlib.geom.Vector3f;
import rlib.geom.Vector3fBuffer;
import rlib.util.CycleBuffer;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.random.Random;
import rlib.util.random.RandomFactory;

import java.util.Collection;

/**
 * The container of thread local objects.
 *
 * @author JavaSaBr
 */
public final class LocalObjects implements Vector3fBuffer {

    private static final int DEFAULT_BUFFER_SIZE = 80;

    private static final class Vector3fBufferImpl implements Vector3fBuffer {

        @NotNull
        private final CycleBuffer<Vector3f> buffer;

        private Vector3fBufferImpl() {
            buffer = new CycleBuffer<>(Vector3f.class, DEFAULT_BUFFER_SIZE, Vector3f::newInstance);
        }

        @NotNull
        @Override
        public Vector3f nextVector() {
            return buffer.next();
        }
    }

    @NotNull
    public static LocalObjects get() {
        return ((ServerThread) Thread.currentThread()).getLocal();
    }

    /**
     * The thread.
     */
    @NotNull
    private final Thread thread;

    /**
     * The random.
     */
    @NotNull
    private final Random random;

    /**
     * The vectors buffer.
     */
    @NotNull
    private final CycleBuffer<Vector3f> vectorsBuffer;

    /**
     * The quaternions buffer.
     */
    @NotNull
    private final CycleBuffer<Quaternion4f> quaternionsBuffer;

    /**
     * The vector buffers buffer.
     */
    @NotNull
    private final CycleBuffer<Vector3fBuffer> vectorBuffersBuffer;

    /**
     * The vector lists buffer.
     */
    @NotNull
    private final CycleBuffer<Array<Vector3f>> vectorListsBuffer;

    /**
     * The object lists buffer.
     */
    @NotNull
    private final CycleBuffer<Array<GameObject>> objectListsBuffer;

    public LocalObjects(@NotNull final Thread thread) {
        this.thread = thread;
        this.random = RandomFactory.newFastRandom();
        this.vectorsBuffer = new CycleBuffer<>(Vector3f.class, DEFAULT_BUFFER_SIZE, Vector3f::newInstance);
        this.quaternionsBuffer = new CycleBuffer<>(Quaternion4f.class, DEFAULT_BUFFER_SIZE, Quaternion4f::newInstance);
        this.vectorBuffersBuffer = new CycleBuffer<>(Vector3fBuffer.class, DEFAULT_BUFFER_SIZE, Vector3fBufferImpl::new);
        this.vectorListsBuffer = new CycleBuffer<>(Array.class, DEFAULT_BUFFER_SIZE,
                () -> ArrayFactory.newArray(Vector3f.class), Collection::clear);
        this.objectListsBuffer = new CycleBuffer<>(Array.class, DEFAULT_BUFFER_SIZE,
                () -> ArrayFactory.newArray(GameObject.class), Collection::clear);
    }

    /**
     * @return next free quaternion.
     */
    @NotNull
    public Quaternion4f nextQuaternion() {
        return quaternionsBuffer.next();
    }

    @NotNull
    @Override
    public Vector3f nextVector() {
        return vectorsBuffer.next();
    }

    /**
     * @return the next free vector buffer.
     */
    @NotNull
    public Vector3fBuffer nextVectorBuffer() {
        return vectorBuffersBuffer.next();
    }

    /**
     * @return the next free vectors array.
     */
    @NotNull
    public Array<Vector3f> nextVectorArray() {
        return vectorListsBuffer.next();
    }

    /**
     * @return the next free objects array.
     */
    @NotNull
    public Array<GameObject> nextObjectArray() {
        return objectListsBuffer.next();
    }

    /**
     * @return the local random.
     */
    @NotNull
    public Random getRandom() {
        return random;
    }

    /**
     * @return the thread.
     */
    @NotNull
    public Thread getThread() {
        return thread;
    }
}
