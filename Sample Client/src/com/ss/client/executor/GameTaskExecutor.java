package com.ss.client.executor;

import com.ss.client.game.task.GameTask;
import org.jetbrains.annotations.NotNull;
import com.ss.client.game.task.GameTask;

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
