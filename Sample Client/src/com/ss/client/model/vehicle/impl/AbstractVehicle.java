package com.ss.client.model.vehicle.impl;

import com.ss.client.model.impl.AbstractSpawnableObject;
import com.ss.client.model.vehicle.Vehicle;
import com.ss.client.template.ObjectTemplate;
import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of a vehicle.
 *
 * @author JavaSaBr
 */
public abstract class AbstractVehicle<T extends ObjectTemplate> extends AbstractSpawnableObject<T> implements Vehicle {

    /**
     * @param template the template.
     */
    public AbstractVehicle(@NotNull final T template) {
        super(template);
    }
}
