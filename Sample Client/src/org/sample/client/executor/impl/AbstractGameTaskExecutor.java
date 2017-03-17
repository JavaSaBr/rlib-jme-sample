package org.sample.client.executor.impl;

import org.jetbrains.annotations.NotNull;
import org.sample.client.GameThread;
import org.sample.client.executor.GameTaskExecutor;
import org.sample.client.game.task.GameTask;
import rlib.concurrent.lock.LockFactory;
import rlib.concurrent.lock.Lockable;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.Reusable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;

/**
 * The base implementation of task executor.
 *
 * @author JavaSaBr
 */
public abstract class AbstractGameTaskExecutor extends GameThread implements GameTaskExecutor, Lockable {

    protected static final Logger LOGGER = LoggerManager.getLogger(GameTaskExecutor.class);

    @NotNull
    protected static final Consumer<GameTask> FINISH_TASK_FUNC = gameTask -> {
        if (gameTask instanceof Reusable) {
            ((Reusable) gameTask).release();
        }
    };

    /**
     * The list of task to execute.
     */
    @NotNull
    private final Array<GameTask> execute;

    /**
     * The list of executed tasks.
     */
    @NotNull
    private final Array<GameTask> executed;

    /**
     * The list of waiting executing tasks.
     */
    @NotNull
    private final Array<GameTask> waitTasks;

    /**
     * The flag of waiting new tasks.
     */
    @NotNull
    protected final AtomicBoolean wait;

    /**
     * The lock.
     */
    @NotNull
    private final Lock lock;

    public AbstractGameTaskExecutor() {
        this.execute = createExecuteArray();
        this.executed = createExecuteArray();
        this.waitTasks = createExecuteArray();
        this.lock = LockFactory.newAtomicLock();
        this.wait = new AtomicBoolean(false);
    }

    @NotNull
    protected Array<GameTask> createExecuteArray() {
        return ArrayFactory.newArray(GameTask.class);
    }

    @Override
    public void execute(@NotNull final GameTask gameTask) {
        lock();
        try {

            final Array<GameTask> waitTasks = getWaitTasks();
            waitTasks.add(gameTask);

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
     * @return the list of task to execute.
     */
    @NotNull
    protected Array<GameTask> getExecute() {
        return execute;
    }

    /**
     * @return the list of executed tasks.
     */
    @NotNull
    protected Array<GameTask> getExecuted() {
        return executed;
    }

    /**
     * @return the list of waiting executing tasks.
     */
    @NotNull
    protected Array<GameTask> getWaitTasks() {
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
