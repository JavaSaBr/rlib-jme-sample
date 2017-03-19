package com.ss.client.executor.impl;

import com.ss.client.game.task.GameTask;
import com.sun.javafx.application.PlatformImpl;
import org.jetbrains.annotations.NotNull;
import com.ss.client.GameClient;
import com.ss.client.util.LocalObjects;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.util.array.Array;

/**
 * The implementation of an executor to execute JavaFX tasks.
 *
 * @author JavaSaBr
 */
public class FXGameTaskExecutor extends AbstractGameTaskExecutor {

    /**
     * The max of javaFX tasks to execute per an iteration.
     */
    private static final int EXECUTE_LIMIT = 300;

    /**
     * The local objects for javaFX thread.
     */
    @NotNull
    private final LocalObjects fxLocal;

    /**
     * The task to execute list of tasks in javaFX thread.
     */
    @NotNull
    private final Runnable fxTask = () -> execute(getExecute(), getExecuted(), getFxLocal());

    public FXGameTaskExecutor() {
        setName(FXGameTaskExecutor.class.getSimpleName());
        setPriority(NORM_PRIORITY);
        this.fxLocal = new LocalObjects();
        start();
    }

    /**
     * Execute javaFX tasks.
     */
    protected void execute(@NotNull final Array<GameTask> execute, @NotNull final Array<GameTask> executed,
                           @NotNull final LocalObjects local) {

        final GameTask[] array = execute.array();

        for (int i = 0, length = execute.size(); i < length; ) {
            try {

                final long currentTime = GameClient.getCurrentTime();

                for (int count = 0; count < EXECUTE_LIMIT && i < length; count++, i++) {

                    final GameTask task = array[i];

                    if (task.execute(local, currentTime)) {
                        executed.add(task);
                    }
                }

            } catch (final Exception e) {
                LOGGER.warning(e);
            }
        }
    }

    /**
     * @return the local objects for javaFX thread.
     */
    @NotNull
    private LocalObjects getFxLocal() {
        return fxLocal;
    }

    @Override
    public void run() {

        final Array<GameTask> execute = getExecute();
        final Array<GameTask> executed = getExecuted();
        final Array<GameTask> waitTasks = getWaitTasks();

        for(;;) {

            executed.clear();
            execute.clear();

            lock();
            try {

                if (waitTasks.isEmpty()) {
                    wait.getAndSet(true);
                } else {
                    execute.addAll(waitTasks);
                }

            } finally {
                unlock();
            }

            if (wait.get()) {
                synchronized (wait) {
                    if (wait.get()) {
                        ConcurrentUtils.waitInSynchronize(wait);
                    }
                }
            }

            if (execute.isEmpty()) {
                continue;
            }

            PlatformImpl.runAndWait(fxTask);

            if (executed.isEmpty()) {
                continue;
            }

            lock();
            try {
                waitTasks.removeAll(executed);
            } finally {
                unlock();
            }

            executed.forEach(FINISH_TASK_FUNC);
        }
    }
}
