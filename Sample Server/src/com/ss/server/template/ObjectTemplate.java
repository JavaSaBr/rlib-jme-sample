package com.ss.server.template;

import static java.util.Objects.requireNonNull;
import static rlib.util.ClassUtils.getConstructor;
import com.ss.server.model.GameObject;
import com.ss.server.network.ServerPacket;
import com.ss.server.template.view.ViewInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import rlib.util.ClassUtils;
import rlib.util.VarTable;
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

    public static final String PROP_TEMPLATE_ID = "id";
    public static final String PROP_NAME = "name";

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
     * The template name.
     */
    @NotNull
    private final String name;

    /**
     * The template id.
     */
    private final int templateId;

    /**
     * Create a template using the vars and XML element.
     *
     * @param vars       the attributes of the xml element.
     * @param xmlElement the xml element.
     */
    public ObjectTemplate(@NotNull final VarTable vars, @Nullable final Element xmlElement) {
        this.instancePool = PoolFactory.newConcurrentAtomicARSWLockReusablePool(getInstanceClass());
        this.viewInfo = new ViewInfo(xmlElement);
        this.templateId = vars.getInteger(PROP_TEMPLATE_ID, 0);
        this.name = vars.getString(PROP_NAME, "no name");
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
    public void writeTo(@NotNull final ServerPacket packet, @NotNull final ByteBuffer buffer) {
        viewInfo.writeTo(packet, buffer);
    }
}
