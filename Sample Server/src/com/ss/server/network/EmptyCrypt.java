package com.ss.server.network;

import org.jetbrains.annotations.NotNull;
import rlib.network.NetworkCrypt;

/**
 * The empty network crypt.
 *
 * @author JavaSaBr
 */
public final class EmptyCrypt implements NetworkCrypt {

    @NotNull
    private static final EmptyCrypt INSTANCE = new EmptyCrypt();

    @NotNull
    public static EmptyCrypt getInstance() {
        return INSTANCE;
    }

    @Override
    public void decrypt(@NotNull final byte[] data, final int offset, final int length) {
    }

    @Override
    public void encrypt(@NotNull final byte[] data, final int offset, final int length) {
    }
}
