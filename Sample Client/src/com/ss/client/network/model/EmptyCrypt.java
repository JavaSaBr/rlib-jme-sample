package com.ss.client.network.model;

import org.jetbrains.annotations.NotNull;
import rlib.network.NetworkCrypt;

/**
 * The empty network crypt.
 *
 * @author JavaSaBr
 */
public class EmptyCrypt implements NetworkCrypt {

    @NotNull
    private static final NetworkCrypt INSTANCE = new EmptyCrypt();

    @NotNull
    public static NetworkCrypt getInstance() {
        return INSTANCE;
    }

    @Override
    public void decrypt(@NotNull final byte[] data, final int offset, final int length) {
    }

    @Override
    public void encrypt(@NotNull final byte[] data, final int offset, final int length) {
    }
}
