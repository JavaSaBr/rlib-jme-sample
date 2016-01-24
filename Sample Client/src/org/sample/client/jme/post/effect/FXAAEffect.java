package org.sample.client.jme.post.effect;

import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FXAAFilter;
import org.sample.client.config.GameConfig;
import org.sample.client.jme.post.PostEffect;
import org.sample.client.util.LocalObjects;

/**
 * Реализация эффекта сглаживания.
 *
 * @author Ronn
 */
public class FXAAEffect implements PostEffect {

    private static FXAAEffect instance;

    public static PostEffect bind(final FilterPostProcessor processor) {
        instance = new FXAAEffect(processor);
        return instance;
    }

    public static FXAAEffect getInstance() {
        return instance;
    }

    /**
     * Фильтр сглаживания.
     */
    private volatile FXAAFilter filter;

    /**
     * Активирован ли был эффект.
     */
    private volatile boolean activated;

    public FXAAEffect(final FilterPostProcessor processor) {

        final FXAAFilter filter = new FXAAFilter();
        filter.setEnabled(false);
        filter.setSubPixelShift(1.0f / 4.0f);
        filter.setVxOffset(0.0f);
        filter.setSpanMax(8.0f);
        filter.setReduceMul(1.0f / 8.0f);

        processor.addFilter(filter);

        setFilter(filter);
    }

    @Override
    public void activate(final LocalObjects local) {

        final GameConfig config = GameConfig.getInstance();

        filter.setEnabled(config.isSmoothing());

        setActivated(true);
    }

    @Override
    public void deactivate(final LocalObjects local) {
        filter.setEnabled(false);
        setActivated(false);
    }

    /**
     * @return активирован ли был эффект.
     */
    private boolean isActivated() {
        return activated;
    }

    /**
     * @param activated активирован ли был эффект.
     */
    private void setActivated(boolean activated) {
        this.activated = activated;
    }

    /**
     * @param filter фильтр сглаживания.
     */
    public void setFilter(final FXAAFilter filter) {
        this.filter = filter;
    }
}
