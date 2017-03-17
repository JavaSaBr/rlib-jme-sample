package com.ss.client.manager;

import com.ss.client.game.task.impl.NotifyFXEventTask;
import com.ss.client.ui.event.ConsumableEvent;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import org.jetbrains.annotations.NotNull;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.ObjectDictionary;
import rlib.util.pools.Reusable;

/**
 * The manager to broadcast FX Events.
 *
 * @author JavaSaBr
 */
public class FXEventManager {

    @NotNull
    private static final FXEventManager INSTANCE = new FXEventManager();

    @NotNull
    public static FXEventManager getInstance() {
        return INSTANCE;
    }

    /**
     * The table of listeners.
     */
    @NotNull
    private final ObjectDictionary<EventType<? extends Event>, Array<EventHandler<? super Event>>> eventHandlers;

    private FXEventManager() {
        this.eventHandlers = DictionaryFactory.newObjectDictionary();
    }

    /**
     * Add new event handler.
     *
     * @param eventType    the event type.
     * @param eventHandler the event handler.
     */
    public void addEventHandler(@NotNull final EventType<? extends Event> eventType,
                                @NotNull final EventHandler<? super Event> eventHandler) {

        final Array<EventHandler<? super Event>> handlers = eventHandlers.get(eventType,
                () -> ArrayFactory.newArray(EventHandler.class));

        handlers.add(eventHandler);
    }

    /**
     * Notify the event.
     *
     * @param event the event.
     */
    public void notify(@NotNull final Event event) {
        if (Platform.isFxApplicationThread()) {
            notifyImpl(event);
        } else {
            final GameTaskManager taskManager = GameTaskManager.getInstance();
            taskManager.addFXTask(NotifyFXEventTask.getInstance(event));
        }
    }

    /**
     * Notify the event in the javaFX thread.
     */
    private void notifyImpl(@NotNull final Event event) {

        for (EventType<? extends Event> eventType = event.getEventType();
             eventType != null; eventType = eventType.getSuperType()) {

            final Array<EventHandler<? super Event>> handlers = eventHandlers.get(eventType);
            if (handlers == null || handlers.isEmpty()) continue;

            handlers.forEach(event, EventHandler::handle);
        }

        if (event instanceof ConsumableEvent && !event.isConsumed()) {
            final GameTaskManager taskManager = GameTaskManager.getInstance();
            taskManager.addFXTask(NotifyFXEventTask.getInstance(event));
        } else if (event instanceof Reusable) {
            ((Reusable) event).release();
        }
    }
}
