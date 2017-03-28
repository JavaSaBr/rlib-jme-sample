package com.ss.server.template.view;

import com.ss.server.network.ServerPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import rlib.geom.Vector3f;
import rlib.util.VarTable;
import rlib.util.XmlUtils;

import java.nio.ByteBuffer;

/**
 * The class with information about view ob a game object.
 *
 * @author JavaSaBr
 */
public class ViewInfo {

    private static final String NODE_VIEW = "view";
    private static final String NODE_SCALE = "scale";

    private static final String ATTR_MODEL_KEY = "modelKey";
    private static final String ATTR_X = "x";
    private static final String ATTR_Y = "y";
    private static final String ATTR_Z = "z";

    /**
     * The model key.
     */
    @NotNull
    private final String modelKey;

    /**
     * The model scale.
     */
    @NotNull
    private final Vector3f scale;

    public ViewInfo(@Nullable final Element element) {
        this.scale = Vector3f.newInstance();

        final Element viewElement = XmlUtils.findFirstElement(element, NODE_VIEW);
        final Element scaleElement = XmlUtils.findFirstElement(viewElement, NODE_SCALE);

        this.modelKey = viewElement == null ? "" : viewElement.getAttribute(ATTR_MODEL_KEY);

        final VarTable vars = VarTable.newInstance(scaleElement);

        scale.set(vars.getFloat(ATTR_X, 1F), vars.getFloat(ATTR_Y, 1F), vars.getFloat(ATTR_Z, 1F));
    }

    /**
     * @return the model key.
     */
    @NotNull
    public String getModelKey() {
        return modelKey;
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
    public void writeTo(@NotNull final ServerPacket packet, @NotNull final ByteBuffer buffer) {
        packet.writeString(buffer, modelKey);
        packet.writeVector(buffer, scale);
    }
}
