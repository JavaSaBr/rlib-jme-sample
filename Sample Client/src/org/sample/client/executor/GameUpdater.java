package org.sample.client.executor;

import org.jetbrains.annotations.NotNull;

/**
 * The executor of game objects.
 *
 * @author JavaSaBr
 */
public interface GameExecutor<T> {

    /**
     * Add a new object to execute.
     *
     * @param object the new object.
     */
    void execute(@NotNull T object);

    /**
     * Remove an object from executing.
     *
     * @param object the object to remove.
     */
    void remove(@NotNull T object);
}
