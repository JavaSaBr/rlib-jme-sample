package org.sample.server.manager;

import org.sample.server.Config;
import org.sample.server.LocalObjects;
import org.sample.server.ServerThread;
import org.sample.server.executor.impl.AsyncPacketExecutor;
import org.sample.server.executor.impl.SyncPacketExecutor;
import rlib.concurrent.GroupThreadFactory;
import rlib.concurrent.executor.TaskExecutor;
import rlib.concurrent.task.SimpleTask;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;

import java.util.concurrent.*;

/**
 * Менеджер серверных потоков.
 *
 * @author Ronn
 */
public final class ExecutorManager {

    private static final Logger LOGGER = LoggerManager.getLogger(ExecutorManager.class);

    private static final Runtime RUNTIME = Runtime.getRuntime();

    private static ExecutorManager instance;

    public static ExecutorManager getInstance() {

        if (instance == null) {
            instance = new ExecutorManager();
        }

        return instance;
    }

    /**
     * Исполнитель основных заданий.
     */
    private final ScheduledExecutorService generalExecutor;

    /**
     * Исполнитель ид генераторов заданий.
     */
    private final ScheduledExecutorService idGeneratorExecutor;

    /**
     * Обработчик параллельных дейтсвий.
     */
    private final ExecutorService executor;

    /**
     * Синхронизированный исполнитель пакетов.
     */
    private final TaskExecutor<LocalObjects> syncPacketExecutor;

    /**
     * Асинхроный исполнитель клиентских пакетов.
     */
    private final TaskExecutor<LocalObjects> asyncPacketExecutor;

    private ExecutorManager() {
        InitializeManager.valid(getClass());

        this.generalExecutor = Executors.newScheduledThreadPool(Config.THREAD_EXECUTOR_GENERAL_SIZE, new GroupThreadFactory("GeneralExecutor", ServerThread.class, Thread.NORM_PRIORITY));
        this.idGeneratorExecutor = Executors.newSingleThreadScheduledExecutor();
        this.executor = Executors.newFixedThreadPool(2, new GroupThreadFactory("AsyncThreadExecutor", ServerThread.class, Thread.MAX_PRIORITY));
        this.syncPacketExecutor = new SyncPacketExecutor();
        this.asyncPacketExecutor = AsyncPacketExecutor.create(RUNTIME.availableProcessors(), 5);

        LOGGER.info("initialized.");
    }

    /**
     * Выполнение задания в параллельном потоке.
     *
     * @param runnable задание, которое надо выполнить.
     */
    public void execute(final Runnable runnable) {
        executor.execute(runnable);
    }

    /**
     * @return исполнитель для ид генераторов.
     */
    public ScheduledExecutorService getIdGeneratorExecutor() {
        return idGeneratorExecutor;
    }

    /**
     * Асинхронное выполнение задачи клиентского пакета.
     *
     * @param task задача выполнения пакета.
     */
    public void runAsyncPacket(final SimpleTask<LocalObjects> task) {
        asyncPacketExecutor.execute(task);
    }

    /**
     * Синхронное выполнение задачи клиентского пакета.
     *
     * @param task задача выполнения пакета.
     */
    public void runSyncPacket(final SimpleTask<LocalObjects> task) {
        syncPacketExecutor.execute(task);
    }

    /**
     * Создание отложенного общего таска.
     *
     * @param runnable содержание таска.
     * @param delay    задержка перед выполнением.
     * @return ссылка на таск.
     */
    @SuppressWarnings("unchecked")
    public <T extends Runnable> ScheduledFuture<T> scheduleGeneral(final T runnable, long delay) {
        try {

            if (delay < 0) {
                delay = 0;
            }

            return (ScheduledFuture<T>) generalExecutor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
        } catch (final RejectedExecutionException e) {
            LOGGER.warning(e);
        }

        return null;
    }

    /**
     * Создание периодического отложенного общего таска.
     *
     * @param runnable содержание таска.
     * @param delay    задержка перед первым запуском.
     * @param interval интервал между выполнением таска.
     * @return ссылка на таск.
     */
    @SuppressWarnings("unchecked")
    public <T extends Runnable> ScheduledFuture<T> scheduleGeneralAtFixedRate(final T runnable, final long delay, long interval) {
        try {

            if (interval <= 0) {
                interval = 1;
            }

            return (ScheduledFuture<T>) generalExecutor.scheduleAtFixedRate(runnable, delay, interval, TimeUnit.MILLISECONDS);
        } catch (final RejectedExecutionException e) {
            LOGGER.warning(e);
        }

        return null;
    }
}