package org.sample.client.manager;

import org.sample.client.executor.GameTaskExecutor;
import org.sample.client.executor.impl.BackgroundGameTaskExecutor;
import org.sample.client.executor.impl.FXGameTaskExecutor;
import org.sample.client.executor.impl.SyncGameTaskExecutor;
import org.sample.client.game.task.GameTask;
import rlib.concurrent.atomic.AtomicInteger;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;

/**
 * Менеджер по исполнению различных задач.
 *
 * @author Ronn
 */
public class GameTaskManager {

    private static final Logger LOGGER = LoggerManager.getLogger(GameTaskManager.class);

    private static final Runtime RUNTIME = Runtime.getRuntime();

    public static final int PROP_BACKGROUND_TASK_EXECUTORS = RUNTIME.availableProcessors();

    public static final int PROP_EXECUTE_LIMIT = 1000;

    public static final int PROP_MIN_LIMIT = 10;
    public static final int PROP_NORMAL_LIMIT = 20;
    public static final int PROP_MAX_LIMIT = 30;

    private static GameTaskManager instance;

    public static GameTaskManager getInstance() {

        if (instance == null) {
            instance = new GameTaskManager();
        }

        return instance;
    }

    /**
     * Список исполнителей фоновых.
     */
    private final GameTaskExecutor[] backgroundTaskExecutors;

    /**
     * Исполнитель синхронизированных задач.
     */
    private final GameTaskExecutor syncTaskExecutor;

    /**
     * Исполнитель задач по обновлению FX UI.
     */
    private final GameTaskExecutor fxTaskExecutor;

    /**
     * Индекс следующего исполнителя фоновых задач.
     */
    private final AtomicInteger nextBackgroundTaskExecutor;

    public GameTaskManager() {
        InitializeManager.valid(getClass());

        this.syncTaskExecutor = new SyncGameTaskExecutor();
        this.fxTaskExecutor = new FXGameTaskExecutor();

        this.backgroundTaskExecutors = new GameTaskExecutor[PROP_BACKGROUND_TASK_EXECUTORS];

        for (int i = 0, length = backgroundTaskExecutors.length; i < length; i++) {
            backgroundTaskExecutors[i] = new BackgroundGameTaskExecutor(i + 1);
        }

        this.nextBackgroundTaskExecutor = new AtomicInteger(0);

        LOGGER.info("initialized.");
    }

    /**
     * Добавление на обработку фоновой задачи.
     *
     * @param task фоновая задача.
     */
    public void addBackgroundTask(final GameTask task) {

        final GameTaskExecutor[] executors = getBackgroundTaskExecutors();
        final AtomicInteger nextTaskExecutor = getNextBackgroundTaskExecutor();

        final int index = nextTaskExecutor.incrementAndGet();

        if (index < executors.length) {
            executors[index].execute(task);
        } else {
            nextTaskExecutor.set(0);
            executors[0].execute(task);
        }
    }

    /**
     * Добавление на обработку задачи по обновлению FX UI.
     *
     * @param task задача.
     */
    public void addFXTask(final GameTask task) {
        final GameTaskExecutor executor = getFxTaskExecutor();
        executor.execute(task);
    }

    /**
     * Добавление на обработку пакетной задачи.
     *
     * @param task пакетная задача.
     */
    public void addSyncTask(final GameTask task) {
        final GameTaskExecutor executor = getSyncTaskExecutor();
        executor.execute(task);
    }

    /**
     * @return список исполнителей фоновых.
     */
    protected GameTaskExecutor[] getBackgroundTaskExecutors() {
        return backgroundTaskExecutors;
    }

    /**
     * @return исполнитель задач по обновлению FX UI.
     */
    protected GameTaskExecutor getFxTaskExecutor() {
        return fxTaskExecutor;
    }

    /**
     * @return индекс следующего исполнителя фоновых задач.
     */
    protected AtomicInteger getNextBackgroundTaskExecutor() {
        return nextBackgroundTaskExecutor;
    }

    /**
     * @return исполнитель синхронизированных задач.
     */
    protected GameTaskExecutor getSyncTaskExecutor() {
        return syncTaskExecutor;
    }
}
