package com.ss.server.model;

import com.ss.server.LocalObjects;
import com.ss.server.model.player.Player;
import com.ss.server.network.ServerPacket;
import com.ss.server.template.ObjectTemplate;
import org.jetbrains.annotations.NotNull;
import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.geom.Quaternion4f;
import rlib.geom.Vector3f;
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
    int getObjectId();

    /**
     * @return the class id.
     */
    int getClassId();

    /**
     * Set a new uniq id.
     *
     * @param objectId the uniq id.
     */
    void setObjectId(int objectId);

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

    /**
     * @return the template.
     */
    @NotNull
    ObjectTemplate getTemplate();

    /**
     * Send a packet to show this object to the player.
     *
     * @param player the player.
     * @param local  the local objects.
     */
    default void showMeTo(@NotNull final Player player, @NotNull final LocalObjects local) {
    }

    /**
     * Send a packet to hide the object to the player.
     *
     * @param player the player.
     * @param local  the local objects.
     */
    default void hideMeTo(@NotNull final Player player, @NotNull final LocalObjects local) {
    }

    /**
     * Send the packet to this object.
     *
     * @param packet the packet.
     * @param local  the local objects.
     */
    default void sendPacket(@NotNull final ServerPacket packet, @NotNull final LocalObjects local) {
    }

    /**
     * Send the packet to all objects exclude this object.
     *
     * @param packet the packet.
     * @param local  the local objects.
     */
    default void broadcastPacket(@NotNull final ServerPacket packet, @NotNull final LocalObjects local) {
    }

    /**
     * The lock.
     *
     * @return the lock.
     */
    @NotNull
    AsyncReadSyncWriteLock getLock();
}
