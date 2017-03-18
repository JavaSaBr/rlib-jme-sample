package com.ss.client.stage;

import com.jme3.app.state.AppState;
import com.ss.client.ui.scene.UISceneType;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a stage of the game.
 *
 * @author JavaSaBr
 */
public interface Stage extends AppState {

    /**
     * The type of UI scene to represent this stage.
     *
     * @return the type of UI scene.
     */
    @NotNull
    UISceneType getUISceneType();
}
