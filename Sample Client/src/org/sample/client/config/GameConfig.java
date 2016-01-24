package org.sample.client.config;

import com.jme3.asset.AssetEventListener;
import com.jme3.asset.AssetKey;
import com.jme3.asset.TextureKey;
import com.jme3.system.AppSettings;
import org.sample.client.GameContext;
import org.sample.client.SampleGame;

import java.awt.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static java.lang.Math.max;

/**
 * Набор настроек, изменяемых пользователем во время игры.
 *
 * @author Ronn
 */
public final class GameConfig implements AssetEventListener {

    public static final String GAME_NAME = "Sample Game";;
    public static final String SOUND_ALIAS = "Sound";
    public static final String GRAPHICS_ALIAS = "Graphics";

    public static final String PREF_SOUND_EFFECT_VOLUME = SOUND_ALIAS + "." + "effectVolume";
    public static final String PREF_SOUND_MUSIC_VOLUME = SOUND_ALIAS + "." + "musicVolume";

    public static final String PREF_GRAPHIC_VIEW_DISTANCE = GRAPHICS_ALIAS + "." + "viewDistance";
    public static final String PREF_GRAPHIC_SCREEN_SIZE = GRAPHICS_ALIAS + "." + "screenSize";
    public static final String PREF_GRAPHIC_FULLSCREEN = GRAPHICS_ALIAS + "." + "fullscreen";
    public static final String PREF_GRAPHIC_SMOOTHING = GRAPHICS_ALIAS + "." + "smoothing";
    public static final String PREF_GRAPHIC_LIGHT_EFFECT = GRAPHICS_ALIAS + "." + "lightEffects";
    public static final String PREF_GRAPHIC_BLOOM_EFFECT = GRAPHICS_ALIAS + "." + "bloomEffects";
    public static final String PREF_GRAPHIC_GLOW_EFFECT = GRAPHICS_ALIAS + "." + "glowEffects";
    public static final String PREF_GRAPHIC_V_SYNC = GRAPHICS_ALIAS + "." + "vSync";

    private static GameConfig instance;

    public static GameConfig getInstance() {

        if (instance == null) {

            final GameConfig config = new GameConfig();
            config.init();

            instance = config;
        }

        return instance;
    }

    /**
     * Используемое разрешение экрана.
     */
    private ScreenSize screenSize;

    /**
     * Громкость музыки.
     */
    private float musicVolume;

    /**
     * Громкость эффектов.
     */
    private float effectVolume;

    /**
     * Дальность видимости объектов.
     */
    private float viewDistance;

    /**
     * Полноэкранный ли режим.
     */
    private boolean fullscreen;

    /**
     * Включен ли эффетк Bloom.
     */
    private boolean bloomEffect;

    /**
     * Включен ли эффект подсветки.
     */
    private boolean glowEffect;

    /**
     * Включены ли световые эффекты.
     */
    private boolean lightEffect;

    /**
     * Включено ли сглаживание.
     */
    private boolean smoothing;

    /**
     * Нужна ли вертикальная синхронизация.
     */
    private boolean needVSync;

