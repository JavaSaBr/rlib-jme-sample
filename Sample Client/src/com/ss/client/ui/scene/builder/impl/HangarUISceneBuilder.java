package com.ss.client.ui.scene.builder.impl;

import com.ss.client.ui.css.CSSClasses;
import com.ss.client.ui.scene.UIScene;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import rlib.ui.util.FXUtils;

/**
 * The implementation of hangar scene builder.
 *
 * @author JavaSaBr
 */
public class HangarUISceneBuilder extends AbstractUISceneBuilder {

    @Override
    protected void fillScene(@NotNull final UIScene scene, @NotNull final StackPane root) {

        final VBox box = new VBox();
        box.setAlignment(Pos.BOTTOM_CENTER);

        final Button button = new Button("Play");

        FXUtils.addClassTo(button, CSSClasses.BUTTON_SIZE_1);
        FXUtils.addToPane(button, box);
        FXUtils.addToPane(box, root);
    }
}
