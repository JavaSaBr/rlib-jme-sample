package com.ss.server.model;

import com.ss.server.network.model.GameClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.util.pools.PoolFactory;
import rlib.util.pools.Reusable;
import rlib.util.pools.ReusablePool;

/**
 * The implementation of an account of an player.
 *
 * @author JavaSaBr
 */
public final class Account implements Reusable {

    @NotNull
    private static final ReusablePool<Account> POOL = PoolFactory.newConcurrentAtomicARSWLockReusablePool(Account.class);

    /**
     * Create a new account.
     */
    @NotNull
    public static Account newInstance() {
        return POOL.take(Account::new);
    }

    /**
     * The account name.
     */
    @Nullable
    private volatile String name;

    /**
     * The account name in lower case.
     */
    @Nullable
    private volatile String lowerName;

    /**
     * The account password.
     */
    @Nullable
    private volatile String password;

    /**
     * The current game client.
     */
    @Nullable
    private volatile GameClient client;

    /**
     * The uniq id.
     */
    private volatile int id;

    @Override
    public void free() {
        name = null;
        client = null;
        password = null;
        lowerName = null;
        id = 0;
    }

    /**
     * @return the current game client.
     */
    @Nullable
    public final GameClient getClient() {
        return client;
    }

    /**
     * @param client the current game client.
     */
    public final void setClient(@Nullable final GameClient client) {
        this.client = client;
    }

    /**
     * @return the account name.
     */
    @Nullable
    public final String getName() {
        return name;
    }

    /**
     * @param name the account name.
     */
    public final void setName(@Nullable final String name) {
        this.name = name;
    }

    /**
     * @return the account password.
     */
    @Nullable
    public final String getPassword() {
        return password;
    }

    /**
     * @param password the account password.
     */
    public final void setPassword(@Nullable final String password) {
        this.password = password;
    }

    /**
     * @param lowerName the account name in lower case.
     */
    public void setLowerName(@Nullable final String lowerName) {
        this.lowerName = lowerName;
    }

    /**
     * @return the account name in lower case.
     */
    @Nullable
    public String getLowerName() {
        return lowerName;
    }

    /**
     * @return the uniq id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the uniq id.
     */
    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public void release() {
        POOL.put(this);
    }

    @Override
    public String toString() {
        return "Account{" + "name='" + name + '\'' + ", lowerName='" + lowerName + '\'' + ", password='" + password +
                '\'' + ", id=" + id + '}';
    }
}
