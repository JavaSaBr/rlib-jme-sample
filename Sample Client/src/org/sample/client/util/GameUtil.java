package org.sample.client.util;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import rlib.util.StringUtils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Набор полезных утилит для разработки.
 *
 * @author Ronn
 */
public abstract class GameUtil {

    private static final ThreadLocal<SimpleDateFormat> LOCATE_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss:SSS");
        }
    };

    /**
     * Проверка существования ресурса по указанному пути.
     *
     * @param path путь к ресрсу.
     * @return существуетли такой ресурс.
     */
    public static boolean checkExists(final String path) {
        final Class<GameUtil> cs = GameUtil.class;
        return cs.getResourceAsStream(path) != null || cs.getResourceAsStream("/" + path) != null;
    }

    /**
     * Проверка наличия ресурса по указанному пути.
     *
     * @param path интересуемый путь к ресурсу.
     * @return существует ли этот ресурс.
     */
    public static boolean exists(final String path) {
        return Object.class.getResource(path) != null;
    }

    public static Geometry findGeometry(final Spatial spatial) {

        if (!(spatial instanceof Node)) {
            return null;
        }

        final Node node = (Node) spatial;

        for (final Spatial children : node.getChildren()) {

            final Geometry geometry = findGeometry(children);

            if (geometry != null) {
                return geometry;
            }

            if (children instanceof Geometry) {
                return (Geometry) children;
            }
        }

        return null;
    }

    /**
     * Получение угла между 2мя точками.
     *
     * @param center точка угла.
     * @param first  первая точка.
     * @param second вторая точка.
     * @return угол между первой и второй точкой.
     */
    public static float getAngle(final Vector2f center, final Vector2f first, final Vector2f second) {

        final float x = center.getX();
        final float y = center.getY();

        final float ax = first.getX() - x;
        final float ay = first.getY() - y;
        final float bx = second.getX() - x;
        final float by = second.getY() - y;

        final float delta = (float) ((ax * bx + ay * by) / Math.sqrt((ax * ax + ay * ay) * (bx * bx + by * by)));

        if (delta > 1.0) {
            return 0.0F;
        } else if (delta < -1.0) {
            return 180.0F;
        }

        return (float) Math.toDegrees(Math.acos(delta));
    }

    /**
     * @param path путь к ресурсу.
     * @return поток ввода.
     */
    public static InputStream getInputStream(final String path) {
        return Object.class.getResourceAsStream(path);
    }

    public static final String getSplitNumber(final long value, final char split) {

        String string = String.valueOf(value);

        if (string.length() > 3) {

            final StringBuilder builder = new StringBuilder(string.length() * 2);

            final int end = string.startsWith("-") ? 1 : 0;

            for (int i = string.length() - 1, g = 0; i >= end; i--, g++) {

                final char ch = string.charAt(i);

                builder.insert(0, ch);

                if (g == 2 && i > end) {
                    builder.insert(0, split);
                    g = -1;
                }
            }

            if (string.startsWith("-")) {
                builder.insert(0, '-');
            }

            string = builder.toString();
        }

        return string;
    }

    /**
     * Получение имя пользователя текущей системы.
     *
     * @return имя пользователя системы.
     */
    public static final String getUserName() {
        return System.getProperty("user.name");
    }

    /**
     * Есть ли узел с указанным названии.
     */
    public static boolean hasNode(final Spatial spatial, final String nodeName) {

        int i = 0;

        for (Node parent = spatial.getParent(); parent != null; parent = parent.getParent(), i++) {
            if (StringUtils.equals(parent.getName(), nodeName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Видно ли на экране объект с такими экранными координатами.
     *
     * @param position позиция на экране объекта.
     * @param camera   камера экрана
     * @return видно ли на экране.
     */
    public static boolean isVisibleOnScreen(final Vector3f position, final Camera camera) {

        final int maxHeight = camera.getHeight();
        final int maxWidth = camera.getWidth();

        final boolean isBottom = position.getY() < 0;
        final boolean isTop = position.getY() > maxHeight;
        final boolean isLeft = position.getX() < 0;
        final boolean isRight = position.getX() > maxWidth;

        return !isBottom && !isLeft && !isTop && !isRight && position.getZ() < 1F;
    }

    /**
     * Получение сдвинутой точки на нужную дистанцию.
     *
     * @param first  первая точка.
     * @param second вторая точка.
     * @param store  контейнер результата.
     * @param length дистанция сдвига.
     */
    public static final void movePoint(final Vector3f first, final Vector3f second, final Vector3f store, final int length) {
        store.x = first.x + (second.x - first.x) * length;
        store.y = first.y + (second.y - first.y) * length;
        store.z = first.z + (second.z - first.z) * length;
    }

    public static String timeFormat(final long time) {
        final SimpleDateFormat format = LOCATE_DATE_FORMAT.get();
        return format.format(new Date(time));
    }
}
