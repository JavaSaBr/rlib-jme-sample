package org.sample.server.network;

import rlib.network.packet.Packet;

/**
 * Интерфейс для реализации фабрики пакетов.
 *
 * @author Ronn
 */
public interface PacketFactory<C, T extends Packet<C>, K> {

    /**
     * Создание нового пакета по указанному типу.
     *
     * @param type тип создаваемого пакета.
     * @return новый созданный пакет.
     */
    public <R extends T> R create(K type);
}
