package com.ss.server.executor;

import com.ss.server.LocalObjects;
import rlib.concurrent.executor.PeriodicTaskExecutor;
import rlib.concurrent.task.PeriodicTask;

/**
 * The interface to implement game tasks executor.
 *
 * @author JavaSaBr
 */
public interface GameTaskExecutor<T extends PeriodicTask<LocalObjects>> extends PeriodicTaskExecutor<T, LocalObjects> {
}
