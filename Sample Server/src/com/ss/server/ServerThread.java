package com.ss.server;

import com.sun.istack.internal.NotNull;

/**
 * The implementation of the server thread.
 *
 * @author JavaSaBr
 */
public class ServerThread extends Thread {

    /**
     * The thread local objects.
     */
    @NotNull
    private final LocalObjects local;

    public ServerThread() {
        this.local = new LocalObjects(this);
    }

    public ServerThread(final Runnable target) {
        super(target);

        this.local = new LocalObjects(this);
    }

    public ServerThread(final Runnable target, final String name) {
        super(target, name);

        this.local = new LocalObjects(this);
    }

    public ServerThread(final ThreadGroup group, final Runnable target, final String name) {
        super(group, target, name);

        this.local = new LocalObjects(this);
    }

    /**
     * @return the thread local objects.
     */
    @NotNull
    public LocalObjects getLocal() {
        return local;
    }
}
