package com.ss.server.executor.impl;

import com.ss.server.LocalObjects;
import com.ss.server.ServerThread;
import com.ss.server.executor.GameTaskExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.concurrent.executor.impl.SingleThreadPeriodicTaskExecutor;
import rlib.concurrent.task.PeriodicTask;

/**
 * The base implementation of the game task executor.
 *
 * @author JavaSaBr
 */
public abstract class AbstractGameTaskExecutor<T extends PeriodicTask<LocalObjects>> extends
        SingleThreadPeriodicTaskExecutor<T, LocalObjects> implements GameTaskExecutor<T> {

    public AbstractGameTaskExecutor(final Class<?> taskClass, final int priority, final int interval,
                                    final String name) {
        super(ServerThread.class, priority, interval, name, taskClass, null);
    }

    @NotNull
    @Override
    protected LocalObjects check(@Nullable final LocalObjects localObjects, @NotNull final Thread thread) {
        return ((ServerThread) thread).getLocal();
    }
}
