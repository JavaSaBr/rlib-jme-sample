package com.ss.server;

import com.jolbox.bonecp.BoneCPConfig;
import com.ss.server.document.DocumentConfig;
import org.jetbrains.annotations.NotNull;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.FileUtils;
import rlib.util.Util;
import rlib.util.VarTable;
import rlib.util.array.Array;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * The config of the server.
 *
 * @author JavaSaBr
 */
public final class Config {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(Config.class);

    /**
     * The config to work with DB.
     */
    public static final BoneCPConfig DATA_BASE_CONFIG = new BoneCPConfig();

    public static final String CONFIG_FOLDER_NAME = "config";
    public static final String DATA_FOLDER_NAME = "data";

    public static final String SCRIPTS_FOLDER_NAME = "scripts";

    /**
     * The server paths.
     */
    public static String FOLDER_PROJECT_PATH;
    public static String FOLDER_CONFIG_PATH;
    public static String FOLDER_SCRIPTS_PATH;
    public static String FOLDER_DATA_PATH;

    /**
     * The minimum access level.
     */
    public static int ACCOUNT_MIN_ACCESS_LEVEL;

    /**
     * The flag of enabling auto registration.
     */
    public static boolean ACCOUNT_AUTO_REGISTER;

    /**
     * The server version.
     */
    public static String SERVER_VERSION;

    /**
     * The server host.
     */
    public static String SERVER_HOST;

    /**
     * The server port.
     */
    public static int SERVER_PORT;

    /**
     * The data base driver.
     */
    public static String DATA_BASE_DRIVER;

    /**
     * The data base URL.
     */
    public static String DATA_BASE_URL;

    /**
     * The data base login.
     */
    public static String DATA_BASE_LOGIN;

    /**
     * The data base password.
     */
    public static String DATA_BASE_PASSWORD;

    /**
     * The database max connections.
     */
    public static int DATA_BASE_MAX_CONNECTIONS;

    /**
     * The data base max statements.
     */
    public static int DATA_BASE_MAX_STATEMENTS;

    /**
     * The flag of cleaning data base on server start.
     */
    public static boolean DATA_BASE_CLEANING_START;

    /**
     * The thread pool size to execute general tasks.
     */
    public static int THREAD_EXECUTOR_GENERAL_SIZE;

    /**
     * The thread pool size to execute packets.
     */
    public static int THREAD_EXECUTOR_PACKET_SIZE;

    /**
     * The network thread group name.
     */
    public static String NETWORK_GROUP_NAME;

    /**
     * The network maximum packet cut.
     */
    public static int NETWORK_MAXIMUM_PACKET_CUT;

    /**
     * The size of the network thread group.
     */
    public static int NETWORK_GROUP_SIZE;

    /**
     * The priority of the network threads.
     */
    public static int NETWORK_THREAD_PRIORITY;

    /**
     * The network read buffer size.
     */
    public static int NETWORK_READ_BUFFER_SIZE;

    /**
     * The network write buffer size.
     */
    public static int NETWORK_WRITE_BUFFER_SIZE;

    /**
     * Инициализация конфига.
     */
    public static void init(@NotNull final String[] args) {
        initFolders();

        final Path directory = Paths.get(FOLDER_CONFIG_PATH);

        if (!Files.exists(directory)) {
            LOGGER.warning(directory + " not available, the configuration is not loaded!");
            System.exit(0);
        }

        final VarTable vars = VarTable.newInstance();
        final Array<Path> paths = FileUtils.getFiles(directory, ".xml");

        for (final Path path : paths) {
            vars.set(new DocumentConfig(path).parse());
        }

        ACCOUNT_MIN_ACCESS_LEVEL = vars.getInteger("Account.minAccessLevel");
        ACCOUNT_AUTO_REGISTER = vars.getBoolean("Account.autoRegister", false);

        SERVER_VERSION = vars.getString("Server.version");
        SERVER_HOST = vars.getString("Server.host");
        SERVER_PORT = vars.getInteger("Server.port");

        DATA_BASE_DRIVER = vars.getString("DataBase.driver");
        DATA_BASE_URL = vars.getString("DataBase.url");
        DATA_BASE_LOGIN = vars.getString("DataBase.login");
        DATA_BASE_PASSWORD = vars.getString("DataBase.password");
        DATA_BASE_MAX_CONNECTIONS = vars.getInteger("DataBase.maxConnections");
        DATA_BASE_MAX_STATEMENTS = vars.getInteger("DataBase.maxStatements");
        DATA_BASE_CLEANING_START = vars.getBoolean("DataBase.cleaningStart");

        THREAD_EXECUTOR_GENERAL_SIZE = vars.getInteger("Thread.executorGeneralSize");
        THREAD_EXECUTOR_PACKET_SIZE = vars.getInteger("Thread.executorPacketSize");

        NETWORK_GROUP_NAME = vars.getString("Network.groupName");
        NETWORK_GROUP_SIZE = vars.getInteger("Network.groupSize");
        NETWORK_THREAD_PRIORITY = vars.getInteger("Network.threadProirity");
        NETWORK_READ_BUFFER_SIZE = vars.getInteger("Network.readBufferSize");
        NETWORK_WRITE_BUFFER_SIZE = vars.getInteger("Network.writeBufferSize");
        NETWORK_MAXIMUM_PACKET_CUT = vars.getInteger("Network.maximumPacketCut");

        DATA_BASE_CONFIG.setJdbcUrl(DATA_BASE_URL);
        DATA_BASE_CONFIG.setUsername(DATA_BASE_LOGIN);
        DATA_BASE_CONFIG.setPassword(DATA_BASE_PASSWORD);
        DATA_BASE_CONFIG.setAcquireRetryAttempts(0);
        DATA_BASE_CONFIG.setAcquireIncrement(5);
        DATA_BASE_CONFIG.setReleaseHelperThreads(0);
        DATA_BASE_CONFIG.setMinConnectionsPerPartition(2);
        DATA_BASE_CONFIG.setMaxConnectionsPerPartition(DATA_BASE_MAX_CONNECTIONS);
        DATA_BASE_CONFIG.setStatementsCacheSize(DATA_BASE_MAX_STATEMENTS);

        final Properties properties = new Properties();

        DATA_BASE_CONFIG.setDriverProperties(properties);
    }

    /**
     * Init server folder paths.
     */
    private static void initFolders() {

        FOLDER_PROJECT_PATH = Util.getRootFolderFromClass(Config.class).toString();

        Path path = Paths.get(FOLDER_PROJECT_PATH, SCRIPTS_FOLDER_NAME);

        FOLDER_SCRIPTS_PATH = path.toString();

        path = Paths.get(FOLDER_PROJECT_PATH, DATA_FOLDER_NAME);

        FOLDER_DATA_PATH = path.toString();

        path = Paths.get(FOLDER_PROJECT_PATH, CONFIG_FOLDER_NAME);

        FOLDER_CONFIG_PATH = path.toString();
    }

    private Config() {
        throw new RuntimeException();
    }
}
