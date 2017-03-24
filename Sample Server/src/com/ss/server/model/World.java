package com.ss.server.model;

import static rlib.util.ArrayUtils.runInReadLock;
import static rlib.util.dictionary.DictionaryUtils.runInWriteLock;
import com.ss.server.LocalObjects;
import com.ss.server.model.player.Player;
import com.ss.server.model.player.PlayerVehicle;
import com.ss.server.network.ServerPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.array.ConcurrentArray;
import rlib.util.dictionary.ConcurrentLongDictionary;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.DictionaryUtils;
import rlib.util.dictionary.LongDictionary;

/**
 * The class to contain all objects.
 *
 * @author JavaSaBr
 */
public class World {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(World.class);

    @NotNull
    private static final World INSTANCE = new World();

    @NotNull
    public static World getInstance() {
        return INSTANCE;
    }

    /**
     * The table of all game objects in the world.
     */
    @NotNull
    private final ConcurrentLongDictionary<SpawnableObject> objectIdToObject;

    /**
     * The table of all players in the world.
     */
    @NotNull
    private final ConcurrentLongDictionary<Player> players;

    /**
     * The list of all objects in the world.
     */
    @NotNull
    private final ConcurrentArray<GameObject> objects;

    public World() {
        this.objectIdToObject = DictionaryFactory.newConcurrentAtomicLongDictionary();
        this.players = DictionaryFactory.newConcurrentAtomicLongDictionary();
        this.objects = ArrayFactory.newConcurrentAtomicARSWLockArray(GameObject.class);
    }

    /**
     * Add a new object to the world.
     *
     * @param newObject the new object.
     * @param local     the local objects.
     */
    public void addObject(@NotNull final SpawnableObject newObject, @NotNull final LocalObjects local) {

        runInWriteLock(objectIdToObject, newObject.getObjectId(), newObject, LongDictionary::put);

        final long stamp = objects.writeLock();
        try {

            for (final GameObject object : objects.array()) {
                if (object == null) {
                    break;
                }

                if (!(object instanceof PlayerVehicle)) {
                    continue;
                }

                final PlayerVehicle vehicle = (PlayerVehicle) object;
                final Player player = vehicle.getPlayer();

                if (player == null) {
                    LOGGER.warning("not found a player in the " + vehicle);
                    continue;
                }

                // show the new object to the player
                object.showMeTo(player, local);
            }

            objects.add(newObject);

        } finally {
            objects.writeUnlock(stamp);
        }
    }

    /**
     * Remove the old object from the world.
     *
     * @param oldObject the old object to remove.
     * @param local     the local objects.
     */
    public void removeObject(@NotNull final SpawnableObject oldObject, @NotNull final LocalObjects local) {

        runInWriteLock(objectIdToObject, oldObject.getObjectId(), LongDictionary::remove);

        final long stamp = objects.writeLock();
        try {

            objects.fastRemove(oldObject);

            for (final GameObject object : objects.array()) {
                if (object == null) {
                    break;
                }

                if (!(object instanceof PlayerVehicle)) {
                    continue;
                }

                final PlayerVehicle vehicle = (PlayerVehicle) object;
                final Player player = vehicle.getPlayer();

                if (player == null) {
                    LOGGER.warning("not found a player in the " + vehicle);
                    continue;
                }

                // remove the old object from the player
                object.hideMeTo(player, local);
            }

        } finally {
            objects.writeUnlock(stamp);
        }
    }

    /**
     * Broadcast the server packet to all players.
     *
     * @param exclude the exclude object.
     * @param packet  the server packet.
     * @param local   the local objects.
     */
    public void broadcastPacket(@Nullable final GameObject exclude, @NotNull final ServerPacket packet,
                                @NotNull final LocalObjects local) {

        final Array<GameObject> temp = local.nextObjectArray();
        try {

            // copy all objects to thread local array
            runInReadLock(objects, temp, (source, target) -> target.addAll(source));

            // increase the count of sendings to avoid storing the packet to reuse pool during broadcasting
            packet.increaseSends();

            for (final GameObject object : temp.array()) {

                if (object == null) {
                    break;
                } else if (object == exclude) {
                    continue;
                }

                object.sendPacket(packet, local);
            }

            // complete 1 sending
            packet.complete();

        } finally {
            temp.clear();
        }
    }

    /**
     * Register a new player to the world.
     *
     * @param player the new player.
     */
    public void registerPlayer(@NotNull final Player player) {
        runInWriteLock(players, player.getObjectId(), player, LongDictionary::put);
    }

    /**
     * Unregister the player from the world.
     *
     * @param player the old player.
     */
    public void unregisterPlayer(@NotNull final Player player) {
        runInWriteLock(players, player.getObjectId(), LongDictionary::remove);
    }

    /**
     * Get a player for the object id.
     *
     * @param objectId the object id.
     * @return the player or null.
     */
    @Nullable
    public Player getPlayer(final long objectId) {
        return DictionaryUtils.getInReadLock(players, objectId, LongDictionary::get);
    }


    /**
     * Get an object for the object id.
     *
     * @param objectId the object id.
     * @return the object or null.
     */
    @Nullable
    public GameObject getObject(final long objectId) {
        return DictionaryUtils.getInReadLock(objectIdToObject, objectId, LongDictionary::get);
    }
}
