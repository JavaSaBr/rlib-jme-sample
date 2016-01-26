package org.sample.client.executor.impl;

import org.sample.client.GameThread;
import org.sample.client.SampleGame;
import org.sample.client.executor.GameExecutor;
import org.sample.client.model.util.UpdatableObject;
import org.sample.client.util.LocalObjects;
import rlib.concurrent.lock.LockFactory;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.Synchronized;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

/**
 * Исполнитель обновления объектов.
 *
 * @author Ronn
 */
public class UpdatableObjectGameExecutor extends GameThread implements Synchronized, GameExecutor<UpdatableObject> {

    private static final Logger LOGGER = LoggerManager.getLogger(UpdatableObjectGameExecutor.class);

    /**
     * Лимит обновлений объектов за одну фазу.
     */
    private static final int UPDATE_LIMIT = 100;

    /**
     * Лимит финиширований объектов за одну фазу.
     */
    private static final int FINISH_LIMIT = 3;

    /**
     * Список исполняемых объектов.
     */
    private final Array<UpdatableObject> objects;

    /**
     * Список на обновление.
     */
    private final Array<UpdatableObject> update;

    /**
     * Список завершенных объектов.
     */
    private final Array<UpdatableObject> finished;

    /**
     * Находится ли исполнитель в ожидании.
     */
    private final AtomicBoolean wait;

    /**
     * Блокировщик.
     */
    private final Lock lock;

    public UpdatableObjectGameExecutor(final int order) {
        this.objects = ArrayFactory.newArray(UpdatableObject.class);
        this.update = ArrayFactory.newArray(UpdatableObject.class);
        this.finished = ArrayFactory.newArray(UpdatableObject.class);
        this.lock = LockFactory.newPrimitiveAtomicLock();
        this.wait = new AtomicBoolean(false);

        setName(UpdatableObjectGameExecutor.class.getSimpleName() + "_" + order);
        setPriority(NORM_PRIORITY - 2);
        start();
    }

    /**
     * Процесс финиширования объектов.
     */
    protected void doFinish(final Array<UpdatableObject> finished, final SampleGame game) {

        final UpdatableObject[] array = finished.array();

        for (int i = 0, length = finished.size(); i < length; ) {

            long time = 0;

            final long stamp = game.asyncLock();
            try {

                time = System.currentTimeMillis();

                for (int count = 0, limit = FINISH_LIMIT; count < limit && i < length; count++, i++) {
                    array[i].finish();
                }

            } catch (final Exception e) {
                LOGGER.warning(e);
            } finally {
                game.asyncUnlock(stamp);
            }
        }
    }

    /**
     * Процесс обновление объектов..
     */
    protected void doUpdate(final Array<UpdatableObject> update, final Array<UpdatableObject> finished, final LocalObjects local, final SampleGame game) {

        final UpdatableObject[] array = update.array();

        for (int i = 0, length = update.size(); i < length; ) {

            long time = 0;

            game.updateGeomStart();
            try {

                time = SampleGame.getCurrentTime();

                for (int count = 0, limit = UPDATE_LIMIT; count < limit && i < length; count++, i++) {

                    final UpdatableObject object = array[i];

                    if (object.update(local, time)) {
                        finished.add(object);
                    }
                }

            } catch (final Exception e) {
                LOGGER.warning(e);
            } finally {
                game.updateGeomEnd();
            }
        }
    }

    @Override
    public void execute(final UpdatableObject object) {
        lock();
        try {

            final Array<UpdatableObject> objects = getObjects();

            if (objects.contains(object)) {
                LOGGER.warning(this, "found duplicate object " + object);
                return;
            }

            objects.add(object);

            final AtomicBoolean wait = getWait();

            if (wait.get()) {
                synchronized (wait) {
                    if (wait.compareAndSet(true, false)) {
                        ConcurrentUtils.notifyAllInSynchronize(wait);
                    }
                }
            }

        } finally {
            unlock();
        }
    }

    /**
     * @return список завершенных объектов.
     */
    public Array<UpdatableObject> getFinished() {
        return finished;
    }

    /**
     * @return список исполняемых объектов.
     */
    public Array<UpdatableObject> getObjects() {
        return objects;
    }

    /**
     * @return список на обновление.
     */
    public Array<UpdatableObject> getUpdate() {
        return update;
    }

    /**
     * @return находится ли исполнитель в ожидании.
     */
    public AtomicBoolean getWait() {
        return wait;
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void remove(final UpdatableObject object) {
        lock();
        try {

            final Array<UpdatableObject> objects = getObjects();
            objects.fastRemove(object);

        } finally {
            unlock();
        }
    }

    @Override
    public void run() {

        final Array<UpdatableObject> update = getUpdate();
        final Array<UpdatableObject> finished = getFinished();
        final Array<UpdatableObject> objects = getObjects();

        final LocalObjects local = getLocalObects();
        final AtomicBoolean wait = getWait();

        final SampleGame game = SampleGame.getInstance();

        while (true) {

            finished.clear();
            update.clear();

            lock();
            try {

                if (objects.isEmpty()) {
                    wait.getAndSet(true);
                } else {
                    update.addAll(objects);
                }

            } finally {
                unlock();
            }

            if (wait.get()) {
                synchronized (wait) {
                    if (wait.get()) {
                        ConcurrentUtils.waitInSynchronize(wait);
                    }
                }
            }

            if (update.isEmpty()) {
                continue;
            }

            // обновление объектов
            doUpdate(update, finished, local, game);

            if (finished.isEmpty()) {
                continue;
            }

            lock();
            try {
                objects.removeAll(finished);
            } finally {
                unlock();
            }

            // завершение эффектов
            doFinish(finished, game);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public void unlock() {
        lock.unlock();
    }
}
