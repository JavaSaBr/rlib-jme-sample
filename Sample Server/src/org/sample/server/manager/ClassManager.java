package org.sample.server.manager;

import org.sample.server.Config;
import org.sample.server.GameServer;
import rlib.classpath.ClassPathScaner;
import rlib.classpath.ClassPathScannerFactory;
import rlib.compiler.Compiler;
import rlib.compiler.CompilerFactory;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;
import rlib.util.StringUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

import java.nio.file.Paths;

/**
 * Менеджер классов.
 *
 * @author Ronn
 */
public class ClassManager {

    private static final Logger LOGGER = LoggerManager.getLogger(ClassManager.class);

    private static ClassManager instance;

    public static ClassManager getInstance() {

        if (instance == null) {
            instance = new ClassManager();
        }

        return instance;
    }

    /**
     * Компилятор скриптов.
     */
    private final Compiler compiler;

    /**
     * Сканер classpath.
     */
    private final ClassPathScaner scaner;

    private ClassManager() {
        InitializeManager.valid(getClass());

        compiler = CompilerFactory.newDefaultCompiler();
        scaner = ClassPathScannerFactory.newManifestScanner(GameServer.class, "Class-Path");

        final Array<Class<?>> classes = ArrayFactory.newArraySet(Class.class);
        classes.addAll(compiler.compileDirectory(Paths.get(Config.FOLDER_SCRIPTS_PATH)));

        LOGGER.info("compile " + classes.size() + " classes.");

        scaner.scanning();
        scaner.addClasses(classes);
        scaner.getAll(classes.clear());
    }

    /**
     * Поиск реализаций указанного интерфейса.
     *
     * @param container      контейнер классов.
     * @param interfaceClass класс интерфейса, который должен быть реализован.
     */
    public <T, R extends T> void findImplements(final Array<Class<R>> container, final Class<T> interfaceClass) {
        scaner.findImplements(container, interfaceClass);
    }

    /**
     * Поиск класса с указанным названием реализующий указанный интерфейс.
     *
     * @param simpleName     краткое название класса.
     * @param interfaceClass реализовывающий интерфейс.
     * @return искомый класс.
     */
    public <T> Class<T> findImplements(final String simpleName, final Class<T> interfaceClass) {

        final Array<Class<T>> container = ArrayFactory.newArray(Class.class);

        scaner.findImplements(container, interfaceClass);

        if (container.isEmpty()) {
            throw new RuntimeException("not found class for name " + simpleName + " and implemented " + interfaceClass);
        }

        for (final Class<T> cs : container) {
            if (StringUtils.equals(cs.getSimpleName(), simpleName)) {
                return cs;
            }
        }

        throw new RuntimeException("not found class for name " + simpleName + " and implemented " + interfaceClass);
    }

    /**
     * Поиск классов наследников указанного класса.
     *
     * @param container   контейнер классов.
     * @param parentClass родительский класс.
     */
    public <T, R extends T> void findInherited(final Array<Class<R>> container, final Class<T> parentClass) {
        scaner.findInherited(container, parentClass);
    }
}
