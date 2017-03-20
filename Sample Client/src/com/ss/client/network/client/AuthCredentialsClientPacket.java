package com.ss.client.network.client;

import static java.util.Objects.requireNonNull;
import com.ss.client.network.ClientPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.network.packet.SendablePacket;
import rlib.network.packet.SendablePacketType;

import java.nio.ByteBuffer;

/**
 * The packet to send authentication credentials to server.
 *
 * @author JavaSaBr
 */
public class AuthCredentialsClientPacket extends ClientPacket {

    @NotNull
    private static final SendablePacketType<ClientPacket> AUTH_CREDENTIALS_TYPE =
            new SendablePacketType<>(AuthCredentialsClientPacket.class, 1);

    /**
     * The example of this packet to create new packets of the type.
     */
    @NotNull
    private static final AuthCredentialsClientPacket EXAMPLE = new AuthCredentialsClientPacket();

    @NotNull
    public static AuthCredentialsClientPacket getInstance(@NotNull final String name, @NotNull final String password) {
        final AuthCredentialsClientPacket packet = EXAMPLE.newInstance();
        packet.name = name;
        packet.password = password;
        return packet;
    }

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
    protected void writeImpl(@NotNull final ByteBuffer buffer) {
        super.writeImpl(buffer);
        writeString(buffer, requireNonNull(name));
        writeString(buffer, requireNonNull(password));
    }

    @NotNull
    @Override
    public SendablePacketType<? extends SendablePacket> getPacketType() {
        return AUTH_CREDENTIALS_TYPE;
    }

    @Override
    public void free() {
        name = null;
        password = null;
    }
}
