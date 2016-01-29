package org.sample.server;

import com.jolbox.bonecp.BoneCPConfig;

import org.sample.server.document.DocumentConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.FileUtils;
import rlib.util.StringUtils;
import rlib.util.Util;
import rlib.util.VarTable;
import rlib.util.array.Array;

/**
 * Набор параметров работы сервера.
 *
 * @author Ronn
 */
public final class Config {

    private static final Logger LOGGER = LoggerManager.getLogger(Config.class);

    /**
     * Экземпляр конфига для БД.
     */
    public static final BoneCPConfig DATA_BASE_CONFIG = new BoneCPConfig();

    public static final String CONFIG_FOLDER_NAME = "config";
    public static final String DATA_FOLDER_NAME = "data";

    public static final String SCRIPTS_FOLDER_NAME = "scripts";

    /**
     * Пути к основным папкам сервера.
     */
    public static String FOLDER_PROJECT_PATH;
    public static String FOLDER_CONFIG_PATH;
    public static String FOLDER_SCRIPTS_PATH;
    public static String FOLDER_DATA_PATH;

    /**
     * Настройки аккаунтов
     */

    /**
     * Минимальный уровень прав для входа на сервер.
     */
    public static int ACCOUNT_MIN_ACCESS_LEVEL;

    /**
     * Автоматически ли регистрировать новые аккаунты.
     */
    public static boolean ACCOUNT_AUTO_REGISTER;

    /**
     * Настройки сервера
     */

    /**
     * Название сервера.
     */
    public static String SERVER_NAME;

    /**
     * Версия игры сервера.
     */
    public static String SERVER_VERSION;

    /**
     * Тип сервера.
     */
    public static String SERVER_TYPE;

    /**
     * Допустимые ники на сервере.
     */
    public static String SERVER_USER_NAME_TEMPLATE;

    /**
     * Адресс сервера.
     */
    public static String SERVER_HOST;

    /**
     * Тип фильтра соединений.
     */
    public static String SERVER_ACCEPT_FILTER;

    /**
     * Адресс вывода в фаил онлайна сервера.
     */
    public static String SERVER_ONLINE_FILE;

    /**
     * Масимальный доступный онлаин насервере.
     */
    public static int SERVER_MAXIMUM_ONLINE;

    /**
     * Порт сервера для бинда.
     */
    public static int SERVER_PORT;

    /**
     * Настройки базы данных
     */

    /**
     * Класс драйвера базы данных
     */
    public static String DATA_BASE_DRIVER;

    /**
     * Адресс базы данных.
     */
    public static String DATA_BASE_URL;

    /**
     * Логин для доступа к базе данных.
     */
    public static String DATA_BASE_LOGIN;

    /**
     * Пароль для доступа к базе данных.
     */
    public static String DATA_BASE_PASSWORD;

    /**
     * Максимальное кол-во коннектов к базе в пуле.
     */
    public static int DATA_BASE_MAX_CONNECTIONS;

    /**
     * Максимальное кол-во создаваемых statements.
     */
    public static int DATA_BASE_MAX_STATEMENTS;

    /**
     * Чистить ли БД при старте сервера.
     */
    public static boolean DATA_BASE_CLEANING_START;

    /**
     * Использование дополнительного кэша коннектов.
     */
    public static boolean DATA_BASE_USE_CACHE;

    /**
     * Настройки потоков
     */

    /**
     * Кол-во потоков для исполнения основных задач.
     */
    public static int THREAD_EXECUTOR_GENERAL_SIZE;

    /**
     * Кол-во потоков для исполнения пакетных задач.
     */
    public static int THREAD_EXECUTOR_PACKET_SIZE;

    /**
     * Настройки сети
     */

    /**
     * Название группы сетевых потоков.
     */
    public static String NETWORK_GROUP_NAME;

    /**
     * Максимальное число разрезаемых пакетов.
     */
    public static int NETWORK_MAXIMUM_PACKET_CUT;

    /**
     * Размер группы сетевых потоков.
     */
    public static int NETWORK_GROUP_SIZE;

    /**
     * Приоритет сетевых потоков.
     */
    public static int NETWORK_THREAD_PRIORITY;

    /**
     * Размер буфера для чтения пакетов.
     */
    public static int NETWORK_READ_BUFFER_SIZE;

    /**
     * Размер буфера для записи пакетов.
     */
    public static int NETWORK_WRITE_BUFFER_SIZE;

    /**
     * Инициализация конфига.
     */
    public static void init(final String[] args) {
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

        SERVER_NAME = vars.getString("Server.name");
        SERVER_VERSION = vars.getString("Server.version");
        SERVER_TYPE = vars.getString("Server.type");
        SERVER_USER_NAME_TEMPLATE = vars.getString("Server.userNameTemplate");
        SERVER_ACCEPT_FILTER = vars.getString("Server.acceptFilter");
        SERVER_ONLINE_FILE = vars.getString("Server.onlineFile", StringUtils.EMPTY);

        SERVER_HOST = vars.getString("Server.host");
        SERVER_PORT = vars.getInteger("Server.port");

        DATA_BASE_DRIVER = vars.getString("DataBase.driver");
        DATA_BASE_URL = vars.getString("DataBase.url");
        DATA_BASE_LOGIN = vars.getString("DataBase.login");
        DATA_BASE_PASSWORD = vars.getString("DataBase.password");
        DATA_BASE_MAX_CONNECTIONS = vars.getInteger("DataBase.maxConnections");
        DATA_BASE_MAX_STATEMENTS = vars.getInteger("DataBase.maxStatemens");
        DATA_BASE_CLEANING_START = vars.getBoolean("DataBase.cleaningStart");
        DATA_BASE_USE_CACHE = vars.getBoolean("DataBase.useCache");

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

        // создаем доп. проперти драйвера мускула
        final Properties properties = new Properties();

        Util.addUTFToMySQLConnectionProperties(properties);

        // применяем проперти
        DATA_BASE_CONFIG.setDriverProperties(properties);

        final Set<String> arguments = new HashSet<>();

        for (final String arg : args) {
            arguments.add(arg);
        }
    }

    /**
     * Инициализация путей к основным папкам сервера.
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
