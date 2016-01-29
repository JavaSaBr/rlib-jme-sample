package org.sample.server.util;

import rlib.geom.Vector;
import rlib.geom.VectorBuffer;

/**
 * Отдельная реализация буфера векторов.
 *
 * @author Ronn
 */
public class FastVectorBuffer implements VectorBuffer {

    /**
     * Массив буферных векторов.
     */
    private final Vector[] vectors;

    /**
     * Индекс след. вектора.
     */
    private int index;

    public FastVectorBuffer() {
        this.vectors = new Vector[20];

        for (int i = 0, length = vectors.length; i < length; i++) {
            vectors[i] = Vector.newInstance();
        }
    }

    @Override
    public Vector getNextVector() {

        if (index == vectors.length) {
            index = 0;
        }

        return vectors[index++];
    }
}
