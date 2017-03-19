package com.ss.server.network.packet.client;

import static java.util.Objects.requireNonNull;
import com.ss.server.LocalObjects;
import com.ss.server.manager.AccountManager;
import com.ss.server.network.ClientPacket;
import com.ss.server.network.model.GameClient;
import com.ss.server.network.packet.server.AuthResultServerPacket;
import com.ss.server.network.packet.server.AuthResultServerPacket.ResultType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The client packet with credentials to authenticate.
 *
 * @author JavaSaBr
 */
public class AuthCredentialsClientPacket extends ClientPacket {

    /**
     * The user name.
     */
    @Nullable
    private volatile String name;

    /**
     * The user password.
     */
    @Nullable
    private volatile String password;

    @Override
    protected void readImpl() {
        name = readString();
        password = readString();
    }

    @Override
    protected void executeImpl(@NotNull final LocalObjects local, final long currentTime) {

        final GameClient owner = requireNonNull(getOwner());
        final String username = requireNonNull(this.name);
        final String password = requireNonNull(this.password);

        final AccountManager accountManager = AccountManager.getInstance();
        final ResultType resultType = accountManager.auth(owner, username, password);

        // send a result of authenticate
        owner.sendPacket(AuthResultServerPacket.getInstance(resultType, local), true);
    }

    @Override
    public void free() {
        name = null;
        password = null;
    }
}
