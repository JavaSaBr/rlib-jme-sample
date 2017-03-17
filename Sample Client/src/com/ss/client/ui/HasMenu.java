package com.ss.client.ui;

/**
 * The interface to implement supporting a menu in a scene.
 *
 * @author JavaSaBr
 */
public interface HasMenu {

    /**
     * Show a menu.
     */
    void showMenu();

    /**
     * @return true if the menu is showed.
     */
    boolean isMenuShowed();

    /**
     * Hide a menu.
     */
    void hideMenu();
}
