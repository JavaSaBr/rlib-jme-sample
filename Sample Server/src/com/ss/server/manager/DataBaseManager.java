package com.ss.server.manager;

import com.ss.server.Config;
import com.ss.server.database.sql.provider.SqlProvider;
import com.ss.server.database.sql.provider.mysql.MySQLSqlProvider;
import org.jetbrains.annotations.NotNull;
import rlib.database.CleaningManager;
import rlib.database.ConnectFactories;
import rlib.database.ConnectFactory;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.ClassUtils;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.ObjectDictionary;

import java.util.Objects;

/**
 * The manager to work with DB.
 *
 * @author JavaSaBr
 */
public class DataBaseManager {

    @NotNull
    protected static final Logger LOGGER = LoggerManager.getLogger(DataBaseManager.class);

    @NotNull
    private static final ObjectDictionary<String, Class<? extends SqlProvider>> SQL_PROVIDERS = DictionaryFactory.newObjectDictionary();

    static {
        SQL_PROVIDERS.put("com.mysql.jdbc.Driver", MySQLSqlProvider.class);
    }

    private static DataBaseManager instance;

    @NotNull
    public static DataBaseManager getInstance() {

        if (instance == null) {
            instance = new DataBaseManager();
        }

        return instance;
    }

    /**
     * The connection factory.
     */
    @NotNull
    private final ConnectFactory connectFactory;

    /**
     * The SQL provider.
     */
    @NotNull
    private final SqlProvider sqlProvider;

    private DataBaseManager() {
        this.connectFactory = ConnectFactories.newBoneCPConnectFactory(Config.DATA_BASE_CONFIG, Config.DATA_BASE_DRIVER);
        this.sqlProvider = ClassUtils.newInstance(Objects.requireNonNull(SQL_PROVIDERS.get(Config.DATA_BASE_DRIVER),
                "Not found sql provider for the DB " + Config.DATA_BASE_DRIVER));
    }

    /**
     * Clean database.
     */
    public void clean() {
        if (Config.DATA_BASE_CLEANING_START) {
            CleaningManager.cleaning(getConnectFactory());
        }
    }

    /**
     * @return the connection factory.
     */
    @NotNull
    public ConnectFactory getConnectFactory() {
        return connectFactory;
    }

    /**
     * @return the SQL provider.
     */
    @NotNull
    public SqlProvider getSqlProvider() {
        return sqlProvider;
    }
}
