package com.ss.client.ui.scene;

import com.ss.client.ui.scene.builder.UISceneBuilder;
import com.ss.client.ui.scene.builder.impl.EmptyUISceneBuilder;
import com.ss.client.ui.scene.builder.impl.LoginUISceneBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.util.ClassUtils;

/**
 * The list of available UI scenes in the game.
 *
 * @author JavaSaBr
 */
public enum UISceneType {
    LOGIN_SCENE(LoginUISceneBuilder.class),
    HANGAR_SCENE(EmptyUISceneBuilder.class),
    GAME_SCENE(EmptyUISceneBuilder.class);

    /**
     * The implementation of UI scene builder.
     */
    @NotNull
    private final Class<? extends UISceneBuilder> implementation;

    /**
     * The built UI scene.
     */
    @Nullable
    private volatile UIScene scene;

    UISceneType(@NotNull final Class<? extends UISceneBuilder> implementation) {
        this.implementation = implementation;
    }

    /**
     * The built UI scene of this type.
     *
     * @return the UI scene.
     */
    @Nullable
    public UIScene getScene() {

        if (scene == null && implementation != null) {
            synchronized (this) {
                if (scene == null) {
                    final UISceneBuilder builder = ClassUtils.newInstance(implementation);
                    scene = builder.build();
                }
            }
        }

        return scene;
    }
}
