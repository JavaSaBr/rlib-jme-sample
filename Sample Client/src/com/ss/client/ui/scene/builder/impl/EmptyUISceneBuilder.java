package com.ss.client.ui.scene.builder.impl;

import com.ss.client.ui.scene.UIScene;
import com.ss.client.ui.scene.builder.UISceneBuilder;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation to fillScene empty scene.
 *
 * @author JavaSaBr
 */
public class EmptyUISceneBuilder implements UISceneBuilder {

    @NotNull
    @Override
    public UIScene build() {

        final Group root = new Group();

        final UIScene scene = new UIScene(root);
        scene.setFill(new Color(0, 0, 0, 0));

        return scene;
    }
}
