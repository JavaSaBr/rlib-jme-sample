package com.ss.client.ui.scene.builder;

import com.ss.client.ui.scene.UIScene;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to fillScene UI scene.
 * 
 * @author JavaSaBr
 */
public interface UISceneBuilder {

	/**
	 * The base ccs style.
	 */
    String CSS_FILE_BASE = "/ui/css/base.css";

	/**
	 * The css style for external controls.
	 */
    String CSS_FILE_EXTERNAL = "/ui/css/external.css";

	/**
	 * The ccs style with custom IDs.
	 */
    String CSS_FILE_CUSTOM_IDS = "/ui/css/custom_ids.css";

	/**
	 * The css style with custom classes.
	 */
    String CSS_FILE_CUSTOM_CLASSES = "/ui/css/custom_classes.css";

	/**
     * Build a new UI scene.
     *
	 * @return the new UI scene.
	 */
	@NotNull
    UIScene build();
}
