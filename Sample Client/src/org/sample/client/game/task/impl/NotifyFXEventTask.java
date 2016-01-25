package org.sample.client.game.task.impl;

import javafx.event.Event;
import org.sample.client.game.task.LimitedGameTask;
import org.sample.client.manager.GameTaskManager;
import org.sample.client.ui.event.FXEventManager;
import org.sample.client.util.LocalObjects;
import rlib.util.pools.Foldable;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.PoolFactory;

/**
 * Задача по отправки события для FX UI.
 *
 * @author Ronn
 */
public class NotifyFXEventTask extends LimitedGameTask implements Foldable {

    private static final FXEventManager FX_EVENT_MANAGER = FXEventManager.getInstance();

    private static final FoldablePool<NotifyFXEventTask> POOL = PoolFactory.newAtomicFoldablePool(NotifyFXEventTask.class);

    public static final NotifyFXEventTask getInstance(final Event event) {

        NotifyFXEventTask task = POOL.take();

        if (task == null) {
            task = new NotifyFXEventTask();
        }

        task.event = event;

        return task;
    }

    /**
     * Отправляемое событие.
     */
    private Event event;

    public NotifyFXEventTask() {
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
        return "NotifyFXEventTask{" +
                "event=" + event +
                '}';
    }
}
