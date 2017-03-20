package com.ss.client.ui.event.impl;

import static java.util.Objects.requireNonNull;
import com.ss.client.network.server.AuthResultServerPacket.ResultType;
import com.ss.client.ui.event.SceneEvent;
import javafx.event.Event;
import javafx.event.EventType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

/**
 * The event to notify about receiving result of authentication.
 *
 * @author JavaSaBr
 */
public class AuthResultEvent extends SceneEvent {

    @NotNull
    private static final ReusablePool<AuthResultEvent> POOL = PoolFactory.newConcurrentAtomicARSWLockReusablePool(AuthResultEvent.class);

    @NotNull
    public static final EventType<SceneEvent> EVENT_TYPE;

    static {
        synchronized (Event.class) {
            EVENT_TYPE = new EventType<>(SceneEvent.EVENT_TYPE, AuthResultEvent.class.getSimpleName());
        }
    }

    @NotNull
    public static AuthResultEvent newInstance(@NotNull final ResultType resultType) {
        final AuthResultEvent event = POOL.take(AuthResultEvent::new);
        event.setResultType(resultType);
        return event;
    }

    /**
     * The result type.
     */
    @Nullable
    private volatile ResultType resultType;

    private AuthResultEvent() {
        super(EVENT_TYPE);
    }

    /**
     * @return the result type.
     */
    @NotNull
    public ResultType getResultType() {
        return requireNonNull(resultType);
    }

    /**
     * @param resultType the result type.
     */
    private void setResultType(@Nullable final ResultType resultType) {
        this.resultType = resultType;
    }

    @Override
    public void free() {
        super.free();
        resultType = null;
    }
}
