package org.sample.client.network;

import com.ss.client.network.packet.server.*;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.PoolFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Перечисление типов серверных пакетов.
 *
 * @author Ronn
 */
public enum ServerPacketType {
    LOGIN_SERVER_CONNECTED(0x1000, new LoginServerConnected()),
    RESPONSE_REGISTER(0x1001, new LoginResponseRegister()),
    RESPONSE_AUTH(0x1002, new LoginResponseAuth()),
    RESPONSE_SERVER_LIST(0x1003, new LoginResponseServerList()),

    REQUEST_AUTH(0x3000, new RequestAuth()),
    RESPONSE_SERVER_AUTH_SUCCESSFUL(0x3001, new AuthSuccessful()),
    RESPONSE_PILOT_LIST(0x3002, new ResponsePilotList()),
    RESPONSE_CREATE_PILOT(0x3003, new ResponseCreatePilot()),
    RESPONSE_ENTER_INFO(0x3004, new ResponseEnterInfo()),
    RESPONSE_SKILL_PANEL(0x3005, new ResponseSkillPanel()),
    RESPONSE_SKILL_ACTIVE(0x3006, new ResponseSkillActive()),
    RESPONSE_SHIP_FLY(0x3007, new ResponseShipFly()),
    RESPONSE_SHIP_ROTATE(0x3008, new ResponseShipRotation()),
    RESPONSE_PLAYER_SHIP_INFO(0x3009, new ResponsePlayerShipInfo()),
    RESPONSE_SAY_MESSAGE(0x3010, new ResponseSayMessage()),
    RESPONSE_STATION_INTERNAL_MODULE_LIST(0x3011, new ResponseStationInternalModuleList()),
    RESPONSE_STATION_INFO(0x3012, new ResponseSpaceStationInfo()),
    RESPONSE_OBJECT_DELETE(0x3013, new ResponseObjectDelete()),
    RESPONSE_INTERFACE(0x3014, new ResponseInterface()),
    RESPONSE_SKILL_LIST(0x3015, new ResponseSkillList()),
    RESPONSE_CHANGE_STATION_RADIUS(0x3016, new ResponseChangeStationRadius()),
    RESPONSE_GRAVITY_OBJECT_INFO(0x3018, new ResponseGravityObjectInfo()),
    RESPONSE_GRAVITY_OBJECT_POS(0x3019, new ResponseGravityObjectPos()),

    RESPONSE_PLAYER_SHIP_TEMPLATE(0x3024, new ResponsePlayerShipTemplate()),
    RESPONSE_MODULE_TEMPLATE(0x3025, new ResponseModuleTemplate()),
    RESPONSE_GRAVITY_OBJECT_TEMPLATE(0x3026, new ResponseGravityObjectTemplate()),
    RESPONSE_LOCATION_TABLE(0x3027, new ResponseLocationTable()),
    RESPONSE_STATION_TEMPLATE(0x3028, new ResponseStationTemplate()),
    RESPONSE_SKILL_TEMPLAE(0x3029, new ResponseSkillTemplate()),
    RESPONSE_UPDATE_GRAVITY_OBJECT(0x3030, new ResponseUpdateGravityObject()),
    RESPONSE_QUEST_DIALOG_CLOSE(0x3031, new ResponseQuestDialogClose()),

    RESPONSE_QUEST_LIST(0x3033, new ResponseQuestList()),
    RESPONSE_QUEST_STATE_INFO(0x3034, new ResponseQuestStateInfo()),
    RESPONSE_OPEN_COMPONENT(0x3035, new ResponseOpenComponent()),
    RESPONSE_COMMON_ITEM_TEMPLATE(0x3036, new ResponseCommonItemTemplate()),
    RESPONSE_ITEM_INFO(0x3037, new ResponseItemInfo()),
    RESPONSE_UPDATE_STORAGE_CELL(0x3038, new ResponseUpdateStorageCell()),
    RESPONSE_STORAGE_ITEMS(0x3039, new ResponseStorageItems()),
    RESPONSE_BLASTER_SHOT_INFO(0x3040, new ResponseBlasterShotInfo()),
    RESPONSE_SHOT_END(0x3041, new ResponseShotEnd()),
    RESPONSE_SHOT_HIT_INFO(0x3043, new ResponseShotHitInfo()),
    RESPONSE_SERVER_TIME(0x3044, new ResponseServerTime()),

