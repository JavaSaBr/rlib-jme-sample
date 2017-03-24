package com.ss.server.template;

import com.ss.server.model.GameObject;
import com.ss.server.model.player.PlayerVehicle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import rlib.util.VarTable;

/**
 * The template of a player vehicle to describe general properties.
 *
 * @author JavaSaBr
 */
public class PlayerVehicleTemplate extends VehicleTemplate {

    /**
     * Create a template using the vars and XML element.
     *
     * @param vars       the attributes of the xml element.
     * @param xmlElement the xml element.
     */
    public PlayerVehicleTemplate(@NotNull final VarTable vars, @Nullable final Element xmlElement) {
        super(vars, xmlElement);
    }

    @NotNull
    @Override
    protected Class<? extends GameObject> getInstanceClass() {
        return PlayerVehicle.class;
    }
}
