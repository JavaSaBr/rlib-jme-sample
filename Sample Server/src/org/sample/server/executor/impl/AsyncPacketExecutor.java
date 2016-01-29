package org.sample.server.executor.impl;

import org.sample.server.LocalObjects;
import org.sample.server.ServerThread;
import rlib.concurrent.GroupThreadFactory;
import rlib.concurrent.executor.impl.ThreadPoolTaskExecutor;

/**
 * Реализация асинхронного исполнителя клиентских пакетов.
 *
 * @author Ronn
 */
public class AsyncPacketExecutor extends ThreadPoolTaskExecutor<LocalObjects> {

    public static final AsyncPacketExecutor create(final int poolSize, final int packetSize) {
        final GroupThreadFactory factory = new GroupThreadFactory(AsyncPacketExecutor.class.getSimpleName(), ServerThread.class, Thread.NORM_PRIORITY);
        return new AsyncPacketExecutor(factory, poolSize, packetSize);
    }

    public AsyncPacketExecutor(final GroupThreadFactory threadFactory, final int poolSize, final int packetSize) {
        super(threadFactory, poolSize, packetSize);
    }

    @Override
    protected LocalObjects getLocalObjects(final Thread thread) {
        return ((ServerThread) thread).getLocal();
    }
}
