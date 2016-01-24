package org.sample.client.model.impl;

/**
 * Аккаунт пользователя.
 *
 * @author Ronn
 */
public final class Account {

    /**
     * Логин аккаунта.
     */
    private volatile String name;

    /**
     * Пароль аккаунта.
     */
    private volatile String password;

    /**
     * Очистка.
     */
    public final void clear() {
        this.name = null;
        this.password = null;
    }

    /**
     * @return имя аккаунта.
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name имя аккаунта.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * @return пароль.
     */
    public final String getPassword() {
        return password;
    }

    /**
     * @param password пароль.
     */
    public final void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
