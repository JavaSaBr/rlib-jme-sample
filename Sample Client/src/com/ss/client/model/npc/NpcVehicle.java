package com.ss.client.model.npc;

import com.ss.client.model.vehicle.impl.AbstractVehicle;
import com.ss.client.template.NpcVehicleTemplate;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of a NPC vehicle.
 *
 * @author JavaSaBr
 */
public class NpcVehicle extends AbstractVehicle<NpcVehicleTemplate> {

    public static final int CLASS_ID = 3;

    /**
     * @param template the template.
     */
    public NpcVehicle(@NotNull final NpcVehicleTemplate template) {
        super(template);
    }
}
