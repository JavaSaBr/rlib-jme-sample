package com.ss.server.model.impl;

import com.ss.server.LocalObjects;
import com.ss.server.model.SpawnableObject;
import com.ss.server.template.ObjectTemplate;
import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of a spawnable object.
 *
 * @author JavaSaBr
 */
public abstract class AbstractSpawnableObject<T extends ObjectTemplate> extends AbstractGameObject<T> implements SpawnableObject {

    /**
     * The flag of spawning this object in the world.
     */
    private volatile boolean spawned;

    /**
     * The flag of deleting this object.
     */
    private volatile boolean deleted;

    /**
     * @param template the template.
     */
    public AbstractSpawnableObject(@NotNull final T template) {
        super(template);
    }

    @Override
    public void spawnMe(@NotNull final LocalObjects local) {

        if (isSpawned()) {
            LOGGER.warning(this, "This object can't be spawned because it's already spawned.");
            return;
        }

        spawnMeImpl(local);
        setSpawned(true);
    }

    /**
     * Spawn this object to the world.
     *
     * @param local the local objects.
     */
    protected void spawnMeImpl(@NotNull final LocalObjects local) {
        WORLD.addObject(this, local);
    }

    @Override
    public void unspawnMe(@NotNull final LocalObjects local) {

        if (!isSpawned()) {
            LOGGER.warning(this, "This object can't be unspawned because it wasn't spawned.");
            return;
        }

        unspawnMeImpl(local);
        setSpawned(false);
    }

    /**
     * Unspawn this object from the world.
     *
     * @param local the local objects.
     */
    protected void unspawnMeImpl(@NotNull final LocalObjects local) {
        WORLD.removeObject(this, local);
    }

    @Override
    public void deleteMe(@NotNull final LocalObjects local) {

        if (isDeleted()) {
            LOGGER.warning(this, "This object is already deleted.");
            return;
        }

        deleteMeImpl(local);
    }

    /**
     * Delete this object from the world and the server.
     *
     * @param local the local objects.
     */
    protected void deleteMeImpl(@NotNull final LocalObjects local) {
    }

    @Override
    public boolean isSpawned() {
        return spawned;
    }

    /**
     * @param spawned true if this object was spawned.
     */
    protected void setSpawned(final boolean spawned) {
        this.spawned = spawned;
    }

    /**
     * @param deleted the true of this object was deleted.
     */
    protected void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the true of this object was deleted.
     */
    protected boolean isDeleted() {
        return deleted;
    }

    @Override
    public void reuse() {
        setDeleted(false);
    }

    @Override
    public void free() {
        setDeleted(true);
    }
}
