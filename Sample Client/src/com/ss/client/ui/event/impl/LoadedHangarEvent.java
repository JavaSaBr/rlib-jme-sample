package com.ss.client.ui.event.impl;

import static java.util.Objects.requireNonNull;
import com.ss.client.model.player.Player;
import com.ss.client.ui.event.SceneEvent;
import javafx.event.Event;
import javafx.event.EventType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

/**
 * The event to notify about receiving data about hangar.
 *
 * @author JavaSaBr
 */
public class LoadedHangarEvent extends SceneEvent {

    @NotNull
    private static final ReusablePool<LoadedHangarEvent> POOL = PoolFactory.newConcurrentAtomicARSWLockReusablePool(LoadedHangarEvent.class);

    @NotNull
    public static final EventType<SceneEvent> EVENT_TYPE;

    static {
        synchronized (Event.class) {
            EVENT_TYPE = new EventType<>(SceneEvent.EVENT_TYPE, LoadedHangarEvent.class.getSimpleName());
        }
    }

    @NotNull
    public static LoadedHangarEvent newInstance(@NotNull final Player player) {
        final LoadedHangarEvent event = POOL.take(LoadedHangarEvent::new);
        event.setPlayer(player);
        return event;
    }

    /**
     * The loaded player.
     */
    @Nullable
    private volatile Player player;

    private LoadedHangarEvent() {
        super(EVENT_TYPE);
    }

    /**
     * @return the loaded player.
     */
    @NotNull
    public Player getPlayer() {
        return requireNonNull(player);
    }

    /**
     * @param player the loaded player.
     */
    private void setPlayer(@Nullable final Player player) {
        this.player = player;
    }

    @Override
    public void free() {
        super.free();
        player = null;
    }
}
