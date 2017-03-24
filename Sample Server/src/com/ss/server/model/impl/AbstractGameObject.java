package com.ss.server.model.impl;

import com.ss.server.model.GameObject;
import com.ss.server.model.World;
import com.ss.server.template.ObjectTemplate;
import org.jetbrains.annotations.NotNull;
import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.concurrent.lock.LockFactory;
import rlib.geom.Quaternion4f;
import rlib.geom.Vector3f;
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
    protected final Quaternion4f rotation;

    /**
     * The template.
     */
    @NotNull
    protected final T template;

    /**
     * The uniq id.
     */
    protected volatile int objectId;

    /**
     * @param template the template.
     */
    public AbstractGameObject(@NotNull final T template) {
        this.template = template;
        this.location = Vector3f.newInstance();
        this.rotation = Quaternion4f.newInstance();
        this.lock = LockFactory.newAtomicARSWLock();
    }

    @Override
    @NotNull
    public ObjectTemplate getTemplate() {
        return template;
    }

    @Override
    public int getObjectId() {
        return objectId;
    }

    @Override
    public void setObjectId(final int objectId) {
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
    public Quaternion4f getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(@NotNull final Quaternion4f rotation) {
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
