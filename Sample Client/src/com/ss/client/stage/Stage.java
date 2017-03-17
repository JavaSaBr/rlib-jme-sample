package com.ss.client.stage;

import com.jme3.app.state.AppState;
import com.ss.client.ui.scene.UISceneType;
import com.ss.client.ui.scene.UISceneType;

/**
 * Created by ronn on 23.01.16.
 */
public interface Stage extends AppState {

    public UISceneType getUISceneType();
}
