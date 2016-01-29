package org.sample.server.executor.impl;

import org.sample.server.LocalObjects;
import org.sample.server.ServerThread;
import org.sample.server.executor.GameExecutor;
import rlib.concurrent.executor.impl.SingleThreadPeriodicTaskExecutor;
import rlib.concurrent.task.PeriodicTask;

/**
 * Базовая реализация исполнителя игровых задач.
 *
 * @author Ronn
 */
public abstract class AbstractGameExecutor<T extends PeriodicTask<LocalObjects>> extends SingleThreadPeriodicTaskExecutor<T, LocalObjects> implements GameExecutor<T> {

    public AbstractGameExecutor(final Class<?> taskClass, final int priority, final int interval, final String name) {
        super(ServerThread.class, priority, interval, name, taskClass, null);
    }

    @Override
    public void addTask(final T task) {

        if (task == null) {
            return;
        }

        super.addTask(task);
    }

    @Override
    protected LocalObjects check(final LocalObjects localObjects, final Thread thread) {
        return ((ServerThread) thread).getLocal();
    }

    @Override
    public void removeTask(final T task) {

        if (task == null) {
            return;
        }

        super.removeTask(task);
    }
}
