package com.ss.client;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.Environment;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3x.jfx.JmeFxContainer;
import com.jme3x.jfx.cursor.proton.ProtonCursorProvider;
import com.jme3x.jfx.util.os.OperatingSystem;
import com.ss.client.config.CommandLineConfig;
import com.ss.client.config.Config;
import com.ss.client.config.GameConfig;
import com.ss.client.config.ScreenSize;
import com.ss.client.executor.impl.GameThreadExecutor;
import com.ss.client.game.task.SwitchStateTask;
import com.ss.client.jme.post.PostEffect;
import com.ss.client.jme.post.effect.BloomEffect;
import com.ss.client.jme.post.effect.FXAAEffect;
import com.ss.client.manager.ExecutorManager;
import com.ss.client.manager.GameTaskManager;
import com.ss.client.manager.UpdateObjectManager;
import com.ss.client.model.impl.UserAccount;
import com.ss.client.network.Network;
import com.ss.client.stage.StageType;
import com.ss.client.ui.util.UIUtils;
import com.sun.javafx.cursor.CursorType;
import javafx.application.Platform;
import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerLevel;
import rlib.logging.LoggerManager;
import rlib.logging.impl.FolderFileListener;
import rlib.manager.InitializeManager;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.StampedLock;
import java.util.logging.Level;

/**
 * Инициализирующий класс игры.
 *
 * @author Ronn
 */
public class SampleGame extends SimpleApplication {

    private static final Logger LOGGER = LoggerManager.getLogger(SampleGame.class);

    private static final SampleGame GAME = new SampleGame();

    public static SampleGame getInstance() {
        return GAME;
    }

