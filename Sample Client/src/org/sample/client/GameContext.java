package org.sample.client;

import static java.util.Objects.requireNonNull;
import com.jme3.system.AppSettings;
import com.jme3.system.NativeLibraryLoader;
import com.jme3.system.lwjgl.LwjglDisplay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * The render context.
 *
 * @author JavaSaBr
 */
public final class GameContext extends LwjglDisplay {

    @NotNull
    protected static final Logger LOGGER = LoggerManager.getLogger(GameContext.class);

    /**
     * The game thread.
     */
    @Nullable
    private GameThread thread;

    @Override
    public void create(final boolean waitFor) {

        if ("LWJGL".equals(settings.getAudioRenderer())) {
            NativeLibraryLoader.loadNativeLibrary("openal-lwjgl3", true);
        }

        NativeLibraryLoader.loadNativeLibrary("lwjgl3", true);
        NativeLibraryLoader.loadNativeLibrary("glfw-lwjgl3", true);
        NativeLibraryLoader.loadNativeLibrary("jemalloc-lwjgl3", true);
        NativeLibraryLoader.loadNativeLibrary("jinput", true);
        NativeLibraryLoader.loadNativeLibrary("jinput-dx8", true);

        if (created.get()) {
            return;
        }

        thread = new GameThread(this);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setName("LWJGL Renderer Thread");
        thread.start();

        if (waitFor) {
            waitFor(true);
        }
    }

    /**
     * @return the custom game thread.
     */
    @NotNull
    public GameThread getThread() {
        return requireNonNull(thread);
    }

    @Override
    protected void createContext(final AppSettings settings) {
        settings.setRenderer(AppSettings.LWJGL_OPENGL3);
        super.createContext(settings);
    }
}
