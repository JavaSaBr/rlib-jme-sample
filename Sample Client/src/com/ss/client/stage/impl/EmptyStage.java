package com.ss.client.stage.impl;

import com.ss.client.ui.scene.UISceneType;
import org.jetbrains.annotations.NotNull;

/**
 * The empty stage.
 *
 * @author JavaSaBr
 */
public class EmptyStage extends AbstractStage {

    @NotNull
    @Override
    public UISceneType getUISceneType() {
        return UISceneType.EMPTY_SCENE;
    }
}
