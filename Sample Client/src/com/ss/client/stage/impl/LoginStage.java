package com.ss.client.stage.impl;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.ss.client.ui.scene.UISceneType;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ronn on 23.01.16.
 */
public class LoginStage extends AbstractStage {

    @NotNull
    @Override
    public UISceneType getUISceneType() {
        return UISceneType.LOGIN_SCENE;
    }

    @Override
    public void initialize(final AppStateManager stateManager, final Application app) {
        super.initialize(stateManager, app);

        //TODO
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }
}
