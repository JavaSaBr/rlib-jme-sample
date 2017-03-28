package com.ss.server;

import com.ss.server.database.AccountDBManager;
import com.ss.server.database.PlayerDBManager;
import com.ss.server.database.PlayerVehicleDBManager;
import com.ss.server.manager.*;
import com.ss.server.network.Network;
import org.jetbrains.annotations.NotNull;
import rlib.compiler.CompilerFactory;
import rlib.concurrent.util.ThreadUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerLevel;
import rlib.logging.LoggerManager;
import rlib.logging.impl.FolderFileListener;
import rlib.manager.InitializeManager;
import rlib.monitoring.MemoryMonitoring;
import rlib.monitoring.MonitoringManager;
import rlib.util.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The main class of the server.
 *
 * @author JavaSaBr
 */
public class GameServer extends ServerThread {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(GameServer.class);

    /**
     * The instance.
     */
    private static GameServer instance;

    @NotNull
    public static GameServer getInstance() {

        if (instance == null) {
            instance = new GameServer();
        }

        return instance;
    }

    private static void configureLogging() {

        LoggerLevel.DEBUG.setEnabled(false);
        LoggerLevel.ERROR.setEnabled(true);
        LoggerLevel.WARNING.setEnabled(true);
        LoggerLevel.INFO.setEnabled(true);

        final Path logFolder = Paths.get(Config.FOLDER_PROJECT_PATH, "log");

        if (!Files.exists(logFolder)) {
            try {
                Files.createDirectories(logFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LoggerManager.addListener(new FolderFileListener(logFolder));
    }

    public static void main(@NotNull final String[] args) throws Exception {

        if (!CompilerFactory.isAvailableCompiler()) {
            throw new RuntimeException("not found java compiler.");
        }

        Config.init(args);

        configureLogging();

        Utils.checkFreePort("*", Config.SERVER_PORT);

        final DataBaseManager manager = DataBaseManager.getInstance();
        manager.clean();

        getInstance();
    }

    private GameServer() {
        setName("Initialize Thread");
        start();
    }

    @Override
    public void run() {
        try {

            InitializeManager.register(ExecutorManager.class);
            InitializeManager.register(DataBaseManager.class);
            InitializeManager.register(AccountDBManager.class);
            InitializeManager.register(PlayerDBManager.class);
            InitializeManager.register(PlayerVehicleDBManager.class);
            InitializeManager.register(PlayerManager.class);
            InitializeManager.register(AccountManager.class);
            InitializeManager.register(ClassManager.class);
            InitializeManager.register(VehicleTemplateManager.class);
            InitializeManager.register(Network.class);
            InitializeManager.initialize();

            LOGGER.info("started.");

            final Runnable target = () -> {

                final MonitoringManager manager = MonitoringManager.getInstance();
                final MemoryMonitoring memory = manager.getMemoryMonitoring();

                for (;;) {
                    LOGGER.info(memory, memory.getHeapUsedSize() / 1024 / 1024 + " MiB.");
                    ThreadUtils.sleep(30000);
                }
            };

            final ServerThread thread = new ServerThread(target);
            thread.setDaemon(true);
            thread.setName("Monitoring Memory");
            thread.start();

        } catch (final Exception e) {
            LOGGER.warning(e);
            System.exit(0);
        }
    }
}
