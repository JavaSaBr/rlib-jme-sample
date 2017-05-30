package com.ss.client.network.server;

import com.ss.client.network.ServerPacket;
import com.ss.client.template.ObjectTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * The packet to receive data of a template.
 *
 * @author JavaSaBr
 */
public abstract class ObjectTemplateServerPacket<T extends ObjectTemplate> extends ServerPacket {

    /**
     * The read template.
     */
    @Nullable
    private T template;

    /**
     * The template id.
     */
    private int templateId;

    @Override
    protected void readImpl(@NotNull final ByteBuffer buffer) {
        super.readImpl(buffer);

        final int templateId = readInt(buffer);
        if (readByte(buffer) == 0) return;

        final T template = createTemplate(templateId);
        template.readFrom(this, buffer);

        this.template = template;
        this.templateId = templateId;
    }

    protected abstract T createTemplate(final int templateId);

    @Override
    protected void runImpl() {
    }

    /**
     * @return the read template.
     */
    @Nullable
    protected T getTemplate() {
        return template;
    }

    /**
     * @return the template id.
     */
    protected int getTemplateId() {
        return templateId;
    }

    @Override
    public void free() {
        templateId = 0;
        template = null;
    }
}
