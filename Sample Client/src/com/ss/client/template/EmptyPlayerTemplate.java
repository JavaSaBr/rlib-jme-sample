package com.ss.client.template;

import com.ss.client.model.GameObject;
import com.ss.client.model.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The empty player template.
 *
 * @author JavaSaBr
 */
public class EmptyPlayerTemplate extends ObjectTemplate {

    @NotNull
    private static final EmptyPlayerTemplate INSTANCE = new EmptyPlayerTemplate(-1);

    @NotNull
    public static EmptyPlayerTemplate getInstance() {
        return INSTANCE;
    }

    /**
     * Create a template.
     */
    public EmptyPlayerTemplate(final int templateId) {
        super(templateId);
    }


    @NotNull
    @Override
    protected Class<? extends GameObject> getInstanceClass() {
        return Player.class;
    }
}
