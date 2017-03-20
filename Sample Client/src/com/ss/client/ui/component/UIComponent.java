package com.ss.client.ui.component;

import javafx.scene.input.KeyEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement UI component.
 *
 * @author JavaSaBr
 */
public interface UIComponent {

    /**
     * Notify about after activating UI stage.
     */
    default void notifyPostActivate() {
    }

    /**
     * Notify about before activating UI stage.
     */
    default void notifyPreActivate() {
    }

    /**
     * Notify about after deactivating UI stage.
     */
    default void notifyPostDeactivate() {
    }

    /**
     * Notify about before deactivating UI stage.
     */
    default void notifyPreDeactivate() {
    }

    /**
     * @return the component id.
     */
    default String getComponentId() {
        return null;
    }

    /**
     * Notify about key released event.
     *
     * @param event the event.
     */
    default void notifyKeyReleased(@NotNull final KeyEvent event) {
    }
}
