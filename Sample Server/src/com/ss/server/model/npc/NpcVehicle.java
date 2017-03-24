package com.ss.server.model.npc;

import com.ss.server.model.vehicle.impl.AbstractVehicle;
import com.ss.server.template.NpcVehicleTemplate;
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

    @Override
    public int getClassId() {
        return CLASS_ID;
    }
}
