package org.sample.client.jme.post;


import org.sample.client.stage.StageType;
import org.sample.client.util.LocalObjects;

/**
 * Интерфейс для реализации пост эффектов в игре.
 *
 * @author Ronn
 */
public interface PostEffect {

    /**
     * Активация эффекта.
     *
     * @param local контейнер локальных объектов.
     */
    public default void activate(final LocalObjects local) {
    }

    /**
     * Деактивация эффекта.
     *
     * @param local контейнер локальных объектов.
     */
    public default void deactivate(final LocalObjects local) {
    }


    /**
     * Обновление состояния эффекта.
     *
     * @param local контейнер локальных объектов.
     */
    public default void update(final LocalObjects local) {
    }

    /**
     * Проверка доступности эффекта для указанной стадии игры.
     *
     * @param stageType тип стадии.
     */
    public default boolean isAvailable(StageType stageType) {
        return true;
    }
}
