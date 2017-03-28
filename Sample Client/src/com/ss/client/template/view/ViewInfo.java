package com.ss.client.template.view;

import static java.util.Objects.requireNonNull;
import com.ss.client.network.ServerPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.geom.Vector3f;

import java.nio.ByteBuffer;

/**
 * The class with information about view ob a game object.
 *
 * @author JavaSaBr
 */
public class ViewInfo {

    /**
     * The model key.
     */
    @Nullable
    private String modelKey;

    /**
     * The model scale.
     */
    @NotNull
    private final Vector3f scale;

    public ViewInfo() {
        this.scale = Vector3f.newInstance();
    }

    /**
     * @return the model key.
     */
    @NotNull
    public String getModelKey() {
        return requireNonNull(modelKey);
    }

    /**
     * @return the model scale.
     */
    @NotNull
    public Vector3f getScale() {
        return scale;
    }

    /**
     * Write data of this view to the buffer of the packet.
     *
     * @param packet the packet.
     * @param buffer the buffer.
     */
    public void readFrom(@NotNull final ServerPacket packet, @NotNull final ByteBuffer buffer) {
        this.modelKey = packet.readString(buffer);
        this.scale.set(packet.readFloat(buffer), packet.readFloat(buffer), packet.readFloat(buffer));
    }
}
