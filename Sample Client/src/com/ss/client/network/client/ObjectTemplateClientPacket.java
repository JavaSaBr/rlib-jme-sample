package com.ss.client.network.client;

import com.ss.client.network.ClientPacket;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The packet to request data of a template.
 *
 * @author JavaSaBr
 */
public abstract class ObjectTemplateClientPacket extends ClientPacket {

    /**
     * The template id.
     */
    protected int templateId;

    @Override
    protected void writeImpl(@NotNull final ByteBuffer buffer) {
        super.writeImpl(buffer);
        writeInt(buffer, templateId);
    }

    @Override
    public void free() {
        templateId = 0;
    }
}
