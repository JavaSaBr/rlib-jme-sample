package org.sample.client.executor;

import org.sample.client.game.task.GameTask;

/**
 * Интерфейс для реализация исполнителей игровых задач.
 *
 * @author Ronn
 */
public interface GameTaskExecutor {

    /**
     * Добавление на исполнение игровой задачи.
     *
     * @param gameTask игровая задача.
     */
    public void execute(GameTask gameTask);
}
