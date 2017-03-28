package com.ss.client.stage.impl;

import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.ss.client.GameClient;
import com.ss.client.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of a stage of the game.
 *
 * @author JavaSaBr
 */
public abstract class AbstractStage extends AbstractAppState implements Stage {

    @NotNull
    protected static final GameClient GAME_CLIENT = GameClient.getInstance();

    @NotNull
    protected static final AssetManager ASSET_MANAGER = GAME_CLIENT.getAssetManager();
}