    public static void start(final String[] args) throws IOException {

        // фикс рендера шрифтов в FX
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");

        // настройки для JavaFX
        System.setProperty("prism.vsync", "true");
        System.setProperty("javafx.animation.fullspeed", "false");
        System.setProperty("prism.cacheshapes", "true");

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

    protected static void configureLogger() {

        // выключаем стандартный логгер
        java.util.logging.Logger.getLogger("").setLevel(Level.SEVERE);

        // настраиваем логгер
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

    public static final long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * Список пост эффектов в игре.
     */
    private final Array<PostEffect> postEffects;

    /**
     * Синхронизатор.
     */
    private final StampedLock lock;

    /**
     * Аккаунт пользователя.
     */
    private final UserAccount userAccount;

    /**
     * Счетчик ожидающих потоков для обновлениния геометрии.
     */
    private final AtomicInteger waitUpdateGeometrySync;

    /**
     * Счетчик исполняющих потоков обновления геометрии.
     */
    private final AtomicInteger executeUpdateGeometrySync;

    /**
     * Синхронизатор ожидания.
     */
    private final AtomicInteger waitState;

    /**
     * Остановлена ли отрисовка сцены.
     */
    private final AtomicInteger stopSceneRender;

    /**
     * Предыдущая стадия игры.
     */
    private volatile StageType prevStage;

    /**
     * Текущая стадия игры.
     */
    private volatile StageType currentStage;

    /**
     * Следующая стадия игры.
     */
    private volatile StageType nextStage;

    /**
     * Контейнер UI JavaFX.
     */
    private JmeFxContainer fxContainer;

    private SampleGame() {
        this.postEffects = ArrayFactory.newArray(PostEffect.class);
        this.userAccount = new UserAccount();
        this.lock = new StampedLock();
        this.waitUpdateGeometrySync = new AtomicInteger();
        this.executeUpdateGeometrySync = new AtomicInteger();
        this.waitState = new AtomicInteger();
        this.stopSceneRender = new AtomicInteger();
    }

    /**
     * Блокировка рендера для каких-то асинхронных действий.
     */
    public final long asyncLock() {
        return lock.readLock();
    }

    /**
     * Разблокировка рендера.
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
     * @return аккаунт пользователя.
     */
    public final UserAccount getUserAccount() {
        return userAccount;
    }

    @Override
    public Camera getCamera() {
        return super.getCamera();
    }

    /**
     * @return текущая стадия игры.
     */
    public final StageType getCurrentStage() {
        return currentStage;
    }

    /**
     * @param currentStage текущая стадия игры.
     */
    public final void setCurrentStage(final StageType currentStage) {
        this.currentStage = currentStage;
    }

    /**
     * @return счетчик исполняющих потоков обновления геометрии.
     */
    public AtomicInteger getExecuteUpdateGeometrySync() {
        return executeUpdateGeometrySync;
    }

    /**
     * @return контейнер UI JavaFX.
     */
    public JmeFxContainer getFxContainer() {
        return fxContainer;
    }

    /**
     * @return следующая стадия игры.
     */
    public StageType getNextStage() {
        return nextStage;
    }

    /**
     * @param nextStage следующая стадия игры.
     */
    public void setNextStage(StageType nextStage) {
        this.nextStage = nextStage;
    }

    /**
     * @return список пост эффектов в игре.
     */
    public Array<PostEffect> getPostEffects() {
        return postEffects;
    }

    /**
     * @return предыдущая стадия игры.
     */
    public StageType getPrevStage() {
        return prevStage;
    }

    /**
     * @param prevStage предыдущая стадия игры.
     */
    public void setPrevStage(StageType prevStage) {
        this.prevStage = prevStage;
    }

    /**
     * @return синхронизатор ожидания.
     */
    public AtomicInteger getWaitState() {
        return waitState;
    }

    /**
     * @return счетчик ожидающих потоков для обновлениния геометрии.
     */
    public AtomicInteger getWaitUpdateGeometrySync() {
        return waitUpdateGeometrySync;
    }

    /**
     * Переходим на указанную стадию.
     *
     * @param stageType ид стадии игры.
     */
    public void gotoStage(final StageType stageType) {

        if (getCurrentStage() == stageType) {
            return;
        }

        setNextStage(stageType);

        Platform.runLater(new SwitchStateTask(stageType));
    }

    /**
     * @return остановлена ли отрисовка сцены.
     */
    public boolean isStoppedSceneRender() {
        return stopSceneRender.get() > 0;
    }

    @Override
    public void restart() {

        final JmeFxContainer fxContainer = getFxContainer();
        final AtomicInteger waitCount = fxContainer.getWaitCount();
        waitCount.incrementAndGet();

        super.restart();
    }

    @Override
    public void simpleInitApp() {

        final OperatingSystem system = new OperatingSystem();

        LOGGER.info(this, "OS: " + system.getDistribution());

        final AudioRenderer audioRenderer = getAudioRenderer();
        // Так указывается окружение для звуков
        audioRenderer.setEnvironment(new Environment(Environment.Garage));

        final Node guiNode = getGuiNode();
        guiNode.detachAllChildren();

        final ProtonCursorProvider cursorDisplayProvider = new ProtonCursorProvider(this, assetManager, inputManager);

        for (final CursorType type : CursorType.values()) {
            cursorDisplayProvider.setup(type);
        }

        final GameConfig config = GameConfig.getInstance();

        // устанавливаем нужный нам обзор в 40 градусов
        cam.setFrustumPerspective(40, (float) cam.getWidth() / cam.getHeight(), 1f, config.getViewDistance());

        flyCam.setDragToRotate(true);
        flyCam.setEnabled(false);

        final FilterPostProcessor postProcessor = new FilterPostProcessor(assetManager);
        postProcessor.initialize(renderManager, viewPort);

        viewPort.addProcessor(postProcessor);

        final Array<PostEffect> postEffects = getPostEffects();
        postEffects.add(BloomEffect.bind(postProcessor));
        postEffects.add(FXAAEffect.bind(postProcessor));

        UIUtils.overrideTooltipBehavior(100, 5000, 0);

        ExecutorManager.getInstance();

        InitializeManager.register(GameTaskManager.class);
        InitializeManager.register(UpdateObjectManager.class);
        InitializeManager.register(Network.class);
        InitializeManager.initialize();

        fxContainer = JmeFxContainer.install(this, guiNode, true, cursorDisplayProvider);

        gotoStage(StageType.LOGIN_STAGE);
    }

    /**
     * Блокировать синхронизированную область.
     */
    public final long syncLock() {
        return lock.writeLock();
    }

    /**
     * Разблокировать синхронизированную область.
     */
    public final void syncUnlock(final long stamp) {
        lock.unlockWrite(stamp);
    }

    /**
     * Попытка произвести синхронизирующую блокировку.
     */
    public long trySyncLock() {
        return lock.tryWriteLock();
    }

    /**
     * Уведомлпение о завершении обновлении геометрии.
     */
    public void updateGeomEnd() {

        final AtomicInteger executeUpdateGeometrySync = getExecuteUpdateGeometrySync();

        if (executeUpdateGeometrySync.decrementAndGet() == 0) {
            ConcurrentUtils.notifyAll(getWaitState());
        }
    }

    /**
     * Ожидание возможности начать обновлять геометрию.
     */
    public void updateGeomStart() {

        final AtomicInteger waitUpdateGeometrySync = getWaitUpdateGeometrySync();

        synchronized (waitUpdateGeometrySync) {
            waitUpdateGeometrySync.incrementAndGet();
            ConcurrentUtils.waitInSynchronize(waitUpdateGeometrySync);
        }
    }

    @Override
    public void update() {

        final JmeFxContainer fxContainer = getFxContainer();

        final AtomicInteger waitUpdateGeometrySync = getWaitUpdateGeometrySync();
        final AtomicInteger executeUpdateGeometrySync = getExecuteUpdateGeometrySync();
        final AtomicInteger waitState = getWaitState();

        synchronized (waitState) {

            if (waitUpdateGeometrySync.get() > 0) {

                synchronized (waitUpdateGeometrySync) {
                    executeUpdateGeometrySync.getAndSet(waitUpdateGeometrySync.getAndSet(0));
                    ConcurrentUtils.notifyAllInSynchronize(waitUpdateGeometrySync);
                }

                ConcurrentUtils.waitInSynchronize(waitState, 10000);
            }

            //TODO здесь надо обновлять камеру
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
