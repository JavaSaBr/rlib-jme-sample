package com.ss.client.ui;

import com.ss.client.ui.component.UIComponent;

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
	public void onShow(UIComponent component);

	/**
	 * Уведомление о закрытии полноэкранного компонента.
	 * 
	 * @param component закрытый компонент.
	 */
	public void onHide(UIComponent component);
}
