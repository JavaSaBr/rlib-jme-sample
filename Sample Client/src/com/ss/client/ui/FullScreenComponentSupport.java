package com.ss.client.ui;

import com.ss.client.ui.component.ScreenComponent;

/**
 * Интерфейс для реалзиации поддержки полноэкранных компонентов.
 * 
 * @author Ronn
 */
public interface FullScreenComponentSupport {

	/**
	 * Уведомление о показне на полный экранн компонента.
	 * 
	 * @param component показанный компонент.
	 */
	public void onShow(ScreenComponent component);

	/**
	 * Уведомление о закрытии полноэкранного компонента.
	 * 
	 * @param component закрытый компонент.
	 */
	public void onHide(ScreenComponent component);
}
