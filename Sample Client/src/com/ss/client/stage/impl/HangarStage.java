package com.ss.client.stage.impl;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.ss.client.ui.scene.UISceneType;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of hangar game stage.
 *
 * @author JavaSaBr
 */
public class HangarStage extends AbstractStage {

    /**
     * The scene node.
     */
    @NotNull
    private final Node scene;

    public HangarStage() {
        this.scene = (Node) ASSET_MANAGER.loadModel("/scene/hangar.j3o");
    }

    @NotNull
    @Override
    public UISceneType getUISceneType() {
        return UISceneType.HANGAR_SCENE;
    }

    @Override
    public void initialize(final AppStateManager stateManager, final Application app) {
        super.initialize(stateManager, app);

        final Node rootNode = GAME_CLIENT.getRootNode();
        rootNode.attachChild(scene);
    }

    @Override
    public void cleanup() {
        super.cleanup();

        final Node rootNode = GAME_CLIENT.getRootNode();
        rootNode.detachChild(scene);
    }
}
