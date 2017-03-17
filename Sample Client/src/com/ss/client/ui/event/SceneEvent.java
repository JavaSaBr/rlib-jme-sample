package com.ss.client.ui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.util.pools.Reusable;

/**
 * The base implementation of javaFX event in the game.
 *
 * @author JavaSaBr
 */
public class SceneEvent extends Event implements Reusable {

    private static final long serialVersionUID = 6827900349094865635L;

    @NotNull
    public static final EventType<SceneEvent> EVENT_TYPE;

    static {
        synchronized (Event.class) {
            EVENT_TYPE = new EventType<>(SceneEvent.class.getSimpleName());
        }
    }

    public SceneEvent(@NotNull final Object source, @NotNull final EventTarget target,
                      @NotNull final EventType<? extends Event> eventType) {
        super(source, target, eventType);
    }

    public SceneEvent(@NotNull final EventType<? extends Event> eventType) {
        super(eventType);
    }

    /**
     * @param target the event target.
     */
    public void setTarget(@Nullable final EventTarget target) {
        this.target = target;
    }

    @Override
    public void free() {

        final EventTarget target = getTarget();

        if (target instanceof Reusable) {
            ((Reusable) target).release();
        }

        setTarget(null);
    }

    @Override
    public void reuse() {
        this.consumed = false;
    }
}
