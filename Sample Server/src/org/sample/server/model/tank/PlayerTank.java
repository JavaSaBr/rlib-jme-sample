package org.sample.server.model.tank;

import org.sample.server.model.impl.AbstractGameObject;

/**
 * Реализация танка.
 */
public class PlayerTank extends AbstractGameObject {

    public PlayerTank(int objectId) {
        super(objectId);
    }

    @Override
    public int getClassId() {
        return 1000;
    }
}
