package org.sample.client.model.util;

import org.sample.client.util.LocalObjects;

/**
 * Интерфейс дял реализации обновляемого объекта.
 * 
 * @author Ronn
 */
public interface UpdatableObject {

	/**
	 * Завершение обновлений объекта.
	 */
	public default void finish() {
	}

	/**
	 * Обновление объекта.
	 * 
	 * @param local контейнер локальных объектов.
	 * @param currentTime текущее время.
	 * @return завершил ли объект задачи по обновлению.
	 */
	public default boolean update(final LocalObjects local, final long currentTime) {
		return false;
	}
}
