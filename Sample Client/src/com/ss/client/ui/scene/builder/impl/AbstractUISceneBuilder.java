package com.ss.client.ui.scene.builder.impl;

import static javafx.geometry.Pos.CENTER;
import static javafx.scene.paint.Color.TRANSPARENT;
import static rlib.ui.util.FXUtils.bindFixedSize;
import com.ss.client.ui.scene.UIScene;
import com.ss.client.ui.scene.builder.UISceneBuilder;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of scene builder.
 *
 * @author JavaSaBr
 */
public abstract class AbstractUISceneBuilder implements UISceneBuilder {

    @NotNull
    @Override
    public UIScene build() {

        final Group root = new Group();

        final UIScene scene = new UIScene(root);
        scene.setFill(TRANSPARENT);

        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.add(CSS_FILE_BASE);
        stylesheets.add(CSS_FILE_EXTERNAL);
        stylesheets.add(CSS_FILE_CUSTOM_IDS);
        stylesheets.add(CSS_FILE_CUSTOM_CLASSES);

        final StackPane container = scene.getContainer();
        container.setAlignment(CENTER);

        fillScene(scene, container);

        bindFixedSize(container, scene.widthProperty(), scene.heightProperty());

        scene.notifyFinishBuild();

        return scene;
    }

    /**
     * Fill scene of controls.
     */
    protected abstract void fillScene(@NotNull final UIScene scene, @NotNull final StackPane root);
}
