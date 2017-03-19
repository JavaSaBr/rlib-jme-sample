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
         * Type: long.
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
}
