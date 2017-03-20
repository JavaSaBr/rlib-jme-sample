package com.ss.client.ui.ui;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.InputManager;
import com.jme3x.jfx.cursor.CursorDisplayProvider;
import com.jme3x.jfx.cursor.proton.ProtonCursorProvider;
import com.ss.client.manager.GameTaskManager;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.CursorType;
import org.jetbrains.annotations.NotNull;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.dictionary.ConcurrentObjectDictionary;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.DictionaryUtils;
import rlib.util.dictionary.ObjectDictionary;

/**
 * Provider Ubuntu cursors.
 *
 * @author JavaSaBr
 */
public class UbuntuCursorProvider implements CursorDisplayProvider {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(ProtonCursorProvider.class);

    @NotNull
    private static final ConcurrentObjectDictionary<CursorType, JmeCursor> CACHE =
            DictionaryFactory.newConcurrentAtomicObjectDictionary();

    @NotNull
    private AssetManager assetManager;

    @NotNull
    private InputManager inputManager;

    @NotNull
    private Application app;

    public UbuntuCursorProvider(@NotNull final Application app, @NotNull final AssetManager assetManager,
                                @NotNull final InputManager inputManager) {
        this.assetManager = assetManager;
        this.inputManager = inputManager;
        this.app = app;
        assetManager.registerLocator("", ClasspathLocator.class);
    }

    @Override
    public void setup(@NotNull final CursorType cursorType) {

        JmeCursor loaded = null;

        switch (cursorType) {
            case CLOSED_HAND:
            case IMAGE:
            case CROSSHAIR:
            case DEFAULT:
            case OPEN_HAND:
            case DISAPPEAR:
            case NONE:
                loaded = (JmeCursor) assetManager.loadAsset("ui/cursor/ubuntu/Ubuntu Normal Select.cur");
                break;
            case HAND:
                loaded = (JmeCursor) assetManager.loadAsset("ui/cursor/ubuntu/Ubuntu link.cur");
                break;
            case MOVE:
                loaded = (JmeCursor) assetManager.loadAsset("ui/cursor/ubuntu/The Real Ubuntu Move.cur");
            case TEXT:
                loaded = (JmeCursor) assetManager.loadAsset("ui/cursor/ubuntu/Ubuntu Text.cur");
                break;
            case SE_RESIZE:
            case SW_RESIZE:
                loaded = (JmeCursor) assetManager.loadAsset("ui/cursor/ubuntu/Ubuntu diagonal 1.cur");
                break;
            case NW_RESIZE:
            case NE_RESIZE:
                loaded = (JmeCursor) assetManager.loadAsset("ui/cursor/ubuntu/Ubuntu diagonal 2.cur");
                break;
            case N_RESIZE:
            case H_RESIZE:
            case S_RESIZE:
            case V_RESIZE:
                loaded = (JmeCursor) assetManager.loadAsset("ui/cursor/ubuntu/Ubuntu vertical.cur");
                break;
            case E_RESIZE:
            case W_RESIZE:
                loaded = (JmeCursor) assetManager.loadAsset("ui/cursor/ubuntu/Ubuntu horozontal.cur");
                break;
            case WAIT:
                loaded = (JmeCursor) assetManager.loadAsset("ui/cursor/ubuntu/Ubuntu Busy.ani");
                break;
        }

        if (loaded != null) {
            DictionaryUtils.runInWriteLock(CACHE, cursorType, loaded, ObjectDictionary::put);
        }
    }

    @Override
    public void showCursor(@NotNull final CursorFrame cursorFrame) {

        CursorType cursorType = cursorFrame.getCursorType();
        JmeCursor jmeCursor = DictionaryUtils.getInReadLock(CACHE, cursorType, ObjectDictionary::get);

        if (jmeCursor == null) {
            LOGGER.warning("Unkown Cursor! " + cursorType);
            jmeCursor = DictionaryUtils.getInReadLock(CACHE, CursorType.DEFAULT, ObjectDictionary::get);
        }

        final JmeCursor toShow = jmeCursor;

        final GameTaskManager gameTaskManager = GameTaskManager.getInstance();
        gameTaskManager.addGameTask((local, currentTime) -> {
            inputManager.setMouseCursor(toShow);
            return true;
        });
    }
}