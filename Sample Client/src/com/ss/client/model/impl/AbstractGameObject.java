package com.ss.client.model.impl;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.ss.client.model.GameObject;
import com.ss.client.model.World;
import com.ss.client.template.ObjectTemplate;
import org.jetbrains.annotations.NotNull;
import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.concurrent.lock.LockFactory;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * The base implementation of a game object.
 *
 * @author JavaSaBr
 */
public abstract class AbstractGameObject<T extends ObjectTemplate> implements GameObject {

    @NotNull
    protected static final Logger LOGGER = LoggerManager.getLogger(GameObject.class);

    /**
     * The world.
     */
    @NotNull
    protected static final World WORLD = World.getInstance();

    /**
     * The lock.
     */
    @NotNull
    protected final AsyncReadSyncWriteLock lock;

    /**
     * The object location.
     */
    @NotNull
    protected final Vector3f location;

    /**
     * The object rotation.
     */
    @NotNull
    protected final Quaternion rotation;

    /**
     * The template.
     */
    @NotNull
    protected final T template;

    /**
     * The uniq id.
     */
    protected volatile long objectId;

    /**
     * @param template the template.
     */
    public AbstractGameObject(@NotNull final T template) {
        this.template = template;
        this.location = new Vector3f();
        this.rotation = new Quaternion();
        this.lock = LockFactory.newAtomicARSWLock();
    }

    @Override
    @NotNull
    public ObjectTemplate getTemplate() {
        return template;
    }

    @Override
    public long getObjectId() {
        return objectId;
    }

    @Override
    public void setObjectId(final long objectId) {
        this.objectId = objectId;
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
    public Quaternion getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(@NotNull final Quaternion rotation) {
        this.rotation.set(rotation);
    }

    @Override
    public void free() {
        setObjectId(0);
    }

    @Override
    @NotNull
    public AsyncReadSyncWriteLock getLock() {
        return lock;
    }
}
