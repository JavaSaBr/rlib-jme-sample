package com.ss.client;

import com.ss.client.util.LocalObjects;
import org.jetbrains.annotations.NotNull;
import com.ss.client.util.LocalObjects;

/**
 * The custom game thread.
 *
 * @author JavaSaBr
 */
public class GameThread extends Thread {

    /**
     * The thread local objects.
     */
    @NotNull
    private final LocalObjects localObects;

    public GameThread() {
        this.localObects = new LocalObjects();
    }

    public GameThread(@NotNull final Runnable target) {
        super(target);
        this.localObects = new LocalObjects();
    }

    public GameThread(@NotNull final ThreadGroup group, @NotNull final Runnable target, @NotNull final String name) {
        super(group, target, name);
        this.localObects = new LocalObjects();
    }

    /**
     * @return the thread local objects..
     */
    @NotNull
    public LocalObjects getLocalObects() {
        return localObects;
    }
}
