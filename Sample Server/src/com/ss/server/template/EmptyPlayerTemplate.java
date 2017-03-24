package com.ss.server.template;

import com.ss.server.model.GameObject;
import com.ss.server.model.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import rlib.util.VarTable;

/**
 * The empty player template.
 *
 * @author JavaSaBr
 */
public class EmptyPlayerTemplate extends ObjectTemplate {

    @NotNull
    private static final EmptyPlayerTemplate INSTANCE = new EmptyPlayerTemplate(VarTable.newInstance(), null);

    @NotNull
    public static EmptyPlayerTemplate getInstance() {
        return INSTANCE;
    }

    /**
     * Create a template using the vars and XML element.
     *
     * @param vars       the attributes of the xml element.
     * @param xmlElement the xml element.
     */
    private EmptyPlayerTemplate(@NotNull final VarTable vars, @Nullable final Element xmlElement) {
        super(vars, xmlElement);
    }

    @NotNull
    @Override
    protected Class<? extends GameObject> getInstanceClass() {
        return Player.class;
    }
}
