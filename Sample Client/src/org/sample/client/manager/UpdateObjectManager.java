package org.sample.client.manager;

import org.sample.client.executor.GameExecutor;
import org.sample.client.executor.impl.UpdatableObjectGameExecutor;
import org.sample.client.model.util.UpdatableObject;
import rlib.concurrent.lock.LockFactory;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.ObjectDictionary;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

/**
 * Менеджер по обновлению различных объектов.
 *
 * @author Ronn
 */
public class UpdateObjectManager {

    protected static final Logger LOGGER = LoggerManager.getLogger(UpdateObjectManager.class);

    private static final Runtime RUNTIME = Runtime.getRuntime();

    public static final int PROP_EXECUTORS = RUNTIME.availableProcessors();

    private static UpdateObjectManager instance;

    public static UpdateObjectManager getInstance() {

        if (instance == null) {
            instance = new UpdateObjectManager();
        }

        return instance;
    }

    /**
     * Таблица какой объект в каком исполнителе.
     */
    private final ObjectDictionary<UpdatableObject, GameExecutor<UpdatableObject>> objectToExecutor;

    /**
     * Исполнитель обновлений объектов.
     */
    private final GameExecutor<UpdatableObject>[] executors;

    /**
     * Индекс следующего исполнителя.
     */
    private final AtomicInteger nextExecutorIndex;

    /**
     * Синхронизатор.
     */
    private final Lock lock;

    public UpdateObjectManager() {
        InitializeManager.valid(getClass());

        this.objectToExecutor = DictionaryFactory.newObjectDictionary();
        this.lock = LockFactory.newPrimitiveAtomicLock();
        this.nextExecutorIndex = new AtomicInteger();
        this.executors = new UpdatableObjectGameExecutor[PROP_EXECUTORS];

        for (int i = 0, length = executors.length; i < length; i++) {
            executors[i] = new UpdatableObjectGameExecutor(i + 1);
        }
    }

    /**
     * Добавление на обновление нового объекта,.
     *
     * @param object новый объект.
     */
    public void addObject(final UpdatableObject object) {

        final GameExecutor<UpdatableObject> executor = getNextExecutor();

        final Lock lock = getLock();
        lock.lock();
        try {

            final ObjectDictionary<UpdatableObject, GameExecutor<UpdatableObject>> objectToExecutor = getObjectToExecutor();
            objectToExecutor.put(object, executor);

            executor.execute(object);

        } finally {
            lock.unlock();
        }
    }

    /**
     * @return исполнители обновлений объектов.
     */
    private GameExecutor<UpdatableObject>[] getExecutors() {
        return executors;
    }

    /**
     * @return синхронизатор.
     */
    private Lock getLock() {
        return lock;
    }

    private GameExecutor<UpdatableObject> getNextExecutor() {

        final GameExecutor<UpdatableObject>[] executors = getExecutors();
        final AtomicInteger nextExecutorIndex = getNextExecutorIndex();

        final int result = nextExecutorIndex.incrementAndGet();

        if (result < executors.length) {
            return executors[result];
        }

        nextExecutorIndex.set(0);

        return executors[0];
    }

    /**
     * @return индекс следующего исполнителя.
     */
    private AtomicInteger getNextExecutorIndex() {
        return nextExecutorIndex;
    }

    /**
     * @return словарь какой объект в каком исполнителе.
     */
    private ObjectDictionary<UpdatableObject, GameExecutor<UpdatableObject>> getObjectToExecutor() {
        return objectToExecutor;
    }

    /**
     * Удалить объект из обновлений.
     *
     * @param object удаляемый объект.
     */
    public void removeObject(final UpdatableObject object) {

        final Lock lock = getLock();
        lock.lock();
        try {

            final ObjectDictionary<UpdatableObject, GameExecutor<UpdatableObject>> objectToExecutor = getObjectToExecutor();

            final GameExecutor<UpdatableObject> executor = objectToExecutor.remove(object);
            executor.remove(object);

        } finally {
            lock.unlock();
        }
    }
}
