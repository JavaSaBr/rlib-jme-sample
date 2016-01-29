package org.sample.server.util;

/**
 * Интерфейс для реализации операции над числом.
 *
 * @author Ronn
 */
public interface NumOperation {

    public static final NumOperation SET = (first, second) -> second;
    public static final NumOperation SUB = (first, second) -> first - second;
    public static final NumOperation ADD = (first, second) -> first + second;
    public static final NumOperation MUL = (first, second) -> first * second;
    public static final NumOperation DIV = (first, second) -> {

        if (second == 0) {
            throw new RuntimeException("incorrect operation, div for 0.");
        }

        return first / second;
    };

    /**
     * применить операцию над числом.
     *
     * @param first  число, над которым проводим операцию.
     * @param second число, которым проводим операцию.
     * @return итоговое число.
     */
    public int apply(int first, int second);
}
