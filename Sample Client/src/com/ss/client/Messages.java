package com.ss.client;

import static java.util.ResourceBundle.getBundle;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import rlib.util.PropertyLoader;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The messages of the game.
 *
 * @author JavaSaBr
 */
public class Messages {

    private static final String BUNDLE_NAME = "messages/messages";

    public static final String LOGIN_AUTH_PANEL_USERNAME_FIELD;
    public static final String LOGIN_AUTH_PANEL_PASSWORD_FIELD;

    static {

        final Locale locale = Locale.getDefault();
        final ClassLoader classLoader = ControlResources.class.getClassLoader();
        final ResourceBundle bundle = getBundle(BUNDLE_NAME, PropertyLoader.getInstance());

        LOGIN_AUTH_PANEL_USERNAME_FIELD = bundle.getString("LoginAuthPanel.usernameField");
        LOGIN_AUTH_PANEL_PASSWORD_FIELD = bundle.getString("LoginAuthPanel.passwordField");
    }
}
