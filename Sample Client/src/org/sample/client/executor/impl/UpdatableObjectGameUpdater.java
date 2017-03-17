package org.sample.client.executor.impl;

import org.jetbrains.annotations.NotNull;
import org.sample.client.GameThread;
import org.sample.client.SampleGame;
import org.sample.client.executor.GameUpdater;
import org.sample.client.model.util.UpdatableObject;
import org.sample.client.util.LocalObjects;
import rlib.concurrent.lock.LockFactory;
import rlib.concurrent.lock.Lockable;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

/**
 * The implementation of an updated game objects.
 *
 * @author JavaSaBr
 */
public class UpdatableObjectGameUpdater extends GameThread implements Lockable, GameUpdater<UpdatableObject> {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(UpdatableObjectGameUpdater.class);

    /**
     * The limit to update objects per iteration.
     */
    private static final int UPDATE_LIMIT = 100;

    /**
     * The limit to finishUpdating objects per iteration.
     */
    private static final int FINISH_LIMIT = 3;

    /**
     * The general list of objects to update.
     */
    @NotNull
    private final Array<UpdatableObject> objects;

    /**
     * The list of object to update for one iteration.
     */
    @NotNull
    private final Array<UpdatableObject> update;

    /**
     * The list of finished objects.
     */
    @NotNull
    private final Array<UpdatableObject> finished;

    /**
     * The flag of waiting new objects.
     */
    @NotNull
    private final AtomicBoolean wait;

    /**
     * The lock.
     */
    @NotNull
    private final Lock lock;

    public UpdatableObjectGameUpdater(final int order) {
        this.objects = ArrayFactory.newArray(UpdatableObject.class);
        this.update = ArrayFactory.newArray(UpdatableObject.class);
        this.finished = ArrayFactory.newArray(UpdatableObject.class);
        this.lock = LockFactory.newAtomicLock();
        this.wait = new AtomicBoolean(false);

        setName(UpdatableObjectGameUpdater.class.getSimpleName() + "_" + order);
        setPriority(NORM_PRIORITY - 2);
        start();
    }

    /**
     * Finish objects.
     */
    private void finish(@NotNull final Array<UpdatableObject> finished, @NotNull final SampleGame game) {

        final UpdatableObject[] array = finished.array();

        for (int i = 0, length = finished.size(); i < length; ) {
            final long stamp = game.asyncLock();
            try {

                for (int count = 0; count < FINISH_LIMIT && i < length; count++, i++) {
                    array[i].finishUpdating();
                }

            } catch (final Exception e) {
                LOGGER.warning(e);
            } finally {
                game.asyncUnlock(stamp);
            }
        }
    }

    /**
     * Update game objects.
     */
    private void update(@NotNull final Array<UpdatableObject> update, @NotNull final Array<UpdatableObject> finished,
                        @NotNull final LocalObjects local, @NotNull final SampleGame game) {

        final UpdatableObject[] array = update.array();

        long time;

        for (int i = 0, length = update.size(); i < length; ) {
            game.updateGeomStart();
            try {

                time = SampleGame.getCurrentTime();

                for (int count = 0; count < UPDATE_LIMIT && i < length; count++, i++) {

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
    public void addToUpdating(@NotNull final UpdatableObject object) {
        lock();
        try {

            final Array<UpdatableObject> objects = getObjects();

            if (objects.contains(object)) {
                LOGGER.warning(this, "found duplicate object " + object);
                return;
            }

            objects.add(object);

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
     * @return the list of finished objects.
     */
    @NotNull
    private Array<UpdatableObject> getFinished() {
        return finished;
    }

    /**
     * @return the general list of objects to update.
     */
    @NotNull
    private Array<UpdatableObject> getObjects() {
        return objects;
    }

    /**
     * @return the list of object to update for one iteration.
     */
    @NotNull
    private Array<UpdatableObject> getUpdate() {
        return update;
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void removeFromUpdating(@NotNull final UpdatableObject object) {
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

        final SampleGame game = SampleGame.getInstance();

        for(;;) {

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

            update(update, finished, local, game);

            if (finished.isEmpty()) {
                continue;
            }

            lock();
            try {
                objects.removeAll(finished);
            } finally {
                unlock();
            }

            finish(finished, game);
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
