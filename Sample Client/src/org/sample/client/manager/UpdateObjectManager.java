package org.sample.client.manager;

import org.jetbrains.annotations.NotNull;
import org.sample.client.executor.GameUpdater;
import org.sample.client.executor.impl.UpdatableObjectGameUpdater;
import org.sample.client.model.util.UpdatableObject;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;
import rlib.util.dictionary.ConcurrentObjectDictionary;
import rlib.util.dictionary.DictionaryFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The manager of updating game objects.
 *
 * @author JavaSaBr
 */
public class UpdateObjectManager {

    @NotNull
    protected static final Logger LOGGER = LoggerManager.getLogger(UpdateObjectManager.class);

    @NotNull
    private static final Runtime RUNTIME = Runtime.getRuntime();

    private static final int PROP_EXECUTORS = RUNTIME.availableProcessors();

    private static UpdateObjectManager instance;

    public static UpdateObjectManager getInstance() {

        if (instance == null) {
            instance = new UpdateObjectManager();
        }

        return instance;
    }

    /**
     * The dictionary to map which object is in which updater.
     */
    @NotNull
    private final ConcurrentObjectDictionary<UpdatableObject, GameUpdater<UpdatableObject>> objectToUpdater;

    /**
     * The list of object updaters.
     */
    @NotNull
    private final GameUpdater<UpdatableObject>[] updaters;

    /**
     * The index of the next updater.
     */
    @NotNull
    private final AtomicInteger nextUpdaterIndex;

    private UpdateObjectManager() {
        InitializeManager.valid(getClass());

        this.objectToUpdater = DictionaryFactory.newConcurrentAtomicObjectDictionary();
        this.nextUpdaterIndex = new AtomicInteger();
        this.updaters = new UpdatableObjectGameUpdater[PROP_EXECUTORS];

        for (int i = 0, length = updaters.length; i < length; i++) {
            updaters[i] = new UpdatableObjectGameUpdater(i + 1);
        }
    }

    /**
     * Add new object ot updating.
     *
     * @param object the new object.
     */
    public void addObject(@NotNull final UpdatableObject object) {

        final GameUpdater<UpdatableObject> executor = nextUpdater();
        final long stamp = objectToUpdater.writeLock();
        try {
            objectToUpdater.put(object, executor);
            executor.addToUpdating(object);
        } finally {
            objectToUpdater.writeUnlock(stamp);
        }
    }

    /**
     * Get the next updater.
     *
     * @return the next updater.
     */
    @NotNull
    private GameUpdater<UpdatableObject> nextUpdater() {
        final int result = nextUpdaterIndex.incrementAndGet();
        if (result < updaters.length) return updaters[result];
        nextUpdaterIndex.set(0);
        return updaters[0];
    }

    /**
     * Remove object from updating.
     *
     * @param object the removed object.
     */
    public void removeObject(@NotNull final UpdatableObject object) {
        final long stamp = objectToUpdater.writeLock();
        try {

            final GameUpdater<UpdatableObject> executor = objectToUpdater.remove(object);

            if (executor == null) {
                LOGGER.warning("not found updater for the object " + object);
                return;
            }

            executor.removeFromUpdating(object);

        } finally {
            objectToUpdater.writeUnlock(stamp);
        }
    }
}
