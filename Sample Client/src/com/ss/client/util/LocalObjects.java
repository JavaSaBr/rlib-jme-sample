package com.ss.client.util;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.ss.client.GameThread;
import org.jetbrains.annotations.NotNull;
import rlib.util.CycleBuffer;

/**
 * The thread local container of local objects.
 *
 * @author JavaSaBr
 */
public class LocalObjects {

    private static final int SIZE = 20;

    @NotNull
    public static LocalObjects get() {
        return ((GameThread) Thread.currentThread()).getLocalObects();
    }

    /**
     * The vectors buffer.
     */
    @NotNull
    private final CycleBuffer<Vector3f> vectorBuffer;

    /**
     * The quaternions buffer.
     */
    @NotNull
    private final CycleBuffer<Quaternion> rotationBuffer;

    /**
     * The rays buffer.
     */
    @NotNull
    private final CycleBuffer<Ray> rayBuffer;

    /**
     * The matrixes buffer.
     */
    @NotNull
    private final CycleBuffer<Matrix3f> matrix3fBuffer;

    /**
     * The matrix float arrays buffer.
     */
    @NotNull
    private final CycleBuffer<float[]> matrixFloatBuffer;

    @SuppressWarnings("unchecked")
    public LocalObjects() {
        this.vectorBuffer = new CycleBuffer<>(Vector3f.class, SIZE, Vector3f::new);
        this.rotationBuffer = new CycleBuffer<>(Quaternion.class, SIZE, Quaternion::new);
        this.rayBuffer = new CycleBuffer<>(Ray.class, SIZE, Ray::new);
        this.matrix3fBuffer = new CycleBuffer<>(Matrix3f.class, SIZE, Matrix3f::new);
        this.matrixFloatBuffer = new CycleBuffer<>(float[].class, SIZE, () -> new float[16]);
    }

    /**
     * @return the next free matrix.
     */
    @NotNull
    public Matrix3f nextMatrix3f() {
        return matrix3fBuffer.next();
    }

    /**
     * @return the next free matrix array.
     */
    @NotNull
    public float[] nextFloatMatrix16F() {
        return matrixFloatBuffer.next();
    }

    /**
     * @return the next free ray.
     */
    @NotNull
    public Ray nextRay() {
        return rayBuffer.next();
    }

    /**
     * @return the next free quaternion.
     */
    @NotNull
    public Quaternion nextRotation() {
        return rotationBuffer.next();
    }

    /**
     * @return the next free vector.
     */
    @NotNull
    public Vector3f nextVector() {
        return vectorBuffer.next();
    }
}
