package com.ss.client.model.impl;

import static java.util.Objects.requireNonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The user account.
 *
 * @author JavaSaBr
 */
public final class UserAccount {

    /**
     * The user login.
     */
    @Nullable
    private volatile String name;

    /**
     * The user password.
     */
    @Nullable
    private volatile String password;

    /**
     * Clear data.
     */
    public final void clear() {
        this.name = null;
        this.password = null;
    }

    /**
     * @return the user login.
     */
    @NotNull
    public final String getName() {
        return requireNonNull(name);
    }

    /**
     * @param name the user login.
     */
    public final void setName(@Nullable final String name) {
        this.name = name;
    }

    /**
     * @return the user password.
     */
    @NotNull
    public final String getPassword() {
        return requireNonNull(password);
    }

    /**
     * @param password the user password.
     */
    public final void setPassword(@Nullable final String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserAccount{" + "name='" + name + '\'' + ", password='" + password + '\'' + '}';
    }
}
