package com.ss.server.database;

/**
 * Information about tables.
 *
 * @author JavaSaBr
 */
public interface DbTables {

    interface AccountTable {

        String TABLE_NAME = "account";

        /**
         * Type: int.
         */
        String ID = "id";

        /**
         * Type: string.
         */
        String NAME = "name";

        /**
         * Type: string.
         */
        String PASSWORD = "password";
    }

    interface PlayerTable {

        String TABLE_NAME = "player";

        /**
         * Type: int.
         */
        String ID = "id";

        /**
         * Type: int.
         */
        String ACCOUNT_ID = "account_id";

        /**
         * Type: int.
         */
        String CURRENT_VEHICLE_ID = "current_vehicle_id";
    }

    interface PlayerVehicleTable {

        String TABLE_NAME = "player_vehicle";

        /**
         * Type: int.
         */
        String ID = "id";

        /**
         * Type: int.
         */
        String PLAYER_ID = "player_id";

        /**
         * Type: int.
         */
        String TEMPLATE_ID = "template_id";
    }
}
