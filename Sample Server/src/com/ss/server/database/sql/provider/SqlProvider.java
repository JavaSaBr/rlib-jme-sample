package com.ss.server.database.sql.provider;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement SQL queries provider.
 *
 * @author JavaSaBr
 */
public interface SqlProvider {

    /**
     * Params: 1 - account name, 2 - password.
     *
     * @return the query to inset a new account.
     */
    @NotNull
    String insertAccountQuery();

    /**
     * Params: 1 - account name.
     *
     * @return the query to select account by account name.
     */
    @NotNull
    String selectAccountByNameQuery();
}
