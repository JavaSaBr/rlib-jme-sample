package com.ss.client.executor.impl;

import static com.ss.client.GameClient.getCurrentTime;
import static com.ss.client.util.LocalObjects.localObjects;
import com.ss.client.game.task.GameTask;
import com.ss.client.util.LocalObjects;
import org.jetbrains.annotations.NotNull;
import rlib.function.ObjectLongObjectConsumer;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.array.ConcurrentArray;
import rlib.util.pools.Reusable;

/**
 * The implementation of an executor to execute taks in the main game thread.
 *
 * @author Ronn
 */
public class GameThreadExecutor {

    @NotNull
    private static final GameThreadExecutor INSTANCE = new GameThreadExecutor();

    @NotNull
    public static GameThreadExecutor getInstance() {
        return INSTANCE;
    }

    @NotNull
    private static final ObjectLongObjectConsumer<GameTask, LocalObjects> EXECUTE_FUNCTION =
            (ObjectLongObjectConsumer<GameTask, LocalObjects>) (task, time, local) -> {

        task.execute(local, time);

        if (task instanceof Reusable) {
            ((Reusable) task).release();
        }
    };

    /**
     * The list of waiting tasks.
     */
    @NotNull
    private final ConcurrentArray<GameTask> waitTasks;

    /**
     * The list of tasks to execute.
     */
    @NotNull
    private final Array<GameTask> execute;

    public GameThreadExecutor() {
        this.waitTasks = ArrayFactory.newConcurrentAtomicARSWLockArray(GameTask.class);
        this.execute = ArrayFactory.newArray(GameTask.class);
    }

    /**
     * Add a new task to execute.
     *
     * @param gameTask the new game task.
     */
    public void addToExecute(@NotNull final GameTask gameTask) {

        final ConcurrentArray<GameTask> waitTasks = getWaitTasks();
        final long stamp = waitTasks.writeLock();
        try {
            waitTasks.add(gameTask);
        } finally {
            waitTasks.writeUnlock(stamp);
        }
    }

    /**
     * Execute all tasks.
     */
    public void execute() {

        final ConcurrentArray<GameTask> waitTasks = getWaitTasks();
        if (waitTasks.isEmpty()) return;

        final Array<GameTask> execute = getExecute();
        try {

            final long stamp = waitTasks.writeLock();
            try {
                execute.addAll(waitTasks);
                waitTasks.clear();
            } finally {
                waitTasks.writeUnlock(stamp);
            }

            final LocalObjects local = localObjects();
            final long currentTime = getCurrentTime();

            execute.forEach(currentTime, local, EXECUTE_FUNCTION);

        } finally {
            execute.clear();
        }
    }

    /**
     * @return the list of tasks to execute.
     */
    @NotNull
    private Array<GameTask> getExecute() {
        return execute;
    }

    /**
     * @return the list of waiting tasks.
     */
    @NotNull
    private ConcurrentArray<GameTask> getWaitTasks() {
        return waitTasks;
    }
}
