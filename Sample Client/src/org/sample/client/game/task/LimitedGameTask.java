package org.sample.client.game.task;

import org.jetbrains.annotations.NotNull;
import org.sample.client.util.LocalObjects;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.pools.Reusable;

import java.awt.color.CMMException;

/**
 * The cycle task with limit of executing.
 *
 * @author JavaSaBr
 */
public abstract class LimitedGameTask implements GameTask, Reusable {

    @NotNull
    protected static final Logger LOGGER = LoggerManager.getLogger(GameTask.class);

    /**
     * The max count to execute.
     */
    private volatile int limit;

    /**
     * The counter of executing this task.
     */
    private volatile int counter;

    public LimitedGameTask(final int limit) {
        this.limit = limit;
    }

    @Override
    public boolean execute(@NotNull final LocalObjects local, final long currentTime) {

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
     * @return the count of executing this task.
     */
    private int getCounter() {
        return counter;
    }

    /**
     * @return the max count to execute.
     */
    private int getLimit() {
        return limit;
    }

    /**
     * @param limit the max count to execute.
     */
    public void setLimit(final int limit) {
        this.limit = limit;
    }

    /**
     * @return true if it was last execution.
     */
    protected boolean isLastExecute() {
        return getLimit() - getCounter() < 2;
    }

    @Override
    public void free() {
        this.counter = 0;
    }
}
