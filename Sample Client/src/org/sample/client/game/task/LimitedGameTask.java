package org.sample.client.game.task;

import org.sample.client.util.LocalObjects;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

import java.awt.color.CMMException;

/**
 * Целкическая ограниченная задача.
 *
 * @author Ronn
 */
public abstract class LimitedGameTask implements GameTask {

    protected static final Logger LOGGER = LoggerManager.getLogger(GameTask.class);

    /**
     * Максимальное кол-во раз исполнений.
     */
    private volatile int limit;

    /**
     * Счетчик исполнений.
     */
    private volatile int counter;

    public LimitedGameTask(final int limit) {
        this.limit = limit;
    }

    @Override
    public boolean execute(final LocalObjects local, final long currentTime) {

        try {
            return executeImpl(local, currentTime) || ++counter >= getLimit();
        } catch (final CMMException e) {
            LOGGER.warning(this, e);
            System.exit(1);
        } catch (final Exception e) {
            LOGGER.warning(this, e);
        } finally {
            if (getCounter() == getLimit()) {
                LOGGER.warning(this, "task is not complete");
            }
        }

        return true;
    }

    protected abstract boolean executeImpl(LocalObjects local, long currentTime);

    /**
     * @return счетчик исполнений.
     */
    public int getCounter() {
        return counter;
    }

    /**
     * @param counter счетчик исполнений.
     */
    public void setCounter(final int counter) {
        this.counter = counter;
    }

    /**
     * @return максимальное кол-во раз исполнений.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * @param limit максимальное кол-во раз исполнений.
     */
    public void setLimit(final int limit) {
        this.limit = limit;
    }

    /**
     * @return последнее ли это выполнение.
     */
    protected boolean isLastExecute() {
        return getLimit() - getCounter() < 2;
    }

    /**
     * Реинициализация задачи.
     */
    public void reinit() {
        setCounter(0);
    }
}
