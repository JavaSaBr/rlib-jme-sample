package com.ss.server.template;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import rlib.util.VarTable;

/**
 * The template of a vehicle to describe general properties.
 *
 * @author JavaSaBr
 */
public abstract class VehicleTemplate extends ObjectTemplate {

    /**
     * Create a template using the vars and XML element.
     *
     * @param vars       the attributes of the xml element.
     * @param xmlElement the xml element.
     */
    public VehicleTemplate(@NotNull final VarTable vars, @Nullable final Element xmlElement) {
        super(vars, xmlElement);
    }
}
