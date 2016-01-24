package org.sample.client.ui.scene.builder;

import org.sample.client.ui.scene.UIScene;

/**
 * Интерфейс для реализации конструктора сцены на javaFX.
 * 
 * @author Ronn
 */
public interface UISceneBuilder {

	public static final String CSS_FILE_BASE = "/ui/css/base.css";
	public static final String CSS_FILE_EXTERNAL = "/ui/css/external.css";
	public static final String CSS_FILE_CUSTOM_IDS = "/ui/css/custom_ids.css";
	public static final String CSS_FILE_CUSTOM_CLASSES = "/ui/css/custom_classes.css";

	/**
	 * @return построенная сцена.
	 */
	public UIScene build();
}
