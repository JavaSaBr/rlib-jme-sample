package com.ss.server.manager;

import com.ss.server.Config;
import com.ss.server.GameServer;
import org.jetbrains.annotations.NotNull;
import rlib.classpath.ClassPathScanner;
import rlib.classpath.ClassPathScannerFactory;
import rlib.compiler.Compiler;
import rlib.compiler.CompilerFactory;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

import java.nio.file.Paths;

/**
 * The class manager.
 *
 * @author JavaSaBr
 */
public class ClassManager {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(ClassManager.class);

    private static ClassManager instance;

    @NotNull
    public static ClassManager getInstance() {

        if (instance == null) {
            instance = new ClassManager();
        }

        return instance;
    }

    /**
     * The compiler.
     */
    @NotNull
    private final Compiler compiler;

    /**
     * The classpath scanner.
     */
    @NotNull
    private final ClassPathScanner scanner;

    private ClassManager() {
        InitializeManager.valid(getClass());

        compiler = CompilerFactory.newDefaultCompiler();
        scanner = ClassPathScannerFactory.newManifestScanner(GameServer.class, "Class-Path");

        final Array<Class<?>> classes = ArrayFactory.newArraySet(Class.class);
        classes.addAll(compiler.compileDirectory(Paths.get(Config.FOLDER_SCRIPTS_PATH)));

        LOGGER.info("compile " + classes.size() + " classes.");

        scanner.scanning(this::isNeedToScan);
        scanner.addClasses(classes);
    }

    @NotNull
    private Boolean isNeedToScan(final String path) {
        if(path.contains("jre")) return false;
        return true;
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
