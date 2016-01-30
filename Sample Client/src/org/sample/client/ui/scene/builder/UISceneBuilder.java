package org.sample.client.ui.scene.builder;

import org.sample.client.ui.scene.UIScene;

/**
 * Интерфейс для реализации конструктора сцены на javaFX.
 * 
 * @author Ronn
 */
public interface UISceneBuilder {

	/**
	 * Фаил для переопределения стандартных стилей.
	 */
	public static final String CSS_FILE_BASE = "/ui/css/base.css";

	/**
	 * Фаил для переопределения чтилей из внешних библиотек.
	 */
	public static final String CSS_FILE_EXTERNAL = "/ui/css/external.css";

	/**
	 * Фаил для описания стилей для своих id.
	 */
	public static final String CSS_FILE_CUSTOM_IDS = "/ui/css/custom_ids.css";

	/**
	 * Фаил для описания стилей своих классов.
	 */
	public static final String CSS_FILE_CUSTOM_CLASSES = "/ui/css/custom_classes.css";

	/**
	 * @return построенная сцена.
	 */
	public UIScene build();
}
