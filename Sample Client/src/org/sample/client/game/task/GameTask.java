package org.sample.client.game.task;

import org.jetbrains.annotations.NotNull;
import org.sample.client.util.LocalObjects;

/**
 * The interface to implement game tasks.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface GameTask extends Comparable<GameTask> {

    int MIN_PRIORITY = 0;
    int MAX_PRIORITY = 10;
    int NORMAL_PRIORITY = 5;

    @Override
    default int compareTo(@NotNull final GameTask task) {
        return task.getPriority() - getPriority();
    }

    /**
     * Execute this task.
     *
     * @return true if need to stop executing this task.
     */
    boolean execute(@NotNull LocalObjects local, long currentTime);

    /**
     * @return приоритет задачи.
     */
    default int getPriority() {
        return MIN_PRIORITY;
    }
}
