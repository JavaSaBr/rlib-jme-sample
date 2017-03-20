package com.ss.client.ui.scene.builder.impl;

import com.ss.client.ui.component.login.LoginAuthPanel;
import com.ss.client.ui.scene.UIScene;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import rlib.ui.util.FXUtils;

/**
 * The implementation of login scene builder.
 *
 * @author JavaSaBr
 */
public class LoginUISceneBuilder extends AbstractUISceneBuilder {

    @Override
    protected void fillScene(@NotNull final UIScene scene, @NotNull final StackPane root) {

        final LoginAuthPanel loginAuthPanel = new LoginAuthPanel();

        FXUtils.addToPane(loginAuthPanel, root);
    }
}
