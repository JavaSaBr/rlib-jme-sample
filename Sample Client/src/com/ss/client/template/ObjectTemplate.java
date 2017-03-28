package com.ss.client.template;

import static java.util.Objects.requireNonNull;
import static rlib.util.ClassUtils.getConstructor;
import com.ss.client.model.GameObject;
import com.ss.client.network.ServerPacket;
import com.ss.client.template.view.ViewInfo;
import org.jetbrains.annotations.NotNull;
import rlib.util.ClassUtils;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.function.Function;

/**
 * The base template to describe general properties of a game object.
 *
 * @author JavaSaBr
 */
public abstract class ObjectTemplate {

    /**
     * The pool of instances to reuse it.
     */
    @NotNull
    private final ReusablePool<GameObject> instancePool;

    /**
     * The constructor of instances.
     */
    @NotNull
    private final Function<ObjectTemplate, GameObject> constructor;

    /**
     * The information about view of objects of this template.
     */
    @NotNull
    private final ViewInfo viewInfo;

    /**
     * The template id.
     */
    private final int templateId;

    /**
     * Create a template.
     */
    public ObjectTemplate(final int templateId) {
        this.templateId = templateId;
        this.instancePool = PoolFactory.newConcurrentAtomicARSWLockReusablePool(getInstanceClass());
        this.viewInfo = new ViewInfo();
        this.constructor = buildConstructor();
    }

    /**
     * The class of instances of this template.
     *
     * @return the class.
     */
    @NotNull
    protected Class<? extends GameObject> getInstanceClass() {
        throw new UnsupportedOperationException();
    }

    /**
     * Build a constructor to create instances.
     *
     * @return the constructor.
     */
    @NotNull
    protected Function<ObjectTemplate, GameObject> buildConstructor() {

        final Class<? extends GameObject> instanceClass = getInstanceClass();
        final Constructor<? extends GameObject> constructor = requireNonNull(getConstructor(instanceClass, getClass()));

        return template -> ClassUtils.newInstance(constructor, template);
    }

    /**
     * @return the template id.
     */
    public int getTemplateId() {
        return templateId;
    }

    /**
     * Take a new instance of this template.
     *
     * @param type     the type.
     * @param objectId the object id.
     * @return the new instance.
     */
    @NotNull
    public <T extends GameObject> T takeInstance(@NotNull final Class<T> type, final int objectId) {

        final T object = type.cast(instancePool.take(constructor, this, Function::apply));
        object.setObjectId(objectId);

        return object;
    }

    /**
     * Write data of this template to the buffer of the packet.
     *
     * @param packet the packet.
     * @param buffer the buffer.
     */
    public void readFrom(@NotNull final ServerPacket packet, @NotNull final ByteBuffer buffer) {
        viewInfo.readFrom(packet, buffer);
    }
}
