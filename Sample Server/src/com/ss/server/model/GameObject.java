package com.ss.server.model;

import org.jetbrains.annotations.NotNull;
import rlib.geom.Quaternion4f;
import rlib.geom.Vector3f;

/**
 * The interface to implement a game object.
 *
 * @author JavaSaBr
 */
public interface GameObject {

    /**
     * @return the uniq id.
     */
    long getObjectId();

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
    Quaternion4f getRotation();

    /**
     * @param rotation the rotation.
     */
    void setRotation(@NotNull Quaternion4f rotation);
}
