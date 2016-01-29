package org.sample.server;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;

/**
 * Глобальная фабрика ид.
 *
 * @author Ronn
 */
public final class IdFactory {

    private static final Logger LOGGER = LoggerManager.getLogger(IdFactory.class);

    private static IdFactory instance;

    public static IdFactory getInstance() {

        if (instance == null) {
            instance = new IdFactory();
        }

        return instance;
    }


    private IdFactory() {
        InitializeManager.valid(getClass());
        LOGGER.info("initialized.");
    }
}