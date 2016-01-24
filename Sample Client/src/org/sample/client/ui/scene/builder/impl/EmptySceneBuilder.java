package org.sample.client.ui.scene.builder.impl;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import org.sample.client.ui.scene.UIScene;
import org.sample.client.ui.scene.builder.UISceneBuilder;

/**
 * Реализация конструктора пустой сцены.
 * 
 * @author Ronn
 */
public class EmptySceneBuilder implements UISceneBuilder {

	@Override
	public UIScene build() {

		final Group root = new Group();

		final UIScene scene = new UIScene(root);
		scene.setFill(new Color(0, 0, 0, 0));

		return scene;
	}
}
