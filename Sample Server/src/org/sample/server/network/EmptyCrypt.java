package org.sample.server.network;

import rlib.network.GameCrypt;

/**
 * Модель игрового криптора пакетов.
 *
 * @author Ronn
 */
public final class EmptyCrypt implements GameCrypt {

    private static final EmptyCrypt INSTANCE = new EmptyCrypt();

    public static EmptyCrypt getInstance() {
        return INSTANCE;
    }

    @Override
    public void decrypt(final byte[] data, final int offset, final int length) {
    }

    @Override
    public void encrypt(final byte[] data, final int offset, final int length) {
    }
}