    @Override
    @SuppressWarnings("rawtypes")
    public void assetDependencyNotFound(final AssetKey parentKey, final AssetKey dependentAssetKey) {
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void assetLoaded(final AssetKey key) {
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void assetRequested(final AssetKey key) {

        if (key instanceof TextureKey) {
            //TODO можно указывать анизатропную фильтрацию
            ((TextureKey) key).setAnisotropy(0);
        }
    }

    /**
     * @return громкость эффектов.
     */
    public float getEffectVolume() {
        return effectVolume;
    }

    /**
     * @param effectVolume громкость эффектов.
     */
    public void setEffectVolume(final float effectVolume) {
        this.effectVolume = effectVolume;
    }

    /**
     * @return громкость музыки.
     */
    public float getMusicVolume() {
        return musicVolume;
    }

    /**
     * @param musicVolume громкость музыки.
     */
    public void setMusicVolume(final float musicVolume) {
        this.musicVolume = musicVolume;
    }

    /**
     * @return используемое разрешение экрана.
     */
    public ScreenSize getScreenSize() {
        return screenSize;
    }

    /**
     * @param screenSize используемое разрешение экрана.
     */
    public void setScreenSize(final ScreenSize screenSize) {
        this.screenSize = screenSize;
    }

    /**
     * @return настройки движка.
     */
    public AppSettings getSettings() {

        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice device = graphicsEnvironment.getDefaultScreenDevice();
        final DisplayMode displayMode = device.getDisplayMode();

        final AppSettings settings = new AppSettings(true);

        settings.setRenderer("CUSTOM" + GameContext.class.getName());
        settings.setTitle(GAME_NAME);
        settings.setFullscreen(screenSize.isFullscreenSupported() && isFullscreen());
        settings.setResolution(screenSize.getWidth(), screenSize.getHeight());
        settings.setFrequency(displayMode.getRefreshRate());

        if (isNeedVSync()) {
            settings.setVSync(true);
            settings.setFrameRate(max(displayMode.getRefreshRate(), 40));
        } else {
           settings.setFrameRate(60);
        }

        return settings;
    }

    /**
     * @return дальность видимости объектов.
     */
    public float getViewDistance() {
        return viewDistance;
    }

    /**
     * @param viewDistance дальность видимости объектов.
     */
    public void setViewDistance(final float viewDistance) {
        this.viewDistance = viewDistance;
    }

    /**
     * Инициализация.
     */
    private void init() {

        final Preferences prefs = Preferences.userNodeForPackage(SampleGame.class);


        this.screenSize = ScreenSize.sizeOf(prefs.get(PREF_GRAPHIC_SCREEN_SIZE, "1244x700"));
        this.fullscreen = prefs.getBoolean(PREF_GRAPHIC_FULLSCREEN, false);
        this.viewDistance = prefs.getFloat(PREF_GRAPHIC_VIEW_DISTANCE, 100000);

        this.bloomEffect = prefs.getBoolean(PREF_GRAPHIC_BLOOM_EFFECT, true);
        this.glowEffect = prefs.getBoolean(PREF_GRAPHIC_GLOW_EFFECT, false);
        this.lightEffect = prefs.getBoolean(PREF_GRAPHIC_LIGHT_EFFECT, false);
        this.smoothing = prefs.getBoolean(PREF_GRAPHIC_SMOOTHING, false);
        this.needVSync = prefs.getBoolean(PREF_GRAPHIC_V_SYNC, false);

        this.musicVolume = prefs.getFloat(PREF_SOUND_MUSIC_VOLUME, 1F);
        this.effectVolume = prefs.getFloat(PREF_SOUND_EFFECT_VOLUME, 1F);
    }

    /**
     * @return включен ли эффетк Bloom.
     */
    public boolean isBloomEffect() {
        return bloomEffect;
    }

    /**
     * @param bloomEffect включен ли эффетк Bloom.
     */
    public void setBloomEffect(final boolean bloomEffect) {
        this.bloomEffect = bloomEffect;
    }

    /**
     * @return полноэкранный ли режим.
     */
    public boolean isFullscreen() {
        return fullscreen;
    }

    /**
     * @param fullscreen полноэкранный ли режим.
     */
    public void setFullscreen(final boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    /**
     * @return включен ли эффект подсветки.
     */
    public boolean isGlowEffect() {
        return glowEffect;
    }

    /**
     * @param glowEffects включен ли эффект подсветки.
     */
    public void setGlowEffect(final boolean glowEffects) {
        this.glowEffect = glowEffects;
    }

    /**
     * @return включены ли световые эффекты.
     */
    public boolean isLightEffect() {
        return lightEffect;
    }

    /**
     * @param lightEffects включены ли световые эффекты.
     */
    public void setLightEffect(final boolean lightEffects) {
        this.lightEffect = lightEffects;
    }

    /**
     * @return нужна ли вертикальная синхронизация.
     */
    public boolean isNeedVSync() {
        return needVSync;
    }

    /**
     * @param needVSync нужна ли вертикальная синхронизация.
     */
    public void setNeedVSync(boolean needVSync) {
        this.needVSync = needVSync;
    }

    /**
     * @return включено ли сглаживание.
     */
    public boolean isSmoothing() {
        return smoothing;
    }

    /**
     * @param smoothing включено ли сглаживание.
     */
    public void setSmoothing(final boolean smoothing) {
        this.smoothing = smoothing;
    }

    /**
     * Сохранения настроек.
     */
    public void save() {

        final Preferences prefs = Preferences.userNodeForPackage(SampleGame.class);

        prefs.put(PREF_GRAPHIC_SCREEN_SIZE, getScreenSize().toString());
        prefs.putBoolean(PREF_GRAPHIC_FULLSCREEN, isFullscreen());

        prefs.putFloat(PREF_GRAPHIC_VIEW_DISTANCE, getViewDistance());

        prefs.putBoolean(PREF_GRAPHIC_BLOOM_EFFECT, isBloomEffect());
        prefs.putBoolean(PREF_GRAPHIC_GLOW_EFFECT, isGlowEffect());
        prefs.putBoolean(PREF_GRAPHIC_LIGHT_EFFECT, isLightEffect());
        prefs.putBoolean(PREF_GRAPHIC_SMOOTHING, isSmoothing());
        prefs.putBoolean(PREF_GRAPHIC_V_SYNC, isNeedVSync());

        prefs.putFloat(PREF_SOUND_EFFECT_VOLUME, getEffectVolume());
        prefs.putFloat(PREF_SOUND_MUSIC_VOLUME, getMusicVolume());

        try {
            prefs.flush();
        } catch (final BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }
}
