package com.ss.server.model;

import com.ss.server.LocalObjects;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a spawnable object.
 *
 * @author JavaSaBr
 */
public interface SpawnableObject extends GameObject {

    /**
     * Spawn this object to the world.
     *
     * @param local the local objects.
     */
    default void spawnMe(@NotNull final LocalObjects local) {
    }

    /**
     * Unspawn this object from the world.
     *
     * @param local the local objects.
     */
    default void unspawnMe(@NotNull final LocalObjects local) {
    }

    /**
     * Delete this object from the world and the server.
     *
     * @param local the local objects.
     */
    default void deleteMe(@NotNull final LocalObjects local) {
    }

    /**
     * @return true if this object was spawned.
     */
    default boolean isSpawned() {
        return false;
    }
}
