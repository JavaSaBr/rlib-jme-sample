package com.ss.client.ui.event;

/**
 * The interface to detect that event can be consumed.
 *
 * @author JavaSaBr
 */
public interface ConsumableEvent {

    /**
     * @return true if this event was finally handled.
     */
    boolean isConsumed();
}
