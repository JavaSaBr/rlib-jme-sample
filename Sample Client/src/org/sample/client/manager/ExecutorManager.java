package org.sample.client.manager;

import org.sample.client.GameThread;
import rlib.concurrent.GroupThreadFactory;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

import java.util.concurrent.*;

/**
 * Менеджер пула потоков.
 *
 * @author Ronn
 */
public final class ExecutorManager {

    private static final Logger LOGGER = LoggerManager.getLogger(ExecutorManager.class);

    private static ExecutorManager instance;

    public static ExecutorManager getInstance() {

        if (instance == null) {
            instance = new ExecutorManager();
        }

        return instance;
    }

    /**
     * Исполнитель асинхронных действий.
     */
    private final ExecutorService asyncExecutor;

    /**
     * Исполнитель синхроных задач.
     */
    private final ExecutorService syncExecutor;

    /**
     * Исполнитель ожидающих задач.
     */
    private final ExecutorService waiteableExecutor;

    /**
     * Исполнитель смены стадий интерфейса.
     */
    private final ExecutorService switchStageExecutor;

    private ExecutorManager() {
        syncExecutor = Executors.newFixedThreadPool(1, new GroupThreadFactory("SynExecutor", GameThread.class, Thread.MAX_PRIORITY));
        asyncExecutor = Executors.newFixedThreadPool(3, new GroupThreadFactory("AsynExecutor", GameThread.class, Thread.MAX_PRIORITY));
        switchStageExecutor = Executors.newSingleThreadExecutor(new GroupThreadFactory("SwitchStateExecutor", GameThread.class, Thread.MIN_PRIORITY));
        waiteableExecutor = Executors.newCachedThreadPool(new GroupThreadFactory("WaitableExecutor", GameThread.class, Thread.MAX_PRIORITY));
    }

    /**
     * Выполнение задания в параллельном потоке.
     *
     * @param runnable задание, которое надо выполнить.
     */
    public void asyncExecute(final Runnable runnable) {
        asyncExecutor.execute(runnable);
    }

    /**
     * Выполнение задания по смене стадии интерфейса.
     *
     * @param runnable задание, которое надо выполнить.
     */
    public void switchStateExecute(final Runnable runnable) {
        switchStageExecutor.execute(runnable);
    }

    /**
     * Выполнение задачи в синхронном порядке.
     *
     * @param runnable выполняемая задача.
     */
    public void syncExecute(final Runnable runnable) {
        syncExecutor.execute(runnable);
    }

    /**
     * Выполнение ожидающей задачи.
     *
     * @param runnable выполняемая задача.
     */
    public void waitableExecute(final Runnable runnable) {
        waiteableExecutor.execute(runnable);
    }
}
