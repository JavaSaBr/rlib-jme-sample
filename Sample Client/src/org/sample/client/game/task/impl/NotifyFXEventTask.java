package org.sample.client.game.task.impl;

import javafx.event.Event;
import org.jetbrains.annotations.NotNull;
import org.sample.client.game.task.LimitedGameTask;
import org.sample.client.manager.GameTaskManager;
import org.sample.client.ui.event.FXEventManager;
import org.sample.client.util.LocalObjects;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

/**
 * The reusable task to notify javaFx about new event in the javaFX thread.
 *
 * @author JavaSaBr
 */
public class NotifyFXEventTask extends LimitedGameTask {

    @NotNull
    private static final FXEventManager FX_EVENT_MANAGER = FXEventManager.getInstance();

    @NotNull
    private static final ReusablePool<NotifyFXEventTask> POOL = PoolFactory.newConcurrentAtomicARSWLockReusablePool(NotifyFXEventTask.class);

    @NotNull
    public static NotifyFXEventTask getInstance(@NotNull final Event event) {

        final NotifyFXEventTask task = POOL.take(NotifyFXEventTask::new);
        task.event = event;

        return task;
    }

    /**
     * The new event.
     */
    @NotNull
    private Event event;

    private NotifyFXEventTask() {
        super(GameTaskManager.PROP_EXECUTE_LIMIT);
    }

    @Override
    protected boolean executeImpl(final LocalObjects local, final long currentTime) {
        FX_EVENT_MANAGER.notify(event);
        return true;
    }

    @Override
    public void release() {
        POOL.put(this);
    }

    @Override
    public String toString() {
        return "NotifyFXEventTask{" + "event=" + event + '}';
    }
}
