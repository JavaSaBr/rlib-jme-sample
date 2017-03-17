package com.ss.client.config;

import static java.lang.Math.max;
import static rlib.util.dictionary.DictionaryFactory.newObjectDictionary;
import org.jetbrains.annotations.NotNull;
import rlib.util.array.Array;
import rlib.util.array.ArrayComparator;
import rlib.util.array.ArrayFactory;
import rlib.util.dictionary.ObjectDictionary;

import java.awt.*;

/**
 * The screen size model.
 *
 * @author JavaSabr
 */
public class ScreenSize {

    public static final int SCREEN_SIZE_MAX_WIDTH = 1920;
    public static final int SCREEN_SIZE_MIN_HEIGHT = 700;
    public static final int SCREEN_SIZE_MIN_WIDTH = 1244;

    /**
     * The table of available screen sizes.
     */
    private static final ObjectDictionary<String, ScreenSize> SCREEN_SIZE_TABLE = newObjectDictionary();

    /**
     * The list of available screen sizes.
     */
    private static ScreenSize[] values;

    /**
     * Init the list of available screen sizes.
     */
    public static void init() {

        final GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice device = environment.getDefaultScreenDevice();
        final DisplayMode[] modes = device.getDisplayModes();

        final Array<ScreenSize> container = ArrayFactory.newArraySet(ScreenSize.class);

        int maxWidth = 0;
        int maxHeight = 0;

        for (final DisplayMode mode : modes) {

            maxWidth = max(mode.getWidth(), maxWidth);
            maxHeight = max(mode.getHeight(), maxHeight);

            if (mode.getWidth() < SCREEN_SIZE_MIN_WIDTH || mode.getHeight() < SCREEN_SIZE_MIN_HEIGHT) {
                continue;
            } else if (mode.getWidth() > SCREEN_SIZE_MAX_WIDTH) {
                continue;
            }

            container.add(new ScreenSize(mode.getWidth(), mode.getHeight(), device.isFullScreenSupported()));
        }

        container.add(new ScreenSize(SCREEN_SIZE_MIN_WIDTH, SCREEN_SIZE_MIN_HEIGHT, false));

        if (maxWidth >= 1600 && maxHeight >= 900) {
            container.add(new ScreenSize(1600, 900, false));
        }

        if (maxWidth >= 1366 && maxHeight >= 768) {
            container.add(new ScreenSize(1366, 768, false));
        }

        container.sort(COMPARATOR);

        for (final ScreenSize screenSize : container) {
            SCREEN_SIZE_TABLE.put(screenSize.size, screenSize);
        }

        values = container.toArray(new ScreenSize[container.size()]);
    }

    /**
     * @param size the string presentation of a screen size.
     * @return the screen size.
     */
    @NotNull
    public static ScreenSize sizeOf(@NotNull final String size) {
        final ScreenSize screenSize = SCREEN_SIZE_TABLE.get(size);
        return screenSize == null ? new ScreenSize(SCREEN_SIZE_MIN_WIDTH, SCREEN_SIZE_MIN_HEIGHT, false) : screenSize;
    }

    /**
     * @return the list of available screen sizes.
     */
    public static ScreenSize[] values() {
        return values;
    }

    /**
     * The string presentation.
     */
    @NotNull
    private final String size;

    /**
     * The screen width.
     */
    private final int width;

    /**
     * The screen height.
     */
    private final int height;

    /**
     * The screen size comparator.
     */
    private static final ArrayComparator<ScreenSize> COMPARATOR = (first, second) -> {

        final int firstTotal = first.getHeight() * first.getWidth();
        final int secondTotal = second.getHeight() * second.getWidth();

        return -(firstTotal - secondTotal);
    };

    /**
     * The flag of supporting full screen mode.
     */
    private final boolean fullscreenSupported;

    private ScreenSize(final int width, final int height, final boolean fullscreenSupported) {
        this.width = width;
        this.height = height;
        this.size = width + "x" + height;
        this.fullscreenSupported = fullscreenSupported;
    }

    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        final ScreenSize other = (ScreenSize) obj;

        if (height != other.height) {
            return false;
        } else if (width != other.width) {
            return false;
        }

        return true;
    }

    /**
     * @return the screen height.
     */
    public final int getHeight() {
        return height;
    }

    /**
     * @return the screen width.
     */
    public final int getWidth() {
        return width;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + width;
        return result;
    }

    /**
     * @return the flag of supporting full screen mode..
     */
    public boolean isFullscreenSupported() {
        return fullscreenSupported;
    }

    @Override
    public String toString() {
        return size;
    }
}
