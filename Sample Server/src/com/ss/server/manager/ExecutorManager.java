package com.ss.server.manager;

import static rlib.util.ClassUtils.unsafeCast;
import com.ss.server.Config;
import com.ss.server.LocalObjects;
import com.ss.server.ServerThread;
import com.ss.server.executor.impl.AsyncPacketExecutor;
import com.ss.server.executor.impl.SyncPacketExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.concurrent.GroupThreadFactory;
import rlib.concurrent.executor.TaskExecutor;
import rlib.concurrent.task.SimpleTask;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;

import java.util.concurrent.*;

/**
 * The manager to manage executing tasks.
 *
 * @author JavaSaBr
 */
public final class ExecutorManager {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(ExecutorManager.class);

    @NotNull
    private static final Runtime RUNTIME = Runtime.getRuntime();

    private static ExecutorManager instance;

    @NotNull
    public static ExecutorManager getInstance() {

        if (instance == null) {
            instance = new ExecutorManager();
        }

        return instance;
    }

    /**
     * The general task executor.
     */
    @NotNull
    private final ScheduledExecutorService generalExecutor;

    /**
     * The executor of async tasks.
     */
    @NotNull
    private final ExecutorService executor;

    /**
     * The executor to sync executing client packets.
     */
    @NotNull
    private final TaskExecutor<LocalObjects> syncPacketExecutor;

    /**
     * The executor to async executing client packets.
     */
    @NotNull
    private final TaskExecutor<LocalObjects> asyncPacketExecutor;

    private ExecutorManager() {
        InitializeManager.valid(getClass());

        this.generalExecutor = Executors.newScheduledThreadPool(Config.THREAD_EXECUTOR_GENERAL_SIZE, new GroupThreadFactory("GeneralExecutor", ServerThread.class, Thread.NORM_PRIORITY));
        this.executor = Executors.newFixedThreadPool(2, new GroupThreadFactory("AsyncThreadExecutor", ServerThread.class, Thread.MAX_PRIORITY));
        this.syncPacketExecutor = new SyncPacketExecutor();
        this.asyncPacketExecutor = AsyncPacketExecutor.create(RUNTIME.availableProcessors(), 5);

        LOGGER.info("initialized.");
    }

    /**
     * Execute a task.
     *
     * @param runnable the task.
     */
    public void execute(@NotNull final Runnable runnable) {
        executor.execute(runnable);
    }

    /**
     * Async execute a task.
     *
     * @param task the task.
     */
    public void runAsyncPacket(@NotNull final SimpleTask<LocalObjects> task) {
        asyncPacketExecutor.execute(task);
    }

    /**
     * Sync execute a task.
     *
     * @param task the task.
     */
    public void runSyncPacket(@NotNull final SimpleTask<LocalObjects> task) {
        syncPacketExecutor.execute(task);
    }

    /**
     * Schedule a task.
     *
     * @param runnable the task.
     * @param delay    the delay to execute.
     * @return the reference to the task.
     */
    @Nullable
    public <T extends Runnable> ScheduledFuture<T> scheduleGeneral(@NotNull final T runnable, long delay) {
        try {
            if (delay < 0) delay = 0;
            return unsafeCast(generalExecutor.schedule(runnable, delay, TimeUnit.MILLISECONDS));
        } catch (final RejectedExecutionException e) {
            LOGGER.warning(e);
        }
        return null;
    }

    /**
     * Schedule a task.
     *
     * @param runnable the task.
     * @param delay    the delay to execute.
     * @param interval the executing interval.
     * @return the reference to the task.
     */
    @Nullable
    public <T extends Runnable> ScheduledFuture<T> scheduleGeneralAtFixedRate(@NotNull final T runnable, long delay,
                                                                              long interval) {
        try {
            if (delay < 0) delay = 0;
            if (interval <= 0) interval = 1;
            return unsafeCast(generalExecutor.scheduleAtFixedRate(runnable, delay, interval, TimeUnit.MILLISECONDS));
        } catch (final RejectedExecutionException e) {
            LOGGER.warning(e);
        }
        return null;
    }
}