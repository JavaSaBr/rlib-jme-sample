package org.sample.client.executor.impl;

import org.sample.client.SampleGame;
import org.sample.client.game.task.GameTask;
import org.sample.client.util.LocalObjects;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.util.array.Array;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.min;

/**
 * Реализация исполнителья фоновых игровых задач.
 *
 * @author Ronn
 */
public class BackgroundGameTaskExecutor extends AbstractGameTaskExecutor {

    /**
     * Рантайм.
     */
    private static final Runtime RUNTIME = Runtime.getRuntime();

    /**
     * Максимум обновляемых задач за проход.
     */
    private static final int PROP_MAXIMUM_UPDATE = 500 / RUNTIME.availableProcessors();

    /**
     * Лимит выполнений задач за одну фазу.
     */
    private static final int PROP_EXECUTE_LIMIT = 5;

    public BackgroundGameTaskExecutor(final int order) {
        setName(BackgroundGameTaskExecutor.class.getSimpleName() + "_" + order);
        setPriority(NORM_PRIORITY - 2);
        start();
    }

    /**
     * Процесс обновления состояния задач.
     */
    protected void doUpdate(final Array<GameTask> execute, final Array<GameTask> executed, final LocalObjects local) {

        final GameTask[] array = execute.array();

        for (int i = 0, length = min(execute.size(), PROP_MAXIMUM_UPDATE); i < length; ) {

            final long currentTime = SampleGame.getCurrentTime();

            for (int count = 0, limit = PROP_EXECUTE_LIMIT; count < limit && i < length; count++, i++) {

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
        final AtomicBoolean wait = getWait();

        while (true) {

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

            // обновление состояния задач
            doUpdate(execute, executed, local);

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
