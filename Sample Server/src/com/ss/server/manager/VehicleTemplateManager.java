package com.ss.server.manager;

import com.ss.server.Config;
import com.ss.server.document.DocumentNpcVehicle;
import com.ss.server.document.DocumentPlayerVehicle;
import com.ss.server.template.NpcVehicleTemplate;
import com.ss.server.template.PlayerVehicleTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.manager.InitializeManager;
import rlib.util.FileUtils;
import rlib.util.array.Array;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.IntegerDictionary;

import java.nio.file.Path;
import java.nio.file.Paths;

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

        final Path vehicleFolder = Paths.get(Config.FOLDER_DATA_PATH, "vehicle");
        final Path npcFolder = vehicleFolder.resolve("npc");
        final Path playerFolder = vehicleFolder.resolve("player");

        final Array<Path> npcFiles = FileUtils.getFiles(npcFolder, "xml");
        final Array<Path> playerFiles = FileUtils.getFiles(playerFolder, "xml");

        npcFiles.forEach(path -> {
            final DocumentNpcVehicle document = new DocumentNpcVehicle(path);
            final Array<NpcVehicleTemplate> result = document.parse();
            result.forEach(template -> npcTemplates.put(template.getTemplateId(), template));
        });

        playerFiles.forEach(path -> {
            final DocumentPlayerVehicle document = new DocumentPlayerVehicle(path);
            final Array<PlayerVehicleTemplate> result = document.parse();
            result.forEach(template -> playerTemplates.put(template.getTemplateId(), template));
        });

        LOGGER.info("initialized " + npcTemplates.size() + " npc templates and " + playerTemplates.size() +
                " player templates.");
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
        return playerTemplates.get(templateId);
    }
}
