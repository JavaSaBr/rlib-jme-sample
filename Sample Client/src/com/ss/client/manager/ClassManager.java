package com.ss.client.manager;

import com.ss.client.GameClient;
import org.jetbrains.annotations.NotNull;
import rlib.classpath.ClassPathScanner;
import rlib.classpath.ClassPathScannerFactory;
import rlib.manager.InitializeManager;
import rlib.util.array.Array;

/**
 * The class manager.
 *
 * @author JavaSaBr
 */
public class ClassManager {

    private static ClassManager instance;

    @NotNull
    public static ClassManager getInstance() {

        if (instance == null) {
            instance = new ClassManager();
        }

        return instance;
    }

    /**
     * The classpath scanner.
     */
    @NotNull
    private final ClassPathScanner scanner;

    private ClassManager() {
        InitializeManager.valid(getClass());

        scanner = ClassPathScannerFactory.newManifestScanner(GameClient.class, "Class-Path");
        scanner.scanning(path -> true);
    }

    /**
     * Find all implementations.
     *
     * @param container      the container.
     * @param interfaceClass the interface class.
     */
    public <T, R extends T> void findImplements(@NotNull final Array<Class<R>> container,
                                                @NotNull final Class<T> interfaceClass) {
        scanner.findImplements(container, interfaceClass);
    }

    /**
     * Find all inherited classes.
     *
     * @param container   the container.
     * @param parentClass the parent class.
     */
    public <T, R extends T> void findInherited(final Array<Class<R>> container, final Class<T> parentClass) {
        scanner.findInherited(container, parentClass);
    }
}
