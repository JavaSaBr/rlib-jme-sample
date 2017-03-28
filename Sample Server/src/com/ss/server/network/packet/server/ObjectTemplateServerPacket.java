package com.ss.server.network.packet.server;

import com.ss.server.network.ServerPacket;
import com.ss.server.template.ObjectTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * The packet to send data of an object template.
 *
 * @author JavaSaBr
 */
public abstract class ObjectTemplateServerPacket<T extends ObjectTemplate> extends ServerPacket {

    /**
     * The template.
     */
    @Nullable
    protected T template;

    /**
     * The template id.
     */
    protected int templateId;

    @Override
    protected void writeImpl(@NotNull final ByteBuffer buffer) {
        super.writeImpl(buffer);
        final T template = getTemplate();
        writeInt(buffer, getTemplateId());
        writeByte(buffer, template == null ? 0 : 1);
        if (template != null) {
            template.writeTo(this, buffer);
        }
    }

    /**
     * @return the template.
     */
    @Nullable
    private T getTemplate() {
        return template;
    }

    /**
     * @return the template id.
     */
    private int getTemplateId() {
        return templateId;
    }

    @Override
    public void free() {
        templateId = 0;
        template = null;
    }

    @Override
    public String toString() {
        return "ObjectTemplateServerPacket{" + "template=" + template + '}';
    }
}