    RESPONSE_NPS_INFO(0x3046, new ResponseNpsInfo()),
    RESPONSE_NPS_TEMPLATE(0x3047, new ResponseNpsTemplate()),
    RESPONSE_DEBUG_OBJECT_ROTATION(0x3048, new ResponseDebugObjectRotation()),
    RESPONSE_DEBUF_OBJECT_POSITION(0x3049, new ResponseDebugObjectPosition()),
    RESPONSE_PLAYER_SHIP_STATE(0x3050, new ResponsePlayerShipState()),
    RESPONSE_OBJECT_DESTRUCTED(0x3051, new ResponseObjectDestructed()),
    RESPONSE_SHIP_DELETE(0x3052, new ResponseShipDelete()),
    RESPONSE_ITEM_DELETE(0x3053, new ResponseItemDelete()),
    RESPONSE_PLAYER_SHIP_TELEPORT(0x3054, new ResponsePlayerShipTeleport()),
    RESPONSE_SHIP_STATUS(0x3055, new ResponseShipStatus()),
    RESPONSE_PLAYER_SHIP_STATUS(0x3056, new ResponsePlayerShipStatus()),
    RESPONSE_ENGINE_POWER(0x3057, new ResponseEnginePower()),
    RESPONSE_ROCKET_SHOT_INFO(0x3058, new ResponseRocketShotInfo()),
    RESPONSE_OBJECT_FRIEND_STATUS(0x3059, new ResponseObjectFriendStatus()),
    RESPONSE_RESPONSE_LOCATION_OBJECT_TEMPLATE(0x3060, new ResponseLocationObjectTemplate()),
    RESPONSE_LOCATION_OBJECT_INFO(0x3061, new ResponseLocationObjectInfo()),
    RESPONSE_ITEM_PANEL(0x3062, new ResponseItemPanel()),
    RESPONSE_CHANGE_EXP(0x3063, new ResponseChangeExp()),
    RESPONSE_CHANGE_LEVEL(0x3064, new ResponseChangeLevel()),
    RESPONSE_QUEST_COUNTER_INFO(0x3065, new ResponseQuestCounterInfo()),
    RESPONSE_PLAYER_ACTION_WAIT(0x3066, new ResponsePlayerActionWait()),
    RESPONSE_PLAYER_ACTION_REQUEST(0x3067, new ResponsePlayerActionRequest()),
    RESPONSE_PLAYER_ACTION_CANCELED(0x3068, new ResponsePlayerActionCanceled()),
    RESPONSE_PLAYER_ACTION_REJECTED(0x3069, new ResponsePlayerActionRejected()),
    RESPONSE_PLAYER_ACTION_ACCEPTED(0x3070, new ResponsePlayerActionAccepted()),
    RESPONSE_PARTY_MEMBERS(0x3071, new ResponsePartyMembers()),
    RESPONSE_PARTY_MEMBER_STATUS(0x3072, new ResponsePartyMemberStatus()),
    RESPONSE_PARTY_MEMBER_EXCLUDE(0x3073, new ResponsePartyMemberExclude()),
    RESPONSE_PARTY_DISBAND(0x3074, new ResponsePartyDisband()),
    RESPONSE_SYSTEM_MESSAGE(0x3075, new ResponseSystemMessage()),
    RESPONSE_FRACTION_LIST(0x3079, new ResponseFractionList()),
    RESPONSE_DELETE_PILOT(0x3080, new ResponseDeletePilot()),
    RESPONSE_EXIT(0x3082, new ResponseExit()),
    RESPONSE_OBJECT_START_TELEPORT(0x3083, new ResponseObjectStartTeleport()),
    RESPONSE_OBJECT_FINISH_TELEPORT(0x3084, new ResponseObjectFinishTeleport()),
    RESPONSE_SHIP_TELEPORTED_TO_STATION(0x3085, new ResponseShipTeleportedToStation()),
    RESPONSE_STATION_QUEST_LIST(0x3086, new ResponseStationQuestList()),
    RESPONSE_STATION_SHOP_INFO(0x3087, new ResponseStationShopInfo()),
    RESPONSE_STATION_SHIP_REPAIR(0x3088, new ResponseStationShipRepair()),
    RESPONSE_SKILL_STATE(0x3089, new ResponseSkillState()),
    RESPONSE_PLAYER_SHIP_DESTROYED(0x3090, new ResponsePlayerShipDestroyed()),
    RESPONSE_SHIP_FLY_AND_ROTATION_INTERRUPT(0x3091, new ResponseShipFlyAndRotationInterrupt()),
    RESPONSE_SOLAR_SYSTEM_TABLE(0x3092, new ResponseSolarSystemTable()),
    RESPONSE_BATTLE_MODE(0x3093, new ResponseBattleMode()),
    RESPONSE_RATING_SYSTEM(0x3094, new ResponseRatingSystem()),
    RESPONSE_RATING_ELEMENT(0x3095, new ResponseRatingElement()),
    RESPONSE_STATION_WORKSHOP_MODULE_LIST(0x3096, new ResponseStationWorkshopModuleList()),
    RESPONSE_STATION_WORKSHOP_PRICE(0x3097, new ResponseStationWorkshopPrice()),
    RESPONSE_STATION_WORKSHOP_SHOP_STATUS(0x3098, new ResponseStationWorkshopShopStatus()),
    RESPONSE_STATION_WORKSHOP_SHIP_LIST(0x3099, new ResponseStationWorkshopShipList()),
    RESPONSE_ENTER_TO_SPACE(0x3100, new ResponseEnterToSpace()),
    RESPONSE_CHANGE_CREDITS(0x3101, new ResponseChangeCredits()),
    RESPONSE_SHOW_QUEST_ON_STATION(0x3102, new ResponseShowQuestOnStation()),
    RESPONSE_OPEN_WORKSHOP(0x3103, new ResponseOpenWorkshop()),
    RESPONSE_OPEN_STATION_SHOP(0x3104, new ResponseOpenStationShop()),
    RESPONSE_GO_TO_SPACE(0x3105, new ResponseGoToSpace()),
    RESPONSE_SHOW_QUEST_IN_SPACE(0x3106, new ResponseShowQuestInSpace()),
    RESPONSE_SHOW_ASSISTANT(0x3107, new ResponseShowAssistant()),
    RESPONSE_HIDE_ASSISTANT(0x3108, new ResponseHideAssistant()),;

