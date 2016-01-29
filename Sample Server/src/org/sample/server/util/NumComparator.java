package org.sample.server.util;

/**
 * @author Ronn
 */
public interface NumComparator {

    public static final NumComparator EQUALITY = (first, second) -> first == second;

    public static final NumComparator NOT_EQUALITY = new NumComparator() {

        @Override
        public boolean compare(final int first, final int second) {
            return first != second;
        }

        @Override
        public String toString() {
            return "!=";
        }
    };

    public static final NumComparator MORE = new NumComparator() {

        @Override
        public boolean compare(final int first, final int second) {
            return first > second;
        }

        @Override
        public String toString() {
            return ">";
        }
    };

    public static final NumComparator LESS = new NumComparator() {

        @Override
        public boolean compare(final int first, final int second) {
            return first < second;
        }

        @Override
        public String toString() {
            return "<";
        }
    };

    public static final NumComparator MORE_THAN_OR_EQUA = new NumComparator() {

        @Override
        public boolean compare(final int first, final int second) {
            return first >= second;
        }

        @Override
        public String toString() {
            return ">=";
        }
    };

    public static final NumComparator LESS_THAN_OR_EQUA = new NumComparator() {

        @Override
        public boolean compare(final int first, final int second) {
            return first <= second;
        }

        @Override
        public String toString() {
            return "<=";
        }
    };

    /**
     * Сравнение 2х чисел.
     *
     * @param first
     * @param second
     * @return выполняют ли условие 2 числа.
     */
    public boolean compare(int first, int second);
}
