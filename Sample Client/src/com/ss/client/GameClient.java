package com.ss.client;

import static java.util.Objects.requireNonNull;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.Environment;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3x.jfx.JmeFxContainer;
import com.jme3x.jfx.util.os.OperatingSystem;
import com.ss.client.config.CommandLineConfig;
import com.ss.client.config.Config;
import com.ss.client.config.GameConfig;
import com.ss.client.config.ScreenSize;
import com.ss.client.executor.impl.GameThreadExecutor;
import com.ss.client.game.task.SwitchStateTask;
import com.ss.client.manager.ClassManager;
import com.ss.client.manager.ExecutorManager;
import com.ss.client.manager.GameTaskManager;
import com.ss.client.manager.UpdateObjectManager;
import com.ss.client.model.impl.UserAccount;
import com.ss.client.network.Network;
import com.ss.client.stage.StageType;
import com.ss.client.ui.ui.UbuntuCursorProvider;
import com.sun.javafx.cursor.CursorType;
import javafx.application.Platform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerLevel;
import rlib.logging.LoggerManager;
import rlib.logging.impl.FolderFileListener;
import rlib.manager.InitializeManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.StampedLock;
import java.util.logging.Level;

/**
 * The main class of the game.
 *
 * @author JavaSaBr
 */
public class GameClient extends SimpleApplication {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(GameClient.class);

    @NotNull
    private static final GameClient GAME = new GameClient();

    @NotNull
    public static GameClient getInstance() {
        return GAME;
    }

    static void start(@NotNull final String[] args) throws IOException {

        // fix font render
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");

        Config.init();
        CommandLineConfig.args(args);

        configureLogger();

        try {

            ScreenSize.init();

            LOGGER.info("address of server " + Config.SERVER_SOCKET_ADDRESS);

            final GameConfig config = GameConfig.getInstance();
            final AppSettings settings = config.getSettings();

            GAME.setSettings(settings);
            GAME.setShowSettings(false);
            GAME.setDisplayStatView(false);
            GAME.setDisplayFps(false);
            GAME.start();

        } catch (final Exception e) {
            LOGGER.warning(e);
        }
    }

