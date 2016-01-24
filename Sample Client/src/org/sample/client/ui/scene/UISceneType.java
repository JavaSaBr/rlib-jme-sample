package org.sample.client.ui.scene;

import org.sample.client.ui.scene.builder.UISceneBuilder;
import org.sample.client.ui.scene.builder.impl.LoginUISceneBuilder;
import rlib.util.ClassUtils;

/**
 * Created by ronn on 23.01.16.
 */
public enum UISceneType {
    LOGIN_SCENE(LoginUISceneBuilder.class),
    HANGAR_SCENE(null),
    GAME_SCENE(null);

    private final Class<? extends UISceneBuilder> implementation;

    private volatile UIScene scene;

    private UISceneType(final Class<? extends UISceneBuilder> implementation) {
        this.implementation = implementation;
    }

    public UIScene getScene() {

        if(scene == null) {
            synchronized (this) {
                if(scene == null) {
                    final UISceneBuilder builder = ClassUtils.newInstance(implementation);
                    scene = builder.build();
                }
            }
        }

        return scene;
    }
}
