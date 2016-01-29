package org.sample.server.executor;

import org.sample.server.LocalObjects;
import rlib.concurrent.executor.PeriodicTaskExecutor;
import rlib.concurrent.task.PeriodicTask;

/**
 * Интерфейс для реализации исполнителя игровых задач.
 *
 * @author Ronn
 */
public interface GameExecutor<T extends PeriodicTask<LocalObjects>> extends PeriodicTaskExecutor<T, LocalObjects> {
}
