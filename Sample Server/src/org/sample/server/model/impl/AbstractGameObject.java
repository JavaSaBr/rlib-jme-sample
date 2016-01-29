package org.sample.server.model.impl;

import org.sample.server.model.GameObject;

import rlib.geom.Rotation;
import rlib.geom.Vector;

/**
 * Базовая реализация игрового объекта.
 *
 *@author Ronn
 */
public abstract class AbstractGameObject implements GameObject {

    /**
     * Положение объекта в пространстве.
     */
    private final Vector location;

    /**
     * Разворот объекта в пространстве.
     */
    private final Rotation rotation;

    /**
     * Уникальный ид объекта.
     */
    private final int objectId;

    public AbstractGameObject(int objectId) {
        this.objectId = objectId;
        this.location = Vector.newInstance();
        this.rotation = Rotation.newInstance();
    }

    @Override
    public int getObjectId() {
        return objectId;
    }

    @Override
    public Vector getLocation() {
        return location;
    }

    @Override
    public void setLocation(Vector location) {
        this.location.set(location);
    }

    @Override
    public Rotation getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(Rotation rotation) {
        this.rotation.set(rotation);
    }
}
