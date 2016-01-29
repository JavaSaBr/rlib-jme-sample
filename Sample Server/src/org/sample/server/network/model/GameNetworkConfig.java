package org.sample.server.network.model;

import org.sample.server.Config;
import org.sample.server.ServerThread;
import rlib.network.NetworkConfig;

/**
 * Конфиг сети сервера с игровыми клиентами.
 *
 * @author Ronn
 */
public class GameNetworkConfig implements NetworkConfig {

    private static GameNetworkConfig instance;

    public static GameNetworkConfig getInstance() {

        if (instance == null) {
            instance = new GameNetworkConfig();
        }

        return instance;
    }

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
