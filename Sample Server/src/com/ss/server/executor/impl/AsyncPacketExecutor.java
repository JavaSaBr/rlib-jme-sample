package com.ss.server.executor.impl;

import com.ss.server.LocalObjects;
import com.ss.server.ServerThread;
import org.jetbrains.annotations.NotNull;
import rlib.concurrent.GroupThreadFactory;
import rlib.concurrent.executor.impl.ThreadPoolTaskExecutor;

/**
 * The implementation of async game task executor.
 *
 * @author JavaSaBr
 */
public class AsyncPacketExecutor extends ThreadPoolTaskExecutor<LocalObjects> {

    @NotNull
    public static AsyncPacketExecutor create(final int poolSize, final int packetSize) {

        final GroupThreadFactory factory = new GroupThreadFactory(AsyncPacketExecutor.class.getSimpleName(),
                ServerThread.class, Thread.NORM_PRIORITY);

        return new AsyncPacketExecutor(factory, poolSize, packetSize);
    }

    private AsyncPacketExecutor(@NotNull final GroupThreadFactory threadFactory, final int poolSize,
                                final int packetSize) {
        super(threadFactory, poolSize, packetSize);
    }

    @NotNull
    @Override
    protected LocalObjects getLocalObjects(@NotNull final Thread thread) {
        return ((ServerThread) thread).getLocal();
    }
}
