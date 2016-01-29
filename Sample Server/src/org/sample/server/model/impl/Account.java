package org.sample.server.model.impl;

import org.sample.server.network.model.GameClient;
import rlib.util.pools.Foldable;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.PoolFactory;

/**
 * Реализация аккаунта игрока.
 *
 * @author Ronn
 */
public final class Account implements Foldable {

    private static final FoldablePool<Account> POOL = PoolFactory.newAtomicFoldablePool(Account.class);

    /**
     * Создание нового аккаунта.
     */
    public static Account newInstance() {

        Account account = POOL.take();

        if (account == null) {
            account = new Account();
        }

        return account;
    }

    /**
     * Имя аккаунта.
     */
    private volatile String name;

    /**
     * Имя в нижнем регистре.
     */
    private volatile String lowerName;

    /**
     * Пароль аккаунта.
     */
    private volatile String password;

    /**
     * Ссылка на клиент аккаунта.
     */
    private volatile GameClient client;

    /**
     * Уникальный ид аккаунта.
     */
    private volatile int id;

    @Override
    public void finalyze() {
        name = null;
        client = null;
        password = null;
        lowerName = null;
        id = 0;
    }

    /**
     * @return ссылка на клиент аккаунта.
     */
    public final GameClient getClient() {
        return client;
    }

    /**
     * @param client ссылка на клиент аккаунта.
     */
    public final void setClient(final GameClient client) {
        this.client = client;
    }

    /**
     * @return название аккаунта.
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name название аккаунта.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * @return пароль аккаунта.
     */
    public final String getPassword() {
        return password;
    }

    /**
     * @param password пароль аккаунта.
     */
    public final void setPassword(final String password) {
        this.password = password;
    }

    /**
     * @param lowerName Имя в нижнем регистре.
     */
    public void setLowerName(final String lowerName) {
        this.lowerName = lowerName;
    }

    /**
     * @return Имя в нижнем регистре.
     */
    public String getLowerName() {
        return lowerName;
    }

    /**
     * @return уникальный ид аккаунта.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id уникальный ид аккаунта.
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
        return "Account{" +
                "name='" + name + '\'' +
                ", lowerName='" + lowerName + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}
