package org.sample.client.executor;

import org.jetbrains.annotations.NotNull;

/**
 * The executor of game objects.
 *
 * @author JavaSaBr
 */
public interface GameUpdater<T> {

    /**
     * Add a new object to updating.
     *
     * @param object the new object.
     */
    void addToUpdating(@NotNull T object);

    /**
     * Remove an object from updating.
     *
     * @param object the object to remove.
     */
    void removeFromUpdating(@NotNull T object);
}
