package com.ss.client.model;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.ss.client.template.ObjectTemplate;
import org.jetbrains.annotations.NotNull;
import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.util.pools.Reusable;

/**
 * The interface to implement a game object.
 *
 * @author JavaSaBr
 */
public interface GameObject extends Reusable {

    /**
     * @return the uniq id.
     */
    long getObjectId();

    /**
     * Set a new uniq id.
     *
     * @param objectId the uniq id.
     */
    void setObjectId(long objectId);

    /**
     * @return the position.
     */
    @NotNull
    Vector3f getLocation();

    /**
     * @param location the position.
     */
    void setLocation(@NotNull Vector3f location);

    /**
     * @return the rotation.
     */
    @NotNull
    Quaternion getRotation();

    /**
     * @param rotation the rotation.
     */
    void setRotation(@NotNull Quaternion rotation);

    /**
     * @return the template.
     */
    @NotNull
    ObjectTemplate getTemplate();

    /**
     * The lock.
     *
     * @return the lock.
     */
    @NotNull
    AsyncReadSyncWriteLock getLock();
}
