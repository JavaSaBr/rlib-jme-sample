package com.ss.server.model.impl;

import com.ss.server.model.GameObject;
import org.jetbrains.annotations.NotNull;
import rlib.geom.Quaternion4f;
import rlib.geom.Vector3f;

/**
 * The base implementation of a game object.
 *
 * @author JavaSaBr
 */
public abstract class AbstractGameObject implements GameObject {

    /**
     * The object location.
     */
    @NotNull
    private final Vector3f location;

    /**
     * The object rotation.
     */
    @NotNull
    private final Quaternion4f rotation;

    /**
     * The uniq id.
     */
    private final long objectId;

    public AbstractGameObject(final long objectId) {
        this.objectId = objectId;
        this.location = Vector3f.newInstance();
        this.rotation = Quaternion4f.newInstance();
    }

    @Override
    public long getObjectId() {
        return objectId;
    }

    @NotNull
    @Override
    public Vector3f getLocation() {
        return location;
    }

    @Override
    public void setLocation(@NotNull final Vector3f location) {
        this.location.set(location);
    }

    @NotNull
    @Override
    public Quaternion4f getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(@NotNull final Quaternion4f rotation) {
        this.rotation.set(rotation);
    }
}