    private static final Logger LOGGER = LoggerManager.getLogger(ServerPacketType.class);

    /**
     * Массив всех типов.
     */
    public static final ServerPacketType[] VALUES = values();

    /**
     * Кол-во всех типов.
     */
    public static final int SIZE = VALUES.length;

    /**
     * Массив пакетов.
     */
    private static ServerPacket[] packets;

    /**
     * Возвращает новый экземпляр пакета в соответствии с опкодом
     *
     * @param opcode опкод пакета.
     * @return экземпляр нужного пакета.
     */
    public static ServerPacket getPacketForOpcode(final int opcode) {
        final ServerPacket packet = packets[opcode];
        return packet == null ? null : packet.newInstance();
    }

    /**
     * Инициализация клиентских пакетов.
     */
    public static void init() {

        final Set<Integer> set = new HashSet<Integer>();

        for (final ServerPacketType packet : values()) {

            final int index = packet.getOpcode();

            if (set.contains(index)) {
                LOGGER.warning("found duplicate opcode " + index + " or " + Integer.toHexString(packet.getOpcode()) + "!");
            }

            set.add(index);
        }

        set.clear();

        packets = new ServerPacket[Short.MAX_VALUE * 2];

        for (final ServerPacketType packet : values()) {
            packets[packet.getOpcode()] = packet.getPacket();
        }

        LOGGER.info("server packets prepared.");
    }

    /**
     * Пул пакетов.
     */
    private final FoldablePool<ServerPacket> pool;

    /**
     * Экземпляр пакета.
     */
    private final ServerPacket packet;

    /**
     * Опкод пакета.
     */
    private final int opcode;

    /**
     * @param opcode опкод пакета.
     * @param packet экземпляр пакета.
     */
    private ServerPacketType(final int opcode, final ServerPacket packet) {
        this.opcode = opcode;
        this.packet = packet;
        this.packet.setPacketType(this);
        this.pool = PoolFactory.newAtomicFoldablePool(ServerPacket.class);
    }

    /**
     * @return опкод пакета.
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * @return экземпляр пакета.
     */
    public ServerPacket getPacket() {
        return packet;
    }

    /**
     * @return пул пакетов.
     */
    public final FoldablePool<ServerPacket> getPool() {
        return pool;
    }
}