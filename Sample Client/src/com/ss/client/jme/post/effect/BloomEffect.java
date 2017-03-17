package com.ss.client.jme.post.effect;

import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.BloomFilter.GlowMode;
import com.ss.client.config.GameConfig;
import com.ss.client.jme.post.PostEffect;
import com.ss.client.util.LocalObjects;
import com.ss.client.config.GameConfig;
import com.ss.client.jme.post.PostEffect;
import com.ss.client.util.LocalObjects;

/**
 * Реализация блум фильтра.
 *
 * @author Ronn
 */
public class BloomEffect implements PostEffect {

    private static BloomEffect instance;

    public static PostEffect bind(final FilterPostProcessor processor) {
        instance = new BloomEffect(processor);
        return instance;
    }

    public static BloomEffect getInstance() {
        return instance;
    }

    /**
     * Фильт для сцены.
     */
    private volatile BloomFilter sceneFilter;

    /**
     * Фильтр для объектов.
     */
    private volatile BloomFilter objectFilter;

    /**
     * Активирован ли был эффект
     */
    private volatile boolean activated;

    public BloomEffect(final FilterPostProcessor processor) {

        final BloomFilter sceneFilter = new BloomFilter(GlowMode.Scene);
        sceneFilter.setDownSamplingFactor(1);
        sceneFilter.setBloomIntensity(2.0F);
        sceneFilter.setBlurScale(1F);
        sceneFilter.setExposureCutOff(0);
        sceneFilter.setExposurePower(5);
        sceneFilter.setEnabled(false);

        final BloomFilter objectFilter = new BloomFilter(GlowMode.Objects);
        objectFilter.setDownSamplingFactor(1);
        objectFilter.setBloomIntensity(1.0F);
        objectFilter.setBlurScale(0);
        objectFilter.setExposureCutOff(0);
        objectFilter.setExposurePower(6);
        objectFilter.setEnabled(false);

        processor.addFilter(objectFilter);
        processor.addFilter(sceneFilter);

        setObjectFilter(objectFilter);
        setSceneFilter(sceneFilter);
    }

    @Override
    public void activate(final LocalObjects local) {

        final GameConfig config = GameConfig.getInstance();

        sceneFilter.setEnabled(config.isBloomEffect());
        objectFilter.setEnabled(config.isGlowEffect());

        setActivated(true);
    }

    @Override
    public void deactivate(LocalObjects local) {

        if (sceneFilter.isEnabled()) {
            sceneFilter.setEnabled(false);
        }

        if (objectFilter.isEnabled()) {
            objectFilter.setEnabled(false);
        }

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
     * @param objectFilter фильтр для объектов.
     */
    private void setObjectFilter(final BloomFilter objectFilter) {
        this.objectFilter = objectFilter;
    }

    /**
     * @param sceneFilter фильт для сцены.
     */
    private void setSceneFilter(final BloomFilter sceneFilter) {
        this.sceneFilter = sceneFilter;
    }
}
