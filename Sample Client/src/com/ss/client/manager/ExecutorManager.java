package com.ss.client.manager;

import com.ss.client.GameThread;
import org.jetbrains.annotations.NotNull;
import rlib.concurrent.GroupThreadFactory;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The manager of executing other tasks.
 *
 * @author JavaSaBr
 */
public final class ExecutorManager {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(ExecutorManager.class);

    private static ExecutorManager instance;

    public static ExecutorManager getInstance() {

        if (instance == null) {
            instance = new ExecutorManager();
        }

        return instance;
    }

    /**
     * The general executor.
     */
    @NotNull
    private final ScheduledExecutorService generalExecutor;

    /**
     * The async tasks executor.
     */
    @NotNull
    private final ExecutorService asyncExecutor;

    /**
     * The sync tasks executor.
     */
    @NotNull
    private final ExecutorService syncExecutor;

    /**
     * The waitable tasks executor.
     */
    @NotNull
    private final ExecutorService waitableExecutor;

    /**
     * The switch stage tasks executor.
     */
    @NotNull
    private final ExecutorService switchStageExecutor;

    private ExecutorManager() {
        generalExecutor = Executors.newSingleThreadScheduledExecutor();
        syncExecutor = Executors.newFixedThreadPool(1, new GroupThreadFactory("SynExecutor", GameThread.class, Thread.MAX_PRIORITY));
        asyncExecutor = Executors.newFixedThreadPool(3, new GroupThreadFactory("AsynExecutor", GameThread.class, Thread.MAX_PRIORITY));
        switchStageExecutor = Executors.newSingleThreadExecutor(new GroupThreadFactory("SwitchStateExecutor", GameThread.class, Thread.MIN_PRIORITY));
        waitableExecutor = Executors.newCachedThreadPool(new GroupThreadFactory("WaitableExecutor", GameThread.class, Thread.MAX_PRIORITY));
    }

    /**
     * Execute the task with delay.
     *
     * @param runnable the task.
     * @param delay    the delay.
     */
    public void execute(@NotNull final Runnable runnable, final long delay) {
        if (delay < 1) throw new IllegalArgumentException("delay can't be less than 1");
        generalExecutor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    public void asyncExecute(@NotNull final Runnable runnable) {
        asyncExecutor.execute(runnable);
    }

    public void switchStateExecute(@NotNull final Runnable runnable) {
        switchStageExecutor.execute(runnable);
    }

    public void syncExecute(@NotNull final Runnable runnable) {
        syncExecutor.execute(runnable);
    }

    public void waitableExecute(@NotNull final Runnable runnable) {
        waitableExecutor.execute(runnable);
    }
}
