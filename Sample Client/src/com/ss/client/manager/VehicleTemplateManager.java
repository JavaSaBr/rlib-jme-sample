package com.ss.client.manager;

import com.ss.client.network.Network;
import com.ss.client.network.client.PlayerVehicleTemplateClientPacket;
import com.ss.client.template.NpcVehicleTemplate;
import com.ss.client.template.PlayerVehicleTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.IntegerDictionary;

/**
 * The manager to manage available templates of vehicles.
 *
 * @author JavaSaBr
 */
public class VehicleTemplateManager {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(VehicleTemplateManager.class);

    private static VehicleTemplateManager instance;

    @NotNull
    public static VehicleTemplateManager getInstance() {

        if (instance == null) {
            instance = new VehicleTemplateManager();
        }

        return instance;
    }

    @NotNull
    private final IntegerDictionary<NpcVehicleTemplate> npcTemplates;

    @NotNull
    private final IntegerDictionary<PlayerVehicleTemplate> playerTemplates;

    private VehicleTemplateManager() {
        InitializeManager.valid(getClass());
        this.npcTemplates = DictionaryFactory.newIntegerDictionary();
        this.playerTemplates = DictionaryFactory.newIntegerDictionary();
        LOGGER.info("initialized.");
    }

    /**
     * Get a template of a NPC vehicle.
     *
     * @param templateId the template id.
     * @return the vehicle template or null.
     */
    @Nullable
    public NpcVehicleTemplate getNpcTemplate(final int templateId) {
        return npcTemplates.get(templateId);
    }

    /**
     * Get a template of a player vehicle.
     *
     * @param templateId the template id.
     * @return the vehicle template or null.
     */
    @Nullable
    public PlayerVehicleTemplate getPlayerTemplate(final int templateId) {

        synchronized (playerTemplates) {

            if (!playerTemplates.containsKey(templateId)) {
                final Network network = Network.getInstance();
                network.sendPacketToGameServer(PlayerVehicleTemplateClientPacket.getInstance(templateId));
            }

            while (!playerTemplates.containsKey(templateId)) {
                ConcurrentUtils.waitInSynchronize(playerTemplates);
            }
        }

        return playerTemplates.get(templateId);
    }

    /**
     * Register a received template.
     *
     * @param templateId the template id.
     * @param template the received template.
     */
    public void register(final int templateId, @Nullable final PlayerVehicleTemplate template) {
        synchronized (playerTemplates) {
            playerTemplates.put(templateId, template);
            ConcurrentUtils.notifyAllInSynchronize(playerTemplates);
        }
    }
}
