package org.sample.server;

/**
 * Модель серверного потока.
 *
 * @author Ronn
 */
public class ServerThread extends Thread {

    /**
     * Набор локальных объектов для серверного потока.
     */
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
     * @return набор локальных объектов для потока.
     */
    public LocalObjects getLocal() {
        return local;
    }
}
