package org.sample.server.network.packet.client;

import org.sample.server.LocalObjects;
import org.sample.server.manager.AccountManager;
import org.sample.server.network.ClientPacket;
import org.sample.server.network.model.GameClient;

/**
 * Реализация пакета запроса на авторизацию.
 *
 * @author Ronn
 */
public class RequestAuth extends ClientPacket {

    /**
     * Имя пользователя.
     */
    private volatile String name;

    /**
     * Пароль от аккаунта.
     */
    private volatile String password;

    @Override
    protected void readImpl() {
        name = readString(readByte());
        password = readString(readByte());
    }

    @Override
    protected void executeImpl(final LocalObjects local, final long currentTime) {

        final GameClient owner = getOwner();

        final AccountManager accountManager = AccountManager.getInstance();
        accountManager.auth(owner, name, password, local);
    }

    @Override
    public void finalyze() {
        name = null;
        password = null;
    }
}
