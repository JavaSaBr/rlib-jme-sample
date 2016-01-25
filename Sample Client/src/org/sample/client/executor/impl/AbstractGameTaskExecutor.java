package org.sample.client.executor.impl;

import org.sample.client.GameThread;
import org.sample.client.executor.GameTaskExecutor;
import org.sample.client.game.task.GameTask;
import rlib.concurrent.lock.LockFactory;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.Synchronized;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.Foldable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;

/**
 * Базовая реализация исполнителя игровых задач.
 *
 * @author Ronn
 */
public abstract class AbstractGameTaskExecutor extends GameThread implements GameTaskExecutor, Synchronized {

    protected static final Logger LOGGER = LoggerManager.getLogger(GameTaskExecutor.class);

    protected static final Consumer<GameTask> FINISH_TASK_FUNC = gameTask -> {
        if (gameTask instanceof Foldable) {
            ((Foldable) gameTask).release();
        }
    };

    /**
     * Список задач на исполнение во время ближайшей фазы.
     */
    private final Array<GameTask> execute;

    /**
     * Список выполненных задач.
     */
    private final Array<GameTask> executed;

    /**
     * Список ожидающих задач на исполнении.
     */
    private final Array<GameTask> waitTasks;

    /**
     * Находится ли исполнитель в ожидании.
     */
    private final AtomicBoolean wait;

    /**
     * Блокировщик.
     */
    private final Lock lock;

    public AbstractGameTaskExecutor() {
        this.execute = createExecuteArray();
        this.executed = createExecuteArray();
        this.waitTasks = createExecuteArray();
        this.lock = LockFactory.newPrimitiveAtomicLock();
        this.wait = new AtomicBoolean(false);
    }

    protected Array<GameTask> createExecuteArray() {
        return ArrayFactory.newArray(GameTask.class);
    }

    @Override
    public void execute(final GameTask gameTask) {
        lock();
        try {

            final Array<GameTask> waitTasks = getWaitTasks();
            waitTasks.add(gameTask);

            final AtomicBoolean wait = getWait();

            if (wait.get()) {
                synchronized (wait) {
                    if (wait.compareAndSet(true, false)) {
                        ConcurrentUtils.notifyAllInSynchronize(wait);
                    }
                }
            }

        } finally {
            unlock();
        }
    }

    /**
     * @return список задач на исполнение во время ближайшей фазы.
     */
    public Array<GameTask> getExecute() {
        return execute;
    }

    /**
     * @return список выполненных задач.
     */
    public Array<GameTask> getExecuted() {
        return executed;
    }

    /**
     * @return находится ли исполнитель в ожидании.
     */
    public AtomicBoolean getWait() {
        return wait;
    }

    /**
     * @return список ожидающих задач на исполнении.
     */
    public Array<GameTask> getWaitTasks() {
        return waitTasks;
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void unlock() {
        lock.unlock();
    }
}
