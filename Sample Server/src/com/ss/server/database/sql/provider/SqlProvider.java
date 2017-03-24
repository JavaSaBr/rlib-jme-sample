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

    /**
     * Params: 1 - account id.
     *
     * @return the query to inset a new player.
     */
    @NotNull
    String insertPlayerQuery();

    /**
     * Params: 1 - vehicle id, 2 - object id.
     *
     * @return the query to update a current vehicle of a player.
     */
    @NotNull
    String updatePlayerCurrentVehicleQuery();

    /**
     * Params: 1 - object id.
     *
     * @return the query to delete a player.
     */
    @NotNull
    String deletePlayerQuery();

    /**
     * Params: 1 - player id, 2 - template id.
     *
     * @return the query to inset a new player vehicle.
     */
    @NotNull
    String insertPlayerVehicleQuery();
}
