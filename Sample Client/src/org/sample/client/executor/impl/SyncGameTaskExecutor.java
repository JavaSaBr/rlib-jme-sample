package org.sample.client.executor.impl;

import org.sample.client.SampleGame;
import org.sample.client.game.task.GameTask;
import org.sample.client.util.LocalObjects;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.concurrent.util.ThreadUtils;
import rlib.util.array.Array;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Реализация исполнителья игровых задач в синхронизированной области.
 *
 * @author Ronn
 */
public class SyncGameTaskExecutor extends AbstractGameTaskExecutor {

    private static final int EXECUTE_LIMIT = 5;

    public SyncGameTaskExecutor() {
        setName(SyncGameTaskExecutor.class.getSimpleName());
        setPriority(NORM_PRIORITY);
        start();
    }

    /**
     * Процесс обновления состояния задач.
     */
    protected void doExecute(final Array<GameTask> execute, final Array<GameTask> executed, final LocalObjects local, final SampleGame game) {

        final GameTask[] array = execute.array();

        for (int i = 0, length = execute.size(); i < length; ) {

            final long time = System.currentTimeMillis();
            final long stamp = game.trySyncLock();

            if (stamp == 0) {
                ThreadUtils.sleep(1);
                continue;
            }

            try {

                final long currentTime = SampleGame.getCurrentTime();

                for (int count = 0, limit = EXECUTE_LIMIT; count < limit && i < length; count++, i++) {

                    final GameTask task = array[i];

                    if (task.execute(local, currentTime)) {
                        executed.add(task);
                    }
                }

            } catch (final Exception e) {
                LOGGER.warning(e);
            } finally {
                game.syncUnlock(stamp);
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

        final SampleGame game = SampleGame.getInstance();

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
            doExecute(execute, executed, local, game);

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
