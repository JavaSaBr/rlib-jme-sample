package com.ss.client.model;

import static rlib.util.ArrayUtils.runInWriteLock;
import static rlib.util.dictionary.DictionaryUtils.runInWriteLock;
import com.ss.client.util.LocalObjects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.array.ConcurrentArray;
import rlib.util.dictionary.ConcurrentLongDictionary;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.DictionaryUtils;
import rlib.util.dictionary.LongDictionary;

/**
 * The class to contain all objects.
 *
 * @author JavaSaBr
 */
public class World {

    @NotNull
    private static final World INSTANCE = new World();

    @NotNull
    public static World getInstance() {
        return INSTANCE;
    }

    /**
     * The table of all game objects in the world.
     */
    @NotNull
    private final ConcurrentLongDictionary<SpawnableObject> objectIdToObject;

    /**
     * The list of all objects in the world.
     */
    @NotNull
    private final ConcurrentArray<GameObject> objects;

    public World() {
        this.objectIdToObject = DictionaryFactory.newConcurrentAtomicLongDictionary();
        this.objects = ArrayFactory.newConcurrentAtomicARSWLockArray(GameObject.class);
    }

    /**
     * Add a new object to the world.
     *
     * @param newObject the new object.
     * @param local     the local objects.
     */
    public void addObject(@NotNull final SpawnableObject newObject, @NotNull final LocalObjects local) {
        runInWriteLock(objectIdToObject, newObject.getObjectId(), newObject, LongDictionary::put);
        runInWriteLock(objects, newObject, Array::add);
    }

    /**
     * Remove the old object from the world.
     *
     * @param oldObject the old object to remove.
     * @param local     the local objects.
     */
    public void removeObject(@NotNull final SpawnableObject oldObject, @NotNull final LocalObjects local) {
        runInWriteLock(objectIdToObject, oldObject.getObjectId(), LongDictionary::remove);
        runInWriteLock(objects, oldObject, Array::fastRemove);
    }

    /**
     * Get an object for the object id.
     *
     * @param objectId the object id.
     * @return the object or null.
     */
    @Nullable
    public GameObject getObject(final long objectId) {
        return DictionaryUtils.getInReadLock(objectIdToObject, objectId, LongDictionary::get);
    }
}
