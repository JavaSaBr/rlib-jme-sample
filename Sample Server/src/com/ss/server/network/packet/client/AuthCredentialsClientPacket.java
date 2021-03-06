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
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.ReadablePacketType;

import java.nio.ByteBuffer;

/**
 * The client packet with credentials to authenticate.
 *
 * @author JavaSaBr
 */
public class AuthCredentialsClientPacket extends ClientPacket {

    @NotNull
    private static final ReadablePacketType<ClientPacket> AUTH_CREDENTIALS_TYPE =
            new ReadablePacketType<>(new AuthCredentialsClientPacket(), 1);

    /**
     * The user name.
     */
    @Nullable
    private String name;

    /**
     * The user password.
     */
    @Nullable
    private String password;

    @Override
    protected void readImpl(@NotNull final ByteBuffer buffer) {
        super.readImpl(buffer);
        name = readString(buffer);
        password = readString(buffer);
    }

    @Override
    protected void executeImpl(@NotNull final LocalObjects local, final long currentTime) {

        final GameClient owner = requireNonNull(getOwner());
        final String username = requireNonNull(this.name);
        final String password = requireNonNull(this.password);

        final AccountManager accountManager = AccountManager.getInstance();
        final ResultType resultType = accountManager.auth(owner, username, password);

        // send a result of authenticate
        owner.sendPacket(AuthResultServerPacket.getInstance(resultType), true);
    }

    @NotNull
    @Override
    public ReadablePacketType<? extends ReadablePacket> getPacketType() {
        return AUTH_CREDENTIALS_TYPE;
    }

    @Override
    public void free() {
        name = null;
        password = null;
    }

    @Override
    public String toString() {
        return "AuthCredentialsClientPacket{" + "name='" + name + '\'' + ", password='" + password + '\'' + '}';
    }
}
