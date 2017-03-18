package com.ss.client.util;

import javax.annotation.Nullable;
import java.io.InputStream;

/**
 * The utility class.
 *
 * @author JavaSaBr
 */
public abstract class GameUtil {

    /**
     * Get an input stream from classpath for the path.
     *
     * @param path the path to a resource.
     * @return the input stream or null.
     */
    @Nullable
    public static InputStream getInputStream(final String path) {
        return Object.class.getResourceAsStream(path);
    }
}
