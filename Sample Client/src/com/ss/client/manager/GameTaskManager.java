package com.ss.client.manager;

import com.ss.client.game.task.GameTask;
import org.jetbrains.annotations.NotNull;
import com.ss.client.executor.GameTaskExecutor;
import com.ss.client.executor.impl.BackgroundGameTaskExecutor;
import com.ss.client.executor.impl.FXGameTaskExecutor;
import com.ss.client.executor.impl.GameThreadExecutor;
import com.ss.client.game.task.GameTask;
import rlib.concurrent.atomic.AtomicInteger;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;

/**
 * The manager of executing game tasks.
 *
 * @author JavaSaBr
 */
public class GameTaskManager {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(GameTaskManager.class);

    @NotNull
    private static final Runtime RUNTIME = Runtime.getRuntime();

    public static final int PROP_EXECUTE_LIMIT = 1000;

    private static GameTaskManager instance;

    public static GameTaskManager getInstance() {

        if (instance == null) {
            instance = new GameTaskManager();
        }

        return instance;
    }

    /**
     * The list of background tasks executors.
     */
    @NotNull
    private final GameTaskExecutor[] backgroundTaskExecutors;

    /**
     * The executor of game task in the main thread.
     */
    @NotNull
    private final GameThreadExecutor gameThreadExecutor;

    /**
     * The executor of javaFx tasks.
     */
    @NotNull
    private final GameTaskExecutor fxTaskExecutor;

    /**
     * The index of the next background task executor.
     */
    @NotNull
    private final AtomicInteger nextBackgroundTaskExecutor;

    private GameTaskManager() {
        InitializeManager.valid(getClass());

        this.gameThreadExecutor = GameThreadExecutor.getInstance();
        this.fxTaskExecutor = new FXGameTaskExecutor();

        this.backgroundTaskExecutors = new GameTaskExecutor[RUNTIME.availableProcessors()];

        for (int i = 0, length = backgroundTaskExecutors.length; i < length; i++) {
            backgroundTaskExecutors[i] = new BackgroundGameTaskExecutor(i + 1);
        }

        this.nextBackgroundTaskExecutor = new AtomicInteger(0);

        LOGGER.info("initialized.");
    }

    /**
     * Add a new background game task to execute.
     *
     * @param task the new task.
     */
    public void addBackgroundTask(@NotNull final GameTask task) {

        final GameTaskExecutor[] executors = getBackgroundTaskExecutors();
        final int index = nextBackgroundTaskExecutor.incrementAndGet();

        if (index < executors.length) {
            executors[index].execute(task);
        } else {
            nextBackgroundTaskExecutor.set(0);
            executors[0].execute(task);
        }
    }

    /**
     * Add a javaFx task to execute.
     *
     * @param task the new javaFX task.
     */
    public void addFXTask(@NotNull final GameTask task) {
        fxTaskExecutor.execute(task);
    }

    /**
     * Add new game task to execute in the main thread.
     *
     * @param task the game task.
     */
    public void addGameTask(final GameTask task) {
        gameThreadExecutor.addToExecute(task);
    }

    /**
     * @return the list of background tasks executors.
     */
    @NotNull
    private GameTaskExecutor[] getBackgroundTaskExecutors() {
        return backgroundTaskExecutors;
    }
}
