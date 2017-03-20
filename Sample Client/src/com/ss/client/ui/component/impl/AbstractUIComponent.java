package com.ss.client.ui.component.impl;

import com.ss.client.manager.FXEventManager;
import com.ss.client.network.Network;
import com.ss.client.ui.component.UIComponent;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;
import rlib.util.StringUtils;

/**
 * The base implementation of UI component.
 *
 * @author JavaSaBr
 */
public abstract class AbstractUIComponent extends GridPane implements UIComponent {

    @NotNull
    protected static final FXEventManager FX_EVENT_MANAGER = FXEventManager.getInstance();

    @NotNull
    protected static final Network NETWORK = Network.getInstance();

    public AbstractUIComponent() {

        final String cssId = getCssId();

        if (!StringUtils.isEmpty(cssId)) {
            setId(cssId);
        }

        createComponentsAndControls();
    }

    /**
     * Get a css id of this component.
     *
     * @return the css id.
     */
    @NotNull
    protected String getCssId() {
        return StringUtils.EMPTY;
    }

    protected void createComponentsAndControls() {
    }
}
