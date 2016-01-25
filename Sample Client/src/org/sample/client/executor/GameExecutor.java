package org.sample.client.executor;

/**
 * Исполнитель игровых объектов.
 *
 * @author Ronn
 */
public interface GameExecutor<T> {

    /**
     * Добавление на исполнение объект.
     *
     * @param object добавляемый объект.
     */
    public void execute(T object);

    /**
     * Удаление из исполнения объект.
     *
     * @param object удаляемый объект.
     */
    public void remove(T object);
}
