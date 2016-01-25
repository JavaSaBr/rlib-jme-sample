package org.sample.client.network.model;

import rlib.network.GameCrypt;

/**
 * Created by ronn on 25.01.16.
 */
public class EmptyCrypt implements GameCrypt {

    private static final GameCrypt INSTANCE = new EmptyCrypt();

    public static GameCrypt getInstance() {
        return INSTANCE;
    }

    @Override
    public void decrypt(final byte[] data, final int offset, final int length) {
    }

    @Override
    public void encrypt(final byte[] data, final int offset, final int length) {
    }
}
