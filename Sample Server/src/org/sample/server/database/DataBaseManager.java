package org.sample.server.database;

import org.sample.server.Config;
import rlib.database.CleaningManager;
import rlib.database.ConnectFactories;
import rlib.database.ConnectFactory;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * Менеджер для работы с БД.
 *
 * @author Ronn
 */
public class DataBaseManager {

    protected static final Logger LOGGER = LoggerManager.getLogger(DataBaseManager.class);

    private static DataBaseManager instance;

    public static DataBaseManager getInstance() {

        if (instance == null) {
            instance = new DataBaseManager();
        }

        return instance;
    }

    /**
     * Фабрика подключений к БД.
     */
    private final ConnectFactory connectFactory;

    private DataBaseManager() {
        this.connectFactory = ConnectFactories.newBoneCPConnectFactory(Config.DATA_BASE_CONFIG, Config.DATA_BASE_DRIVER);
    }

    /**
     * Запуск очистки БД.
     */
    public void clean() {

        if (Config.DATA_BASE_CLEANING_START) {
            CleaningManager.cleaning(getConnectFactory());
        }
    }

    /**
     * @return фабрика подкючений.
     */
    public ConnectFactory getConnectFactory() {
        return connectFactory;
    }
}
