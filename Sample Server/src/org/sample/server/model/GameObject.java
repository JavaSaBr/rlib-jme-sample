package org.sample.server.model;

import rlib.geom.Rotation;
import rlib.geom.Vector;

/**
 * Интерфейс для реализации игрового объекта.
 *
 * @author Ronn
 */
public interface GameObject {

    /**
     * @return уникальный ид объекта.
     */
    public int getObjectId();

    /**
     * @return ид класса объектов.
     */
    public int getClassId();

    /**
     * @return положение в пространстве.
     */
    public Vector getLocation();

    /**
     * @param location новое положение в пространстве.
     */
    public void setLocation(Vector location);

    /**
     * @return разворот в пространстве.
     */
    public Rotation getRotation();

    /**
     * @param rotation новый разворот в пространстве.
     */
    public void setRotation(Rotation rotation);
}
