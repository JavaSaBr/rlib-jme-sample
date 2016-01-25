package org.sample.client.executor.impl;

import org.sample.client.SampleGame;
import org.sample.client.game.task.GameTask;
import org.sample.client.util.LocalObjects;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.Foldable;

/**
 * Реализация исполнителя задач в основном потооке.
 *
 * @author Ronn
 */
public class GameThreadExecutor {

    private static final Logger LOGGER = LoggerManager.getLogger(GameThreadExecutor.class);

    private static final GameThreadExecutor INSTANCE = new GameThreadExecutor();

    public static GameThreadExecutor getInstance() {
        return INSTANCE;
    }

    /**
     * Ожидающие исполнения задачи.
     */
    private final Array<GameTask> waitTasks;

    /**
     * Задачи которые должны сейчас выполнится.
     */
    private final Array<GameTask> execute;

    public GameThreadExecutor() {
        this.waitTasks = ArrayFactory.newConcurrentAtomicArray(GameTask.class);
        this.execute = ArrayFactory.newArray(GameTask.class);
    }

    /**
     * Добавление задачи на выполнение.
     *
     * @param gameTask задача на выполнение.
     */
    public void addToExecute(final GameTask gameTask) {

        final Array<GameTask> waitTasks = getWaitTasks();
        waitTasks.writeLock();
        try {
            waitTasks.add(gameTask);
        } finally {
            waitTasks.writeUnlock();
        }
    }

    /**
     * Выполнить ожидающие задачи.
     */
    public void execute() {

        final Array<GameTask> waitTasks = getWaitTasks();

        if (waitTasks.isEmpty()) {
            return;
        }

        final Array<GameTask> execute = getExecute();
        try {

            waitTasks.writeLock();
            try {
                execute.addAll(waitTasks);
                waitTasks.clear();
            } finally {
                waitTasks.writeUnlock();
            }

            final LocalObjects local = LocalObjects.get();
            final long currentTime = SampleGame.getCurrentTime();

            for (final GameTask task : execute.array()) {

                if (task == null) {
                    break;
                }

                task.execute(local, currentTime);

                if (task instanceof Foldable) {
                    ((Foldable) task).release();
                }
            }

        } finally {
            execute.clear();
        }
    }

    /**
     * @return задачи которые должны сейчас выполнится.
     */
    private Array<GameTask> getExecute() {
        return execute;
    }

    /**
     * @return ожидающие исполнения задачи.
     */
    private Array<GameTask> getWaitTasks() {
        return waitTasks;
    }
}
