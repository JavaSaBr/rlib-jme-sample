package com.ss.server.model.tank;

import com.ss.server.model.impl.AbstractGameObject;
import com.ss.server.template.ObjectTemplate;
import org.jetbrains.annotations.NotNull;

/**
 * Реализация танка.
 */
public class PlayerTank extends AbstractGameObject<ObjectTemplate> {

    /**
     * @param template the template.
     */
    public PlayerTank(@NotNull final ObjectTemplate template) {
        super(template);
    }

    @Override
    public int getClassId() {
        return 0;
    }
}
