package com.ss.client.stage;

import com.ss.client.stage.impl.LoginStage;
import com.ss.client.stage.impl.LoginStage;
import rlib.util.ClassUtils;

/**
 * Created by ronn on 23.01.16.
 */
public enum StageType {
    LOGIN_STAGE(LoginStage.class),
    HANGAR_STAGE(null),
    GAME_STAGE(null);

    private final Class<? extends Stage> implementation;

    private volatile Stage stage;

    private StageType(final Class<? extends Stage> implementation) {
        this.implementation = implementation;
    }

    public Stage getStage() {

        if(stage == null) {
            synchronized (this) {
                if(stage == null) {
                    stage = ClassUtils.newInstance(implementation);
                }
            }
        }

        return stage;
    }
}
