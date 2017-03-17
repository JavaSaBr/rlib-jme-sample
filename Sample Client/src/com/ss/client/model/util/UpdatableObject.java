package com.ss.client.model.util;

import org.jetbrains.annotations.NotNull;
import com.ss.client.util.LocalObjects;

/**
 * The interface to implement updatable object.
 * 
 * @author JavaSaBr
 */
public interface UpdatableObject {

	/**
	 * Handle finishing updating.
	 */
	default void finishUpdating() {
	}

	/**
	 * Update this object.
	 * 
	 * @param local the container of thread local objects.
	 * @param currentTime the current time.
	 * @return true if need to finish updating.
	 */
	default boolean update(@NotNull final LocalObjects local, final long currentTime) {
		return false;
	}
}