    private static void configureLogger() {

        // disable default logger
        java.util.logging.Logger.getLogger("").setLevel(Level.SEVERE);

        // configure logger from rlib
        LoggerLevel.DEBUG.setEnabled(false);
        LoggerLevel.INFO.setEnabled(true);
        LoggerLevel.ERROR.setEnabled(true);
        LoggerLevel.WARNING.setEnabled(true);

        final Path logFolder = Paths.get(Config.PROJECT_PATH, "log");

        if (!Files.exists(logFolder)) {
            try {
                Files.createDirectories(logFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LoggerManager.addListener(new FolderFileListener(logFolder));
    }

    /**
     * @return the current time.
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * The synchronizer.
     */
    @NotNull
    private final StampedLock lock;

    /**
     * the user account.
     */
    @NotNull
    private final UserAccount userAccount;

    /**
     * The count of waited updaters to update geometries.
     */
    @NotNull
    private final AtomicInteger waitedUpdatersGeometriesCount;

    /**
     * The count of started updaters to update geometries.
     */
    @NotNull
    private final AtomicInteger startedUpdatersGeometriesCount;

    /**
     * The wait state.
     */
    @NotNull
    private final AtomicInteger waitState;

    /**
     * The flat of stopping scene render.
     */
    @NotNull
    private final AtomicInteger stopSceneRender;

    /**
     * The prev stage of the game.
     */
    @Nullable
    private volatile StageType prevStage;

    /**
     * The current stage of the game.
     */
    @Nullable
    private volatile StageType currentStage;

    /**
     * The next stage of the game.
     */
    @Nullable
    private volatile StageType nextStage;

    /**
     * The javaFX container.
     */
    @Nullable
    private JmeFxContainer fxContainer;

    private GameClient() {
        this.userAccount = new UserAccount();
        this.lock = new StampedLock();
        this.waitedUpdatersGeometriesCount = new AtomicInteger();
        this.startedUpdatersGeometriesCount = new AtomicInteger();
        this.waitState = new AtomicInteger();
        this.stopSceneRender = new AtomicInteger();
    }

    /**
     * Async lock rendering to do something.
     */
    public final long asyncLock() {
        return lock.readLock();
    }

    /**
     * Async unlock rendering.
     */
    public final void asyncUnlock(final long stamp) {
        lock.unlockRead(stamp);
    }

    @Override
    public void destroy() {
        super.destroy();
        System.exit(0);
    }

    /**
     * @return the user account.
     */
    @NotNull
    public final UserAccount getUserAccount() {
        return userAccount;
    }

    @Override
    public Camera getCamera() {
        return super.getCamera();
    }

    /**
     * @return the current stage of the game.
     */
    @Nullable
    public final StageType getCurrentStage() {
        return currentStage;
    }

    /**
     * @param currentStage the current stage of the game.
     */
    public final void setCurrentStage(@Nullable final StageType currentStage) {
        this.currentStage = currentStage;
    }

    /**
     * @return the javaFX container.
     */
    @Nullable
    public JmeFxContainer getFxContainer() {
        return fxContainer;
    }

    /**
     * @return the next stage of the game.
     */
    @Nullable
    public StageType getNextStage() {
        return nextStage;
    }

    /**
     * @param nextStage the next stage of the game.
     */
    public void setNextStage(@Nullable final StageType nextStage) {
        this.nextStage = nextStage;
    }

    /**
     * @return the prev stage of the game.
     */
    @Nullable
    public StageType getPrevStage() {
        return prevStage;
    }

    /**
     * @param prevStage the prev stage of the game.
     */
    public void setPrevStage(@Nullable final StageType prevStage) {
        this.prevStage = prevStage;
    }

    /**
     * Go to the stage.
     *
     * @param stageType the new stage.
     */
    public void gotoStage(@NotNull final StageType stageType) {
        if (getCurrentStage() == stageType) return;
        setNextStage(stageType);
        Platform.runLater(new SwitchStateTask(stageType));
    }

    /**
     * @return true if the scene render was stopped.
     */
    public boolean isStoppedSceneRender() {
        return stopSceneRender.get() > 0;
    }

    @Override
    public void restart() {

        final JmeFxContainer fxContainer = getFxContainer();
        //TODO final AtomicInteger waitCount = fxContainer.getWaitCount();
        //waitCount.incrementAndGet();

        super.restart();
    }

    @Override
    public void simpleInitApp() {

        final OperatingSystem system = new OperatingSystem();

        LOGGER.info(this, "OS: " + system.getDistribution());

        final AudioRenderer audioRenderer = getAudioRenderer();
        audioRenderer.setEnvironment(new Environment(Environment.Garage));

        final Node guiNode = getGuiNode();
        guiNode.detachAllChildren();

        final UbuntuCursorProvider cursorDisplayProvider = new UbuntuCursorProvider(this, assetManager, inputManager);

        for (final CursorType type : CursorType.values()) {
            cursorDisplayProvider.setup(type);
        }

        final GameConfig config = GameConfig.getInstance();

        cam.setFrustumPerspective(40, (float) cam.getWidth() / cam.getHeight(), 1f, config.getViewDistance());

        flyCam.setDragToRotate(true);
        flyCam.setEnabled(false);

        final FilterPostProcessor postProcessor = new FilterPostProcessor(assetManager);
        postProcessor.initialize(renderManager, viewPort);

        viewPort.addProcessor(postProcessor);

        ExecutorManager.getInstance();

        InitializeManager.register(GameTaskManager.class);
        InitializeManager.register(UpdateObjectManager.class);
        InitializeManager.register(ClassManager.class);
        InitializeManager.register(Network.class);
        InitializeManager.initialize();

        fxContainer = JmeFxContainer.install(this, guiNode, cursorDisplayProvider);

        gotoStage(StageType.LOGIN_STAGE);
    }

    /**
     * Sync lock rendering to do something.
     */
    public final long syncLock() {
        return lock.writeLock();
    }

    /**
     * Sync unlock rendering.
     */
    public final void syncUnlock(final long stamp) {
        lock.unlockWrite(stamp);
    }

    /**
     * Notify about finished updating geometries from other thread.
     */
    public void finishUpdateGeometries() {
        if (startedUpdatersGeometriesCount.decrementAndGet() == 0) {
            ConcurrentUtils.notifyAll(waitState);
        }
    }

    /**
     * Notify about started updating geometries from other thread.
     */
    public void startUpdateGeometries() {
        synchronized (waitedUpdatersGeometriesCount) {
            waitedUpdatersGeometriesCount.incrementAndGet();
            ConcurrentUtils.waitInSynchronize(waitedUpdatersGeometriesCount);
        }
    }

    @Override
    public void update() {

        final JmeFxContainer fxContainer = requireNonNull(getFxContainer());

        synchronized (waitState) {
            if (waitedUpdatersGeometriesCount.get() > 0) {
                synchronized (waitedUpdatersGeometriesCount) {
                    startedUpdatersGeometriesCount.getAndSet(waitedUpdatersGeometriesCount.getAndSet(0));
                    ConcurrentUtils.notifyAllInSynchronize(waitedUpdatersGeometriesCount);
                }
                ConcurrentUtils.waitInSynchronize(waitState, 10000);
            }
        }

        final long stamp = syncLock();
        try {

            final GameThreadExecutor gameThreadExecutor = GameThreadExecutor.getInstance();
            gameThreadExecutor.execute();

            if (paused) {
                return;
            }

            if (fxContainer.isNeedWriteToJME()) {
                fxContainer.writeToJME();
            }

            super.update();

        } catch (final ArrayIndexOutOfBoundsException | NullPointerException e) {
            LOGGER.warning(e);
            System.exit(1);
        } catch (final IllegalStateException e) {
            LOGGER.warning(e);
        } finally {
            syncUnlock(stamp);
        }

        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());
    }
}
