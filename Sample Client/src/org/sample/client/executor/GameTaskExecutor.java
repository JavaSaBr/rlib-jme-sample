package org.sample.client.executor;

import org.jetbrains.annotations.NotNull;
import org.sample.client.game.task.GameTask;

/**
 * The interface to implement an executor of game tasks.
 *
 * @author JavaSaBr
 */
public interface GameTaskExecutor {

    /**
     * Add a new game task to execute.
     *
     * @param gameTask the new game task.
     */
    void execute(@NotNull GameTask gameTask);
}
