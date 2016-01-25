package org.sample.client.game.task;

import org.sample.client.util.LocalObjects;

/**
 * Задача связанная с компонентами игры.
 *
 * @author Ronn
 */
@FunctionalInterface
public interface GameTask extends Comparable<GameTask> {

    public static final int MIN_PRIORITY = 0;
    public static final int MAX_PRIORITY = 10;
    public static final int NORMAL_PRIORITY = 5;

    @Override
    public default int compareTo(final GameTask task) {
        return task.getPriority() - getPriority();
    }

    /**
     * @return завершено ли выполение задачи.
     */
    public boolean execute(LocalObjects local, long currentTime);

    /**
     * @return приоритет задачи.
     */
    public default int getPriority() {
        return MIN_PRIORITY;
    }

    /**
     * Идет сравнения задачи с проверяемым объектом.
     *
     * @param object сравнивый объект.
     * @return соответствует ли задача объекту.
     */
    public default boolean isMatches(final Object object) {
        return false;
    }
}
