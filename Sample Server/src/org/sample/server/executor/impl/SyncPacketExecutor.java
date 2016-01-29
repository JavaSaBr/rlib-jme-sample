package org.sample.server.executor.impl;

import org.sample.server.LocalObjects;
import org.sample.server.ServerThread;
import rlib.concurrent.executor.impl.SingleThreadTaskExecutor;


/**
 * Реализация синхронного исполнения клиентских пакетов.
 *
 * @author Ronn
 */
public class SyncPacketExecutor extends SingleThreadTaskExecutor<LocalObjects> {

    public static final String EXECUTOR_NAME = SyncPacketExecutor.class.getName();

    public SyncPacketExecutor() {
        super(ServerThread.class, Thread.NORM_PRIORITY + 1, EXECUTOR_NAME, null);
    }

    @Override
    protected LocalObjects check(final LocalObjects local, final Thread thread) {
        return ((ServerThread) thread).getLocal();
    }
}
