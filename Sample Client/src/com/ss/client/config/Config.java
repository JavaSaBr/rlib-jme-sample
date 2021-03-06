package com.ss.client.config;

import com.ss.client.GameThread;
import com.ss.client.Starter;
import com.ss.client.document.DocumentConfig;
import com.ss.client.util.GameUtil;
import org.jetbrains.annotations.NotNull;
import rlib.network.NetworkConfig;
import rlib.util.Utils;
import rlib.util.VarTable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * The client configuration.
 *
 * @author JavaSaBr
 */
public abstract class Config {

    public static final String CONFIG_RESOURCE_PATH = "/com/ss/client/config/config.xml";

    /**
     * The version of the client.
     */
    @NotNull
    public static final String GAME_VERSION = "0.0.1";

    /**
     * The server address.
     */
    public static InetSocketAddress SERVER_SOCKET_ADDRESS;

    /**
     * The server host.
     */
    public static String SERVER_HOST;

    /**
     * The path to client folder.
     */
    public static String PROJECT_PATH;

    /**
     * The server port.
     */
    public static int SERVER_PORT;

    /**
     * The read buffer size.
     */
    public static int NETWORK_READ_BUFFER_SIZE;

    /**
     * The write buffer size.
     */
    public static int NETWORK_WRITE_BUFFER_SIZE;

    /**
     * The network group.
     */
    public static int NETWORK_GROUP_SIZE;

    /**
     * The network thread priority.
     */
    public static int NETWORK_THREAD_PRIORITY;

    /**
     * The network config.
     */
    public static NetworkConfig NETWORK_CONFIG = new NetworkConfig() {

        @Override
        public String getGroupName() {
            return "Network";
        }

        @Override
        public int getGroupSize() {
            return NETWORK_GROUP_SIZE;
        }

        @Override
        public int getReadBufferSize() {
            return NETWORK_READ_BUFFER_SIZE;
        }

        @NotNull
        @Override
        public Class<? extends Thread> getThreadClass() {
            return GameThread.class;
        }

        @Override
        public int getThreadPriority() {
            return NETWORK_THREAD_PRIORITY;
        }

        @Override
        public int getWriteBufferSize() {
            return NETWORK_WRITE_BUFFER_SIZE;
        }

        @Override
        public boolean isVisibleReadException() {
            return true;
        }

        @Override
        public boolean isVisibleWriteException() {
            return false;
        }
    };

    /**
     * Enable dev debug.
     */
    public static boolean DEV_DEBUG;

    /**
     * Init config.
     */
    public static void init() throws UnknownHostException {

        final VarTable vars = new DocumentConfig(GameUtil.getInputStream(CONFIG_RESOURCE_PATH)).parse();

        SERVER_HOST = vars.getString("Server.host", "localhost");
        SERVER_PORT = vars.getInteger("Server.port", 1000);

        SERVER_SOCKET_ADDRESS = new InetSocketAddress(InetAddress.getByName(SERVER_HOST), SERVER_PORT);

        NETWORK_READ_BUFFER_SIZE = vars.getInteger("Network.readBufferSize", 8388608);
        NETWORK_WRITE_BUFFER_SIZE = vars.getInteger("Network.writeBufferSize", 8388608);
        NETWORK_GROUP_SIZE = vars.getInteger("Network.groupSize", 1);
        NETWORK_THREAD_PRIORITY = vars.getInteger("Network.threadPriority", 5);

        DEV_DEBUG = vars.getBoolean("Dev.debug", false);

        PROJECT_PATH = Utils.getRootFolderFromClass(Starter.class).toString();
    }
}
