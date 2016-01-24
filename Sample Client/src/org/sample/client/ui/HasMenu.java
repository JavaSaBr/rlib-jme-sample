package org.sample.client.ui;

/**
 * Интерфейс для реализации у сцены меню.
 * 
 * @author Ronn
 */
public interface HasMenu {

	/**
	 * Отобразить меню для этой сцены.
	 */
	public void showMenu();

	/**
	 * @return отоброжено ли меню сейчас.
	 */
	public boolean isMenuShowed();

	/**
	 * Спрятать меню.
	 */
	public void hideMenu();
}
