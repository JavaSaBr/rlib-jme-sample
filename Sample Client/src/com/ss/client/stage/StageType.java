package com.ss.client.stage;

import com.ss.client.stage.impl.EmptyStage;
import com.ss.client.stage.impl.HangarStage;
import com.ss.client.stage.impl.LoginStage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.util.ClassUtils;

/**
 * The list of available stages of the game.
 *
 * @author JavaSaBr
 */
public enum StageType {
    LOGIN_STAGE(LoginStage.class),
    HANGAR_STAGE(HangarStage.class),
    GAME_STAGE(EmptyStage.class);

    /**
     * The implementation of the stage.
     */
    @NotNull
    private final Class<? extends Stage> implementation;

    /**
     * The built stage.
     */
    @Nullable
    private volatile Stage stage;

    private StageType(@NotNull final Class<? extends Stage> implementation) {
        this.implementation = implementation;
    }

    /**
     * Get the built stage.
     *
     * @return the built stage.
     */
    @NotNull
    public Stage getStage() {

        if (stage == null) {
            synchronized (this) {
                if (stage == null) {
                    stage = ClassUtils.newInstance(implementation);
                }
            }
        }

        return stage;
    }
}
