package com.ss.server.network.model;

import com.ss.server.Config;
import com.ss.server.ServerThread;
import org.jetbrains.annotations.NotNull;
import rlib.network.NetworkConfig;

/**
 * The network configuration.
 *
 * @author JavaSaBr
 */
public class GameNetworkConfig implements NetworkConfig {

    @NotNull
    private static final GameNetworkConfig INSTANCE = new GameNetworkConfig();

    @NotNull
    public static GameNetworkConfig getInstance() {
        return INSTANCE;
    }

    @NotNull
    @Override
    public String getGroupName() {
        return Config.NETWORK_GROUP_NAME;
    }

    @Override
    public int getGroupSize() {
        return Config.NETWORK_GROUP_SIZE;
    }

    @Override
    public int getReadBufferSize() {
        return Config.NETWORK_READ_BUFFER_SIZE;
    }

    @NotNull
    @Override
    public Class<? extends Thread> getThreadClass() {
        return ServerThread.class;
    }

    @Override
    public int getThreadPriority() {
        return Config.NETWORK_THREAD_PRIORITY;
    }

    @Override
    public int getWriteBufferSize() {
        return Config.NETWORK_WRITE_BUFFER_SIZE;
    }

    @Override
    public boolean isVisibleReadException() {
        return false;
    }

    @Override
    public boolean isVisibleWriteException() {
        return false;
    }
}
