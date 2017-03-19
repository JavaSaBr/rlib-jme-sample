package com.ss.server.executor.impl;

import com.ss.server.LocalObjects;
import com.ss.server.ServerThread;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.concurrent.executor.impl.SingleThreadTaskExecutor;

/**
 * The implementation of sync game task executor.
 *
 * @author JavaSaBr
 */
public class SyncPacketExecutor extends SingleThreadTaskExecutor<LocalObjects> {

    @NotNull
    private static final String EXECUTOR_NAME = SyncPacketExecutor.class.getName();

    public SyncPacketExecutor() {
        super(ServerThread.class, Thread.NORM_PRIORITY + 1, EXECUTOR_NAME, null);
    }

    @NotNull
    @Override
    protected LocalObjects check(@Nullable final LocalObjects local, @NotNull final Thread thread) {
        return ((ServerThread) thread).getLocal();
    }
}
