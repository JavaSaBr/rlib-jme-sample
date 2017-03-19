package com.ss.client.executor.impl;

import static java.lang.Math.min;
import com.ss.client.GameClient;
import com.ss.client.game.task.GameTask;
import com.ss.client.util.LocalObjects;
import org.jetbrains.annotations.NotNull;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.util.array.Array;

/**
 * The implementation of an executor to execute background tasks.
 *
 * @author JavaSaBr
 */
public class BackgroundGameTaskExecutor extends AbstractGameTaskExecutor {

    /**
     * The runtime.
     */
    private static final Runtime RUNTIME = Runtime.getRuntime();

    /**
     * The max task count to execute per an iteration.
     */
    private static final int PROP_MAXIMUM_UPDATE = 500 / RUNTIME.availableProcessors();

    /**
     * The execute limit of task which uses the same current time.
     */
    private static final int PROP_EXECUTE_LIMIT = 5;

    public BackgroundGameTaskExecutor(final int order) {
        setName(BackgroundGameTaskExecutor.class.getSimpleName() + "_" + order);
        setPriority(Thread.NORM_PRIORITY - 2);
        start();
    }

    /**
     * Execute tasks.
     */
    protected void execute(@NotNull final Array<GameTask> execute, @NotNull final Array<GameTask> executed,
                           @NotNull final LocalObjects local) {

        final GameTask[] array = execute.array();

        for (int i = 0, length = min(execute.size(), PROP_MAXIMUM_UPDATE); i < length; ) {

            final long currentTime = GameClient.getCurrentTime();

            for (int count = 0; count < PROP_EXECUTE_LIMIT && i < length; count++, i++) {

                final GameTask task = array[i];

                if (task.execute(local, currentTime)) {
                    executed.add(task);
                }
            }
        }
    }

    @Override
    public void run() {

        final Array<GameTask> execute = getExecute();
        final Array<GameTask> executed = getExecuted();
        final Array<GameTask> waitTasks = getWaitTasks();

        final LocalObjects local = getLocalObects();

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

            execute(execute, executed, local);

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
