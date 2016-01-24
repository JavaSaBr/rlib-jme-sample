package org.sample.client.ui.scene.builder.impl;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import org.sample.client.ui.scene.UIScene;
import org.sample.client.ui.css.CSSFont;
import org.sample.client.ui.scene.builder.UISceneBuilder;

import static javafx.geometry.Pos.CENTER;
import static javafx.scene.paint.Color.TRANSPARENT;
import static org.sample.client.ui.css.CSSFont.FONTS;
import static org.sample.client.util.GameUtil.getInputStream;
import static rlib.ui.util.FXUtils.bindFixedSize;

/**
 * Реализация конструктора сцены логина.
 * 
 * @author Ronn
 */
public class LoginUISceneBuilder implements UISceneBuilder {

	@Override
	public UIScene build() {

		for(final CSSFont font : FONTS) {
			Font.loadFont(getInputStream(font.getPath()), font.getSize());
		}

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

		build(scene, container);

		bindFixedSize(container, scene.widthProperty(), scene.heightProperty());

		scene.notifyFinishBuild();

		return scene;
	}

	/**
	 * Построение UI авторизации.
	 */
	protected void build(final UIScene scene, final StackPane root) {
	}
}
