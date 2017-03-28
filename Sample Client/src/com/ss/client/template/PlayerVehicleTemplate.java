package com.ss.client.template;


import com.ss.client.model.GameObject;
import com.ss.client.model.player.PlayerVehicle;
import org.jetbrains.annotations.NotNull;

/**
 * The template of a player vehicle to describe general properties.
 *
 * @author JavaSaBr
 */
public class PlayerVehicleTemplate extends VehicleTemplate {

    /**
     * Create a template.
     */
    public PlayerVehicleTemplate(final int templateId) {
        super(templateId);
    }

    @NotNull
    @Override
    protected Class<? extends GameObject> getInstanceClass() {
        return PlayerVehicle.class;
    }
}
