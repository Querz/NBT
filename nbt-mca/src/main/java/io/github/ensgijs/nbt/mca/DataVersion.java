package io.github.ensgijs.nbt.mca;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

// source: version.json file, found in the root directory of the client and server jars
// table of versions can also be found on https://minecraft.fandom.com/wiki/Data_version#List_of_data_versions
// google sheet to help generate enum values https://docs.google.com/spreadsheets/d/1VVGUPe9sfsd3rsFYcBGDnt1bTifBh3dUkKV9vQvNWQY
//   - paste rows from the fandom table into the sheet and sort ascending by data version (if you don't sort it the mc version WILL BE WRONG!)
//
// As the wiki has been lacking in freshness lately the test DataVersionTest#testFetchMissingDataVersionInformation
// will help keep this enum updated for all official builds - which may exclude experimental builds, but it sure
// beats having to farm the data by hand.

/**
 * List of MC versions and MCA data versions back to 1.9.0
 * <p>
 *     TODO: weekly builds don't really fit with having a version but it's annoying to not have a version too - what to do?
 * </p>
 */
public enum DataVersion {
    // TODO: document change history by digging through net.minecraft.util.datafix.DataConverterRegistry
    // Kept in ASC order (unit test enforced)
    UNKNOWN(0, 0, 0, 0),

    JAVA_1_9_15W32A(100, 1, 9, 0, "15w32a"),
    JAVA_1_9_15W32B(103, 1, 9, 0, "15w32b"),
    JAVA_1_9_15W32C(104, 1, 9, 0, "15w32c"),
    JAVA_1_9_15W33B(111, 1, 9, 0, "15w33b"),  // A and B have the same data version
    JAVA_1_9_15W33C(112, 1, 9, 0, "15w33c"),
    JAVA_1_9_15W34A(114, 1, 9, 0, "15w34a"),
    JAVA_1_9_15W34B(115, 1, 9, 0, "15w34b"),
    JAVA_1_9_15W34C(116, 1, 9, 0, "15w34c"),
    JAVA_1_9_15W34D(117, 1, 9, 0, "15w34d"),
    JAVA_1_9_15W35A(118, 1, 9, 0, "15w35a"),
    JAVA_1_9_15W35B(119, 1, 9, 0, "15w35b"),
    JAVA_1_9_15W35C(120, 1, 9, 0, "15w35c"),
    JAVA_1_9_15W35D(121, 1, 9, 0, "15w35d"),
    JAVA_1_9_15W35E(122, 1, 9, 0, "15w35e"),
    JAVA_1_9_15W36A(123, 1, 9, 0, "15w36a"),
    JAVA_1_9_15W36B(124, 1, 9, 0, "15w36b"),
    JAVA_1_9_15W36C(125, 1, 9, 0, "15w36c"),
    JAVA_1_9_15W36D(126, 1, 9, 0, "15w36d"),
    JAVA_1_9_15W37A(127, 1, 9, 0, "15w37a"),
    JAVA_1_9_15W38A(128, 1, 9, 0, "15w38a"),
    JAVA_1_9_15W38B(129, 1, 9, 0, "15w38b"),
    JAVA_1_9_15W39A(130, 1, 9, 0, "15w39a"),
    JAVA_1_9_15W39B(131, 1, 9, 0, "15w39b"),
    JAVA_1_9_15W39C(132, 1, 9, 0, "15w39c"),
    JAVA_1_9_15W40A(133, 1, 9, 0, "15w40a"),
    JAVA_1_9_15W40B(134, 1, 9, 0, "15w40b"),
    JAVA_1_9_15W41A(136, 1, 9, 0, "15w41a"),
    JAVA_1_9_15W41B(137, 1, 9, 0, "15w41b"),
    JAVA_1_9_15W42A(138, 1, 9, 0, "15w42a"),
    JAVA_1_9_15W43A(139, 1, 9, 0, "15w43a"),
    JAVA_1_9_15W43B(140, 1, 9, 0, "15w43b"),
    JAVA_1_9_15W43C(141, 1, 9, 0, "15w43c"),
    JAVA_1_9_15W44A(142, 1, 9, 0, "15w44a"),
    JAVA_1_9_15W44B(143, 1, 9, 0, "15w44b"),
    JAVA_1_9_15W45A(145, 1, 9, 0, "15w45a"),
    JAVA_1_9_15W46A(146, 1, 9, 0, "15w46a"),
    JAVA_1_9_15W47A(148, 1, 9, 0, "15w47a"),
    JAVA_1_9_15W47B(149, 1, 9, 0, "15w47b"),
    JAVA_1_9_15W47C(150, 1, 9, 0, "15w47c"),
    JAVA_1_9_15W49A(151, 1, 9, 0, "15w49a"),
    JAVA_1_9_15W49B(152, 1, 9, 0, "15w49b"),
    JAVA_1_9_15W50A(153, 1, 9, 0, "15w50a"),
    JAVA_1_9_15W51A(154, 1, 9, 0, "15w51a"),
    JAVA_1_9_15W51B(155, 1, 9, 0, "15w51b"),
    JAVA_1_9_16W02A(156, 1, 9, 0, "16w02a"),
    JAVA_1_9_16W03A(157, 1, 9, 0, "16w03a"),
    JAVA_1_9_16W04A(158, 1, 9, 0, "16w04a"),
    JAVA_1_9_16W05A(159, 1, 9, 0, "16w05a"),
    JAVA_1_9_16W05B(160, 1, 9, 0, "16w05b"),
    JAVA_1_9_16W06A(161, 1, 9, 0, "16w06a"),
    JAVA_1_9_16W07A(162, 1, 9, 0, "16w07a"),
    JAVA_1_9_16W07B(163, 1, 9, 0, "16w07b"),
    JAVA_1_9_PRE1(164, 1, 9, 0, "PRE1"),
    JAVA_1_9_PRE2(165, 1, 9, 0, "PRE2"),
    JAVA_1_9_PRE3(167, 1, 9, 0, "PRE3"),
    JAVA_1_9_PRE4(168, 1, 9, 0, "PRE4"),
    JAVA_1_9_0(169, 1, 9, 0),
    JAVA_1_9_1_PRE1(170, 1, 9, 1, "PRE1"),
    JAVA_1_9_1_PRE2(171, 1, 9, 1, "PRE2"),
    JAVA_1_9_1_PRE3(172, 1, 9, 1, "PRE3"),
    JAVA_1_9_1(175, 1, 9, 1),
    JAVA_1_9_2(176, 1, 9, 2),
    JAVA_1_9_3_16W14A(177, 1, 9, 3, "16w14a"),
    JAVA_1_9_3_16W15A(178, 1, 9, 3, "16w15a"),
    JAVA_1_9_3_16W15B(179, 1, 9, 3, "16w15b"),
    JAVA_1_9_3_PRE1(180, 1, 9, 3, "PRE1"),
    JAVA_1_9_3_PRE2(181, 1, 9, 3, "PRE2"),
    JAVA_1_9_3_PRE3(182, 1, 9, 3, "PRE3"),
    JAVA_1_9_3(183, 1, 9, 3),
    JAVA_1_9_4(184, 1, 9, 4),
    JAVA_1_10_16W20A(501, 1, 10, 0, "16w20a"),
    JAVA_1_10_16W21A(503, 1, 10, 0, "16w21a"),
    JAVA_1_10_16W21B(504, 1, 10, 0, "16w21b"),
    JAVA_1_10_PRE1(506, 1, 10, 0, "PRE1"),
    JAVA_1_10_PRE2(507, 1, 10, 0, "PRE2"),
    JAVA_1_10_0(510, 1, 10, 0),
    JAVA_1_10_1(511, 1, 10, 1),
    JAVA_1_10_2(512, 1, 10, 2),
    JAVA_1_11_16W32A(800, 1, 11, 0, "16w32a"),
    JAVA_1_11_16W32B(801, 1, 11, 0, "16w32b"),
    JAVA_1_11_16W33A(802, 1, 11, 0, "16w33a"),
    JAVA_1_11_16W35A(803, 1, 11, 0, "16w35a"),
    JAVA_1_11_16W36A(805, 1, 11, 0, "16w36a"),
    JAVA_1_11_16W38A(807, 1, 11, 0, "16w38a"),
    JAVA_1_11_16W39A(809, 1, 11, 0, "16w39a"),
    JAVA_1_11_16W39B(811, 1, 11, 0, "16w39b"),
    JAVA_1_11_16W39C(812, 1, 11, 0, "16w39c"),
    JAVA_1_11_16W40A(813, 1, 11, 0, "16w40a"),
    JAVA_1_11_16W41A(814, 1, 11, 0, "16w41a"),
    JAVA_1_11_16W42A(815, 1, 11, 0, "16w42a"),
    JAVA_1_11_16W43A(816, 1, 11, 0, "16w43a"),
    JAVA_1_11_16W44A(817, 1, 11, 0, "16w44a"),
    JAVA_1_11_PRE1(818, 1, 11, 0, "PRE1"),
    JAVA_1_11_0(819, 1, 11, 0),
    JAVA_1_11_1_16W50A(920, 1, 11, 1, "16w50a"),
    JAVA_1_11_1(921, 1, 11, 1),
    JAVA_1_11_2(922, 1, 11, 2),
    JAVA_1_12_17W06A(1022, 1, 12, 0, "17w06a"),
    JAVA_1_12_17W13A(1122, 1, 12, 0, "17w13a"),
    JAVA_1_12_17W13B(1123, 1, 12, 0, "17w13b"),
    JAVA_1_12_17W14A(1124, 1, 12, 0, "17w14a"),
    JAVA_1_12_17W15A(1125, 1, 12, 0, "17w15a"),
    JAVA_1_12_17W16A(1126, 1, 12, 0, "17w16a"),
    JAVA_1_12_17W16B(1127, 1, 12, 0, "17w16b"),
    JAVA_1_12_17W17A(1128, 1, 12, 0, "17w17a"),
    JAVA_1_12_17W17B(1129, 1, 12, 0, "17w17b"),
    JAVA_1_12_17W18A(1130, 1, 12, 0, "17w18a"),
    JAVA_1_12_17W18B(1131, 1, 12, 0, "17w18b"),
    JAVA_1_12_PRE1(1132, 1, 12, 0, "PRE1"),
    JAVA_1_12_PRE2(1133, 1, 12, 0, "PRE2"),
    JAVA_1_12_PRE3(1134, 1, 12, 0, "PRE3"),
    JAVA_1_12_PRE4(1135, 1, 12, 0, "PRE4"),
    JAVA_1_12_PRE5(1136, 1, 12, 0, "PRE5"),
    JAVA_1_12_PRE6(1137, 1, 12, 0, "PRE6"),
    JAVA_1_12_PRE7(1138, 1, 12, 0, "PRE7"),
    JAVA_1_12_0(1139, 1, 12, 0),
    JAVA_1_12_1_17W31A(1239, 1, 12, 1, "17w31a"),
    JAVA_1_12_1_PRE1(1240, 1, 12, 1, "PRE1"),
    JAVA_1_12_1(1241, 1, 12, 1),
    JAVA_1_12_2_PRE1(1341, 1, 12, 2, "PRE1"),
    JAVA_1_12_2_PRE2(1342, 1, 12, 2, "PRE2"),
    JAVA_1_12_2(1343, 1, 12, 2),
    JAVA_1_13_17W43A(1444, 1, 13, 0, "17w43a"),
    JAVA_1_13_17W43B(1445, 1, 13, 0, "17w43b"),
    JAVA_1_13_17W45A(1447, 1, 13, 0, "17w45a"),
    JAVA_1_13_17W45B(1448, 1, 13, 0, "17w45b"),
    JAVA_1_13_17W46A(1449, 1, 13, 0, "17w46a"),
    /** "Blocks" and "Data" were replaced with block palette */
    JAVA_1_13_17W47A(1451, 1, 13, 0, "17w47a"),
    JAVA_1_13_17W47B(1452, 1, 13, 0, "17w47b"),
    JAVA_1_13_17W48A(1453, 1, 13, 0, "17w48a"),
    JAVA_1_13_17W49A(1454, 1, 13, 0, "17w49a"),
    JAVA_1_13_17W49B(1455, 1, 13, 0, "17w49b"),
    JAVA_1_13_17W50A(1457, 1, 13, 0, "17w50a"),
    JAVA_1_13_18W01A(1459, 1, 13, 0, "18w01a"),
    JAVA_1_13_18W02A(1461, 1, 13, 0, "18w02a"),
    JAVA_1_13_18W03A(1462, 1, 13, 0, "18w03a"),
    JAVA_1_13_18W03B(1463, 1, 13, 0, "18w03b"),
    JAVA_1_13_18W05A(1464, 1, 13, 0, "18w05a"),
    /**
     * Biome data now stored in IntArrayTag instead of ByteArrayTag (still 2D using only 256 entries).
     * <p>Tags Removed</p>
     * <ul>
     *   <li>region: Level.Biomes  &lt;ByteArrayTag&gt; (type changed)</li>
     *   <li>region: Level.HeightMap  &lt;IntArrayTag&gt;</li>
     *   <li>region: Level.LightPopulated  &lt;ByteTag&gt;</li>
     *   <li>region: Level.TerrainPopulated  &lt;ByteTag&gt; (replaced by Status string)</li>
     * </ul>
     * <p>Tags Added</p>
     * <ul>
     *   <li>region: Level.Biomes  &lt;IntArrayTag&gt;</li>
     *   <li>region: Level.Heightmaps  &lt;CompoundTag&gt;</li>
     *   <li>region: Level.Heightmaps.LIGHT  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Heightmaps.LIQUID  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Heightmaps.RAIN  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Heightmaps.SOLID  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Lights  &lt;ListTag&lt;ListTag&lt;ShortTag&gt;&gt;&gt;</li>
     *   <li>region: Level.PostProcessing  &lt;ListTag&lt;ListTag&lt;ShortTag&gt;&gt;&gt;</li>
     *   <li>region: Level.Status  &lt;StringTag&gt;</li>
     *   <li>region: Level.Structures  &lt;CompoundTag&gt;</li>
     *   <li>region: Level.Structures.References  &lt;CompoundTag&gt;
     *      <br>Keys are the name of a structure type such as "Desert_Pyramid".
     *      <br>Values are &lt;LongArrayTag&gt; which are packed chunk coordinates where Z is packed in the high 32 bits and X is in the low 32 bits.</li>
     *   <li>region: Level.Structures.Starts  &lt;CompoundTag&gt;
     *      <br>Keys are the name of a structure type such as "Desert_Pyramid".
     *      <br>Values are &lt;CompoundTag&gt; defining structure bounds and generation information.</li>
     *   <li>region: Level.ToBeTicked  &lt;ListTag&lt;ListTag&lt;ShortTag&gt;&gt;&gt;</li>
     * </ul>
     */
    JAVA_1_13_18W06A(1466, 1, 13, 0, "18w06a"),
    JAVA_1_13_18W07A(1467, 1, 13, 0, "18w07a"),
    JAVA_1_13_18W07B(1468, 1, 13, 0, "18w07b"),
    JAVA_1_13_18W07C(1469, 1, 13, 0, "18w07c"),
    JAVA_1_13_18W08A(1470, 1, 13, 0, "18w08a"),
    JAVA_1_13_18W08B(1471, 1, 13, 0, "18w08b"),
    JAVA_1_13_18W09A(1472, 1, 13, 0, "18w09a"),
    JAVA_1_13_18W10A(1473, 1, 13, 0, "18w10a"),
    JAVA_1_13_18W10B(1474, 1, 13, 0, "18w10b"),
    JAVA_1_13_18W10C(1476, 1, 13, 0, "18w10c"),
    JAVA_1_13_18W10D(1477, 1, 13, 0, "18w10d"),
    JAVA_1_13_18W11A(1478, 1, 13, 0, "18w11a"),
    JAVA_1_13_18W14A(1479, 1, 13, 0, "18w14a"),
    JAVA_1_13_18W14B(1481, 1, 13, 0, "18w14b"),
    JAVA_1_13_18W15A(1482, 1, 13, 0, "18w15a"),
    /**
     * <p>Tags Added</p>
     * <ul>
     *   <li>region: Level.LiquidTicks  &lt;ListTag&lt;CompoundTag&gt;&gt;</li>
     *   <li>region: Level.LiquidTicks[].i  &lt;StringTag&gt;</li>
     *   <li>region: Level.LiquidTicks[].p  &lt;IntTag&gt;</li>
     *   <li>region: Level.LiquidTicks[].t  &lt;IntTag&gt;</li>
     *   <li>region: Level.LiquidTicks[].x  &lt;IntTag&gt;</li>
     *   <li>region: Level.LiquidTicks[].y  &lt;IntTag&gt;</li>
     *   <li>region: Level.LiquidTicks[].z  &lt;IntTag&gt;</li>
     *   <li>region: Level.LiquidsToBeTicked  &lt;ListTag&lt;ListTag&lt;ShortTag&gt;&gt;&gt;</li>
     * </ul>
     */
    JAVA_1_13_18W16A(1483, 1, 13, 0, "18w16a"),

    /**
     *
     * <p>Tags Removed</p>
     * <ul>
     *   <li>region: Level.Heightmaps.LIGHT  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Heightmaps.LIQUID  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Heightmaps.RAIN  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Heightmaps.SOLID  &lt;LongArrayTag&gt;</li>
     * </ul>
     * <p>Tags Added</p>
     * <ul>
     *   <li>region: Level.CarvingMasks  &lt;CompoundTag&gt;</li>
     *   <li>region: Level.CarvingMasks.AIR  &lt;ByteArrayTag&gt;</li>
     *   <li>region: Level.CarvingMasks.LIQUID  &lt;ByteArrayTag&gt;</li>
     *   <li>region: Level.Heightmaps.LIGHT_BLOCKING  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Heightmaps.MOTION_BLOCKING  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Heightmaps.MOTION_BLOCKING_NO_LEAVES  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Heightmaps.OCEAN_FLOOR  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Heightmaps.OCEAN_FLOOR_WG  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Heightmaps.WORLD_SURFACE_WG  &lt;LongArrayTag&gt;</li>
     * </ul>
     */
    JAVA_1_13_18W19A(1484, 1, 13, 0, "18w19a"),
    JAVA_1_13_18W19B(1485, 1, 13, 0, "18w19b"),
    JAVA_1_13_18W20A(1489, 1, 13, 0, "18w20a"),
    JAVA_1_13_18W20B(1491, 1, 13, 0, "18w20b"),
    /** Believe this to be the end of the Level.hasLegacyStructureData tag */
    JAVA_1_13_18W20C(1493, 1, 13, 0, "18w20c"),
    JAVA_1_13_18W21A(1495, 1, 13, 0, "18w21a"),
    JAVA_1_13_18W21B(1496, 1, 13, 0, "18w21b"),
    JAVA_1_13_18W22A(1497, 1, 13, 0, "18w22a"),
    JAVA_1_13_18W22B(1498, 1, 13, 0, "18w22b"),
    JAVA_1_13_18W22C(1499, 1, 13, 0, "18w22c"),
    JAVA_1_13_PRE1(1501, 1, 13, 0, "PRE1"),
    JAVA_1_13_PRE2(1502, 1, 13, 0, "PRE2"),
    /**
     * <p>Tags Added</p>
     * <ul>
     *   <li>region: Level.Heightmaps.WORLD_SURFACE  &lt;LongArrayTag&gt;</li>
     * </ul>
     */
    JAVA_1_13_PRE3(1503, 1, 13, 0, "PRE3"),
    JAVA_1_13_PRE4(1504, 1, 13, 0, "PRE4"),
    // 1506 -- legacy biome id mapping changed
    JAVA_1_13_PRE5(1511, 1, 13, 0, "PRE5"),
    JAVA_1_13_PRE6(1512, 1, 13, 0, "PRE6"),
    JAVA_1_13_PRE7(1513, 1, 13, 0, "PRE7"),
    JAVA_1_13_PRE8(1516, 1, 13, 0, "PRE8"),
    JAVA_1_13_PRE9(1517, 1, 13, 0, "PRE9"),
    JAVA_1_13_PRE10(1518, 1, 13, 0, "PRE10"),
    JAVA_1_13_0(1519, 1, 13, 0),
    JAVA_1_13_1_18W30A(1620, 1, 13, 1, "18w30a"),
    JAVA_1_13_1_18W30B(1621, 1, 13, 1, "18w30b"),
    JAVA_1_13_1_18W31A(1622, 1, 13, 1, "18w31a"),
    JAVA_1_13_1_18W32A(1623, 1, 13, 1, "18w32a"),
    JAVA_1_13_1_18W33A(1625, 1, 13, 1, "18w33a"),
    JAVA_1_13_1_PRE1(1626, 1, 13, 1, "PRE1"),
    JAVA_1_13_1_PRE2(1627, 1, 13, 1, "PRE2"),
    JAVA_1_13_1(1628, 1, 13, 1),
    JAVA_1_13_2_PRE1(1629, 1, 13, 2, "PRE1"),
    JAVA_1_13_2_PRE2(1630, 1, 13, 2, "PRE2"),
    JAVA_1_13_2(1631, 1, 13, 2),
    JAVA_1_14_18W43A(1901, 1, 14, 0, "18w43a"),
    JAVA_1_14_18W43B(1902, 1, 14, 0, "18w43b"),
    JAVA_1_14_18W43C(1903, 1, 14, 0, "18w43c"),
    /**
     * <p>Tags Added</p>
     * <ul>
     *   <li>region: Level.NoiseMask  &lt;ByteArrayTag&gt;</li>
     * </ul>
     */
    JAVA_1_14_18W44A(1907, 1, 14, 0, "18w44a"),
    JAVA_1_14_18W45A(1908, 1, 14, 0, "18w45a"),
    /**
     * <p>Tags Removed</p>
     * <ul>
     *   <li>region: Level.NoiseMask  &lt;ByteArrayTag&gt;</li>
     * </ul>
     */
    JAVA_1_14_18W46A(1910, 1, 14, 0, "18w46a"),
    JAVA_1_14_18W47A(1912, 1, 14, 0, "18w47a"),
    JAVA_1_14_18W47B(1913, 1, 14, 0, "18w47b"),
    JAVA_1_14_18W48A(1914, 1, 14, 0, "18w48a"),
    JAVA_1_14_18W48B(1915, 1, 14, 0, "18w48b"),
    JAVA_1_14_18W49A(1916, 1, 14, 0, "18w49a"),

    /**
     * FIRST SEEN (may have been added before this version). Villagers gain professions?
     * <ul>
     *   <li>region: Level.Entities[].VillagerData.level  &lt;IntTag&gt;</li>
     *   <li>region: Level.Entities[].VillagerData.profession  &lt;StringTag&gt;</li>
     *   <li>region: Level.Entities[].VillagerData.type  &lt;StringTag&gt;</li>
     * </ul>
     */
    JAVA_1_14_18W50A(1919, 1, 14, 0, "18w50a"),
    /**
     * <p>Tags Added</p>
     * <ul>
     *   <li>region: Level.isLightOn  &lt;ByteTag&gt;</li>
     * </ul>
     */
    JAVA_1_14_19W02A(1921, 1, 14, 0, "19w02a"),
    JAVA_1_14_19W03A(1922, 1, 14, 0, "19w03a"),
    JAVA_1_14_19W03B(1923, 1, 14, 0, "19w03b"),
    JAVA_1_14_19W03C(1924, 1, 14, 0, "19w03c"),
    JAVA_1_14_19W04A(1926, 1, 14, 0, "19w04a"),
    JAVA_1_14_19W04B(1927, 1, 14, 0, "19w04b"),
    JAVA_1_14_19W05A(1930, 1, 14, 0, "19w05a"),
    JAVA_1_14_19W06A(1931, 1, 14, 0, "19w06a"),
    JAVA_1_14_19W07A(1932, 1, 14, 0, "19w07a"),
    JAVA_1_14_19W08A(1933, 1, 14, 0, "19w08a"),
    JAVA_1_14_19W08B(1934, 1, 14, 0, "19w08b"),
    JAVA_1_14_19W09A(1935, 1, 14, 0, "19w09a"),
    /**
     * /poi/r.X.Z.mca files introduced with a premature nbt structure. POI files not supported by this library until
     * {@link #JAVA_1_14_PRE1}. Note this poi format did not include a DataVersion.
     * <p>Temporary POI Structure</p>
     * <ul>
     *   <li>poi: #  &lt;ListTag&lt;CompoundTag&gt;&gt; - the keys &lt;#&gt; are a number literal indicating the chunk section Y</li>
     *   <li>poi: #[].free_tickets  &lt;IntTag&gt;</li>
     *   <li>poi: #[].pos  &lt;IntArrayTag&gt;</li>
     *   <li>poi: #[].type  &lt;StringTag&gt;</li>
     * </ul>
     * <p>Villagers got brains ({@code Entities[].Brain}) in the region file data.</p>
     */
    JAVA_1_14_19W11A(1937, 1, 14, 0, "19w11a"),
    JAVA_1_14_19W11B(1938, 1, 14, 0, "19w11b"),
    JAVA_1_14_19W12A(1940, 1, 14, 0, "19w12a"),
    JAVA_1_14_19W12B(1941, 1, 14, 0, "19w12b"),
    JAVA_1_14_19W13A(1942, 1, 14, 0, "19w13a"),
    JAVA_1_14_19W13B(1943, 1, 14, 0, "19w13b"),
    JAVA_1_14_19W14A(1944, 1, 14, 0, "19w14a"),
    JAVA_1_14_19W14B(1945, 1, 14, 0, "19w14b"),
    /**
     * POI tag structure changed. Begin this library's support of POI files.
     * <p>Final POI Structure</p>
     * <ul>
     *   <li>poi: DataVersion  &lt;IntTag&gt;</li>
     *   <li>poi: Sections  &lt;CompoundTag&gt;</li>
     *   <li>poi: Sections.#  &lt;CompoundTag&gt; - the keys &lt;#&gt; are a number literal indicating the chunk section Y</li>
     *   <li>poi: Sections.#.Records  &lt;ListTag&lt;CompoundTag&gt;&gt;</li>
     *   <li>poi: Sections.#.Records[].free_tickets  &lt;IntTag&gt;</li>
     *   <li>poi: Sections.#.Records[].pos  &lt;IntArrayTag&gt;</li>
     *   <li>poi: Sections.#.Records[].type  &lt;StringTag&gt;</li>
     *   <li>poi: Sections.#.Valid  &lt;ByteTag&gt; (boolean)</li>
     * </ul>
     */
    JAVA_1_14_PRE1(1947, 1, 14, 0, "PRE1"),
    JAVA_1_14_PRE2(1948, 1, 14, 0, "PRE2"),
    /**
     * <p>Tags Removed</p>
     * <ul>
     *   <li>region: Level.CarvingMasks.AIR  &lt;ByteArrayTag&gt;</li>
     *   <li>region: Level.CarvingMasks.LIQUID  &lt;ByteArrayTag&gt;</li>
     *   <li>region: Level.LiquidsToBeTicked  &lt;ListTag&lt;ListTag&lt;ShortTag&gt;&gt;&gt; - NOTE: JAVA_1_18_21W43A change notes make reference to this tag so IDK</li>
     *   <li>region: Level.ToBeTicked  &lt;ListTag&lt;ListTag&lt;ShortTag&gt;&gt;&gt; - NOTE: JAVA_1_18_21W43A change notes make reference to this tag so IDK</li>
     * </ul>
     */
    JAVA_1_14_PRE3(1949, 1, 14, 0, "PRE3"),
    JAVA_1_14_PRE4(1950, 1, 14, 0, "PRE4"),
    JAVA_1_14_PRE5(1951, 1, 14, 0, "PRE5"),
    JAVA_1_14_0(1952, 1, 14, 0),
    JAVA_1_14_1_PRE1(1955, 1, 14, 1, "PRE1"),
    JAVA_1_14_1_PRE2(1956, 1, 14, 1, "PRE2"),
    JAVA_1_14_1(1957, 1, 14, 1),
    JAVA_1_14_2_PRE1(1958, 1, 14, 2, "PRE1"),
    JAVA_1_14_2_PRE2(1959, 1, 14, 2, "PRE2"),
    JAVA_1_14_2_PRE3(1960, 1, 14, 2, "PRE3"),
    JAVA_1_14_2_PRE4(1962, 1, 14, 2, "PRE4"),
    JAVA_1_14_2(1963, 1, 14, 2),
    JAVA_1_14_3_PRE1(1964, 1, 14, 3, "PRE1"),
    JAVA_1_14_3_PRE2(1965, 1, 14, 3, "PRE2"),
    JAVA_1_14_3_PRE3(1966, 1, 14, 3, "PRE3"),
    JAVA_1_14_3_PRE4(1967, 1, 14, 3, "PRE4"),
    JAVA_1_14_3(1968, 1, 14, 3),
    JAVA_1_14_4_PRE1(1969, 1, 14, 4, "PRE1"),
    JAVA_1_14_4_PRE2(1970, 1, 14, 4, "PRE2"),
    JAVA_1_14_4_PRE3(1971, 1, 14, 4, "PRE3"),
    JAVA_1_14_4_PRE4(1972, 1, 14, 4, "PRE4"),
    JAVA_1_14_4_PRE5(1973, 1, 14, 4, "PRE5"),
    JAVA_1_14_4_PRE6(1974, 1, 14, 4, "PRE6"),
    JAVA_1_14_4_PRE7(1975, 1, 14, 4, "PRE7"),
    /** First version where Mojang published jar deobfuscation mappings. */
    JAVA_1_14_4(1976, 1, 14, 4),
//    JAVA_1_14_3_CT1(2067, 1, 14, 3, "CT1"),
//    JAVA_1_15_CT2(2068, 1, 15, 0, "CT2"),
//    JAVA_1_15_CT3(2069, 1, 15, 0, "CT3"),
    /** Bees introduced. */
    JAVA_1_15_19W34A(2200, 1, 15, 0, "19w34a"),
    JAVA_1_15_19W35A(2201, 1, 15, 0, "19w35a"),
    /**
     * 3D Biomes added. Biomes array in the  Level tag for each chunk changed
     * to contain 1024 integers instead of 256 see {@link TerrainChunk}
     */
    JAVA_1_15_19W36A(2203, 1, 15, 0, "19w36a"),
    JAVA_1_15_19W37A(2204, 1, 15, 0, "19w37a"),
    JAVA_1_15_19W38A(2205, 1, 15, 0, "19w38a"),
    JAVA_1_15_19W38B(2206, 1, 15, 0, "19w38b"),
    JAVA_1_15_19W39A(2207, 1, 15, 0, "19w39a"),
    JAVA_1_15_19W40A(2208, 1, 15, 0, "19w40a"),
    JAVA_1_15_19W41A(2210, 1, 15, 0, "19w41a"),
    JAVA_1_15_19W42A(2212, 1, 15, 0, "19w42a"),
    JAVA_1_15_19W44A(2213, 1, 15, 0, "19w44a"),
    JAVA_1_15_19W45A(2214, 1, 15, 0, "19w45a"),
    JAVA_1_15_19W45B(2215, 1, 15, 0, "19w45b"),
    JAVA_1_15_19W46A(2216, 1, 15, 0, "19w46a"),
    JAVA_1_15_19W46B(2217, 1, 15, 0, "19w46b"),
    JAVA_1_15_PRE1(2218, 1, 15, 0, "PRE1"),
    JAVA_1_15_PRE2(2219, 1, 15, 0, "PRE2"),
    JAVA_1_15_PRE3(2220, 1, 15, 0, "PRE3"),
    JAVA_1_15_PRE4(2221, 1, 15, 0, "PRE4"),
    JAVA_1_15_PRE5(2222, 1, 15, 0, "PRE5"),
    JAVA_1_15_PRE6(2223, 1, 15, 0, "PRE6"),
    JAVA_1_15_PRE7(2224, 1, 15, 0, "PRE7"),
    JAVA_1_15_0(2225, 1, 15, 0),
    JAVA_1_15_1_PRE1(2226, 1, 15, 1, "PRE1"),
    JAVA_1_15_1(2227, 1, 15, 1),
    JAVA_1_15_2_PRE1(2228, 1, 15, 2, "PRE1"),
    JAVA_1_15_2_PRE2(2229, 1, 15, 2, "PRE2"),
    JAVA_1_15_2(2230, 1, 15, 2),
//    JAVA_1_16_CT4(2320, 1, 16, 0, "CT4"),
//    JAVA_1_16_CT5(2321, 1, 16, 0, "CT5"),
    JAVA_1_16_20W06A(2504, 1, 16, 0, "20w06a"),
    JAVA_1_16_20W07A(2506, 1, 16, 0, "20w07a"),
    JAVA_1_16_20W08A(2507, 1, 16, 0, "20w08a"),
    JAVA_1_16_20W09A(2510, 1, 16, 0, "20w09a"),
    JAVA_1_16_20W10A(2512, 1, 16, 0, "20w10a"),
    JAVA_1_16_20W11A(2513, 1, 16, 0, "20w11a"),
    /**
     * Entity UUID data storage changed.
     *
     * <p>Tags Removed</p>
     * <ul>
     *   <li>region: Level.Entities[].Attributes[].Modifiers[].UUIDLeast  &lt;LongTag&gt;</li>
     *   <li>region: Level.Entities[].Attributes[].Modifiers[].UUIDMost  &lt;LongTag&gt;</li>
     *   <li>region: Level.Entities[].UUIDLeast  &lt;LongTag&gt;</li>
     *   <li>region: Level.Entities[].UUIDMost  &lt;LongTag&gt;</li>
     * </ul>
     * <p>Tags Added</p>
     * <ul>
     *   <li>region: Level.Entities[].Attributes[].Modifiers[].UUID  &lt;IntArrayTag[4]&gt;</li>
     *   <li>region: Level.Entities[].UUID  &lt;IntArrayTag&gt;</li>
     * </ul>
     */
    JAVA_1_16_20W12A(2515, 1, 16, 0, "20w12a"),
    JAVA_1_16_20W13A(2520, 1, 16, 0, "20w13a"),
    JAVA_1_16_20W13B(2521, 1, 16, 0, "20w13b"),
    JAVA_1_16_20W14A(2524, 1, 16, 0, "20w14a"),
    JAVA_1_16_20W15A(2525, 1, 16, 0, "20w15a"),
    JAVA_1_16_20W16A(2526, 1, 16, 0, "20w16a"),
    /** Block palette packing changed in this version - see {@link TerrainSection} */
    JAVA_1_16_20W17A(2529, 1, 16, 0, "20w17a"),
    JAVA_1_16_20W18A(2532, 1, 16, 0, "20w18a"),
    JAVA_1_16_20W19A(2534, 1, 16, 0, "20w19a"),
    /** The server.jar build of this version was DOA with a null pointer exception on initialization. */
    JAVA_1_16_20W20A(2536, 1, 16, 0, "20w20a"),
    JAVA_1_16_20W20B(2537, 1, 16, 0, "20w20b"),
    /**
     * Structure name format changed from Caps_Snake_Case to lower_snake_case.
     * <p>Example: Level.Structures.References.Desert_Pyramid became Level.Structures.References.desert_pyramid</p>
     * <p>Example: Level.Structures.Starts.Desert_Pyramid became Level.Structures.Starts.desert_pyramid</p>
     *
     */
    JAVA_1_16_20W21A(2554, 1, 16, 0, "20w21a"),
    JAVA_1_16_20W22A(2555, 1, 16, 0, "20w22a"),
    /**
     * <p>Tags Removed</p>
     * <ul>
     *   <li>region: Level.Entities[].Angry  &lt;ByteTag&gt;</li>
     *   <li>region: Level.TileEntities[].Bees[].EntityData.Anger  &lt;IntTag&gt;</li>
     * </ul>
     * <p>Tags Added</p>
     * <ul>
     *   <li>region: Level.Entities[].AngerTime  &lt;IntTag&gt;</li>
     *   <li>region: Level.TileEntities[].Bees[].EntityData.AngerTime  &lt;IntTag&gt;</li>
     * </ul>
     */
    JAVA_1_16_PRE1(2556, 1, 16, 0, "PRE1"),
    JAVA_1_16_PRE2(2557, 1, 16, 0, "PRE2"),
    JAVA_1_16_PRE3(2559, 1, 16, 0, "PRE3"),
    JAVA_1_16_PRE4(2560, 1, 16, 0, "PRE4"),
    /**
     * FIRST SEEN (may have been added prior to this version)
     * <ul>
     *   <li>region: Level.Entities[].AngryAt  &lt;IntArrayTag&gt;</li>
     * </ul>
     */
    JAVA_1_16_PRE5(2561, 1, 16, 0, "PRE5"),
    JAVA_1_16_PRE6(2562, 1, 16, 0, "PRE6"),
    JAVA_1_16_PRE7(2563, 1, 16, 0, "PRE7"),
    JAVA_1_16_PRE8(2564, 1, 16, 0, "PRE8"),
    JAVA_1_16_RC1(2565, 1, 16, 0, "RC1"),
    JAVA_1_16_0(2566, 1, 16, 0),
    JAVA_1_16_1(2567, 1, 16, 1),
    JAVA_1_16_2_20W27A(2569, 1, 16, 2, "20w27a"),
    JAVA_1_16_2_20W28A(2570, 1, 16, 2, "20w28a"),
    JAVA_1_16_2_20W29A(2571, 1, 16, 2, "20w29a"),
    JAVA_1_16_2_20W30A(2572, 1, 16, 2, "20w30a"),
    JAVA_1_16_2_PRE1(2573, 1, 16, 2, "PRE1"),
    JAVA_1_16_2_PRE2(2574, 1, 16, 2, "PRE2"),
    JAVA_1_16_2_PRE3(2575, 1, 16, 2, "PRE3"),
    JAVA_1_16_2_RC1(2576, 1, 16, 2, "RC1"),
    JAVA_1_16_2_RC2(2577, 1, 16, 2, "RC2"),
    JAVA_1_16_2(2578, 1, 16, 2),
    JAVA_1_16_3_RC1(2579, 1, 16, 3, "RC1"),
    JAVA_1_16_3(2580, 1, 16, 3),
    JAVA_1_16_4_PRE1(2581, 1, 16, 4, "PRE1"),
    JAVA_1_16_4_PRE2(2582, 1, 16, 4, "PRE2"),
    JAVA_1_16_4_RC1(2583, 1, 16, 4, "RC1"),
    JAVA_1_16_4(2584, 1, 16, 4),
    JAVA_1_16_5_RC1(2585, 1, 16, 5, "RC1"),
    JAVA_1_16_5(2586, 1, 16, 5),
    /**
     * /entities/r.X.Z.mca files introduced.
     * Entities no longer inside region/r.X.Z.mca - except in un-migrated chunks AND (allegedly) during some phases of
     * chunk generation.
     * <p>https://www.minecraft.net/en-us/article/minecraft-snapshot-20w45a</p>
     */
    JAVA_1_17_20W45A(2681, 1, 17, 0, "20w45a"),
    JAVA_1_17_20W46A(2682, 1, 17, 0, "20w46a"),
    JAVA_1_17_20W48A(2683, 1, 17, 0, "20w48a"),
    JAVA_1_17_20W49A(2685, 1, 17, 0, "20w49a"),
    JAVA_1_17_20W51A(2687, 1, 17, 0, "20w51a"),
    JAVA_1_17_21W03A(2689, 1, 17, 0, "21w03a"),
    JAVA_1_17_21W05A(2690, 1, 17, 0, "21w05a"),
    JAVA_1_17_21W05B(2692, 1, 17, 0, "21w05b"),
    JAVA_1_17_21W06A(2694, 1, 17, 0, "21w06a"),
    JAVA_1_17_21W07A(2695, 1, 17, 0, "21w07a"),
    JAVA_1_17_21W08A(2697, 1, 17, 0, "21w08a"),
    JAVA_1_17_21W08B(2698, 1, 17, 0, "21w08b"),
    JAVA_1_17_21W10A(2699, 1, 17, 0, "21w10a"),
//    JAVA_1_17_CT6(2701, 1, 17, 0, "CT6"),
//    JAVA_1_17_CT7(2702, 1, 17, 0, "CT7"),
    JAVA_1_17_21W11A(2703, 1, 17, 0, "21w11a"),
//    JAVA_1_17_CT7B(2703, 1, 17, 0, "CT7b"), -- ambiguous data version
//    JAVA_1_17_CT7C(2704, 1, 17, 0, "CT7c"),
    JAVA_1_17_21W13A(2705, 1, 17, 0, "21w13a"),
//    JAVA_1_17_CT8(2705, 1, 17, 0, "CT8"), -- ambiguous data version
    JAVA_1_17_21W14A(2706, 1, 17, 0, "21w14a"),
//    JAVA_1_17_CT8B(2706, 1, 17, 0, "CT8b"), -- ambiguous data version
//    JAVA_1_17_CT8C(2707, 1, 17, 0, "CT8c"),
    JAVA_1_17_21W15A(2709, 1, 17, 0, "21w15a"),
    JAVA_1_17_21W16A(2711, 1, 17, 0, "21w16a"),
    JAVA_1_17_21W17A(2712, 1, 17, 0, "21w17a"),
    JAVA_1_17_21W18A(2713, 1, 17, 0, "21w18a"),
    JAVA_1_17_21W19A(2714, 1, 17, 0, "21w19a"),
    JAVA_1_17_21W20A(2715, 1, 17, 0, "21w20a"),
    JAVA_1_17_PRE1(2716, 1, 17, 0, "PRE1"),
    JAVA_1_17_PRE2(2718, 1, 17, 0, "PRE2"),
    JAVA_1_17_PRE3(2719, 1, 17, 0, "PRE3"),
    JAVA_1_17_PRE4(2720, 1, 17, 0, "PRE4"),
    JAVA_1_17_PRE5(2721, 1, 17, 0, "PRE5"),
    JAVA_1_17_RC1(2722, 1, 17, 0, "RC1"),
    JAVA_1_17_RC2(2723, 1, 17, 0, "RC2"),
    JAVA_1_17_0(2724, 1, 17, 0),
    JAVA_1_17_1_PRE1(2725, 1, 17, 1, "PRE1"),
    JAVA_1_17_1_PRE2(2726, 1, 17, 1, "PRE2"),
    JAVA_1_17_1_PRE3(2727, 1, 17, 1, "PRE3"),
    JAVA_1_17_1_RC1(2728, 1, 17, 1, "RC1"),
    JAVA_1_17_1_RC2(2729, 1, 17, 1, "RC2"),
    JAVA_1_17_1(2730, 1, 17, 1),
//    JAVA_1_18_XS1(2825, 1, 18, 0, "XS1"),
//    JAVA_1_18_XS2(2826, 1, 18, 0, "XS2"),
//    JAVA_1_18_XS3(2827, 1, 18, 0, "XS3"),
//    JAVA_1_18_XS4(2828, 1, 18, 0, "XS4"),
//    JAVA_1_18_XS5(2829, 1, 18, 0, "XS5"),
//    JAVA_1_18_XS6(2830, 1, 18, 0, "XS6"),
//    JAVA_1_18_XS7(2831, 1, 18, 0, "XS7"),
    /**
     * <a href=https://www.minecraft.net/en-us/article/minecraft-snapshot-21w39a>article 21w39a</a>
     * (yes, they didn't document these changes until a later weekly snapshot).
     * <ul>
     * <li>Level.Sections[].BlockStates &amp; Level.Sections[].Palette have moved to a container structure in Level.Sections[].block_states
     * <li>Level.Biomes are now paletted and live in a similar container structure in Level.Sections[].biomes
     * </ul>
     * <p>Tags Removed</p>
     * <ul>
     *   <li>region: Level.Biomes  &lt;IntArrayTag&gt;</li>
     *   <li>region: Level.Sections[].BlockStates  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Sections[].Palette  &lt;ListTag&lt;CompoundTag&gt;&gt;</li>
     * </ul>
     * <p>Tags Added</p>
     * <ul>
     *   <li>region: Level.Sections[].biomes  &lt;CompoundTag&gt;</li>
     *   <li>region: Level.Sections[].biomes.data  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Sections[].biomes.palette  &lt;ListTag&lt;StringTag&gt;&gt;</li>
     *   <li>region: Level.Sections[].block_states  &lt;CompoundTag&gt;</li>
     *   <li>region: Level.Sections[].block_states.data  &lt;LongArrayTag&gt;</li>
     *   <li>region: Level.Sections[].block_states.palette  &lt;ListTag&lt;CompoundTag&gt;&gt;</li>
     * </ul>
     * <p>About the New Biome Palette</p>
     * <ul><li>Consists of 64 entries, representing 4×4×4 biome regions in the chunk section.</li>
     * <li>When `palette` contains a single entry `data` will be omitted and the full chunk section is composed of a single biome.</li></ul>
     */
    // 2832 -- exact point of above noted changes
    JAVA_1_18_21W37A(2834, 1, 18, 0, "21w37a"),
    JAVA_1_18_21W38A(2835, 1, 18, 0, "21w38a"),
    JAVA_1_18_21W39A(2836, 1, 18, 0, "21w39a"),
    JAVA_1_18_21W40A(2838, 1, 18, 0, "21w40a"),
    JAVA_1_18_21W41A(2839, 1, 18, 0, "21w41a"),
    JAVA_1_18_21W42A(2840, 1, 18, 0, "21w42a"),
    /**
     * https://www.minecraft.net/en-us/article/minecraft-snapshot-21w43a
     * <ul>
     * <li>Removed chunk’s Level and moved everything it contained up
     * <li>Chunk’s Level.Entities has moved to entities -- entities are stored in the terrain region file during chunk generation
     *     <br><em>It actually appears this tag may have been removed entirely from region mca files until {@link #JAVA_1_18_2_22W03A}</em>
     *     <br><em>Note: Hilariously, the name remains capitalized in entities mca files.</em>
     * <li>Chunk’s Level.TileEntities has moved to block_entities
     * <li>Chunk’s Level.TileTicks and Level.ToBeTicked have moved to block_ticks
     * <li>Chunk’s Level.LiquidTicks and Level.LiquidsToBeTicked have moved to fluid_ticks
     * <li>Chunk’s Level.Sections has moved to sections
     * <li>Chunk’s Level.Structures has moved to structures
     * <li>Chunk’s Level.Structures.Starts has moved to structures.starts
     * <li>Chunk’s Level.Sections[].BlockStates and Level.Sections[].Palette have moved to a container structure in sections[].block_states
     * <li>Added yPos the minimum section y position in the chunk
     * <li>Added below_zero_retrogen containing data to support below zero generation
     * <li>Added blending_data containing data to support blending new world generation with existing chunks
     * </ul>
     */
    JAVA_1_18_21W43A(2844, 1, 18, 0, "21w43a"),
    JAVA_1_18_21W44A(2845, 1, 18, 0, "21w44a"),
    JAVA_1_18_PRE1(2847, 1, 18, 0, "PRE1"),
    JAVA_1_18_PRE2(2848, 1, 18, 0, "PRE2"),
    JAVA_1_18_PRE3(2849, 1, 18, 0, "PRE3"),
    JAVA_1_18_PRE4(2850, 1, 18, 0, "PRE4"),
    JAVA_1_18_PRE5(2851, 1, 18, 0, "PRE5"),
    JAVA_1_18_PRE6(2853, 1, 18, 0, "PRE6"),
    JAVA_1_18_PRE7(2854, 1, 18, 0, "PRE7"),
    JAVA_1_18_PRE8(2855, 1, 18, 0, "PRE8"),
    JAVA_1_18_RC1(2856, 1, 18, 0, "RC1"),
    JAVA_1_18_RC2(2857, 1, 18, 0, "RC2"),
    JAVA_1_18_RC3(2858, 1, 18, 0, "RC3"),
    JAVA_1_18_RC4(2859, 1, 18, 0, "RC4"),
    JAVA_1_18_0(2860, 1, 18, 0),
    JAVA_1_18_1_PRE1(2861, 1, 18, 1, "PRE1"),
    JAVA_1_18_1_RC1(2862, 1, 18, 1, "RC1"),
    JAVA_1_18_1_RC2(2863, 1, 18, 1, "RC2"),
    JAVA_1_18_1_RC3(2864, 1, 18, 1, "RC3"),
    JAVA_1_18_1(2865, 1, 18, 1),
    /**
     * <a href=https://www.minecraft.net/en-us/article/minecraft-snapshot-21w39a>article 21w39a</a> (This change was
     * noted on an earlier snapshot but didn't make it into the codebase until this one!)
     * <ul>
     * <li>Level.CarvingMasks[] is now CompoundTag containing &lt;LongArrayTag&gt;
     * instead of CompoundTag containing &lt;ByteArrayTag&gt;.
     * </ul>
     * <p>This version is also the first time the mca scan data shows the `entities` tag being present in region chunks
     * again (probably during some stage(s) of world generation). I find it unlikely that the scanned mca versions
     * between {@link #JAVA_1_18_21W43A} and this one just happen to not have any entities in the right state to be
     * stored in the region mca file - that was 20 * 25 world spawns generated and scanned between these 2 versions!</p>
     */
    JAVA_1_18_2_22W03A(2966, 1, 18, 2, "22w03a"),
    JAVA_1_18_2_22W05A(2967, 1, 18, 2, "22w05a"),
    JAVA_1_18_2_22W06A(2968, 1, 18, 2, "22w06a"),
    /**
     * `structures.References.*` and `structures.starts.*` entry name format changed to include the "minecraft:" prefix.
     * Ex. old: "buried_treasure", new: "minecraft:buried_treasure"
     */
    JAVA_1_18_2_22W07A(2969, 1, 18, 2, "22w07a"),
    JAVA_1_18_2_PRE1(2971, 1, 18, 2, "PRE1"),
    JAVA_1_18_2_PRE2(2972, 1, 18, 2, "PRE2"),
    JAVA_1_18_2_PRE3(2973, 1, 18, 2, "PRE3"),
    JAVA_1_18_2_RC1(2974, 1, 18, 2, "RC1"),
    JAVA_1_18_2(2975, 1, 18, 2),
//    JAVA_1_19_XS1(3066, 1, 19, 0, "XS1"),
    JAVA_1_19_22W11A(3080, 1, 19, 0, "22w11a"),
    JAVA_1_19_22W12A(3082, 1, 19, 0, "22w12a"),
    JAVA_1_19_22W13A(3085, 1, 19, 0, "22w13a"),
    JAVA_1_19_22W14A(3088, 1, 19, 0, "22w14a"),
    JAVA_1_19_22W15A(3089, 1, 19, 0, "22w15a"),
    JAVA_1_19_22W16A(3091, 1, 19, 0, "22w16a"),
    JAVA_1_19_22W16B(3092, 1, 19, 0, "22w16b"),
    JAVA_1_19_22W17A(3093, 1, 19, 0, "22w17a"),
    JAVA_1_19_22W18A(3095, 1, 19, 0, "22w18a"),
    JAVA_1_19_22W19A(3096, 1, 19, 0, "22w19a"),
    JAVA_1_19_PRE1(3098, 1, 19, 0, "PRE1"),
    JAVA_1_19_PRE2(3099, 1, 19, 0, "PRE2"),
    JAVA_1_19_PRE3(3100, 1, 19, 0, "PRE3"),
    JAVA_1_19_PRE4(3101, 1, 19, 0, "PRE4"),
    JAVA_1_19_PRE5(3102, 1, 19, 0, "PRE5"),
    JAVA_1_19_RC1(3103, 1, 19, 0, "RC1"),
    JAVA_1_19_RC2(3104, 1, 19, 0, "RC2"),
    JAVA_1_19_0(3105, 1, 19, 0),
    JAVA_1_19_1_22W24A(3106, 1, 19, 1, "22w24a"),
    JAVA_1_19_1_PRE1(3107, 1, 19, 1, "PRE1"),
    JAVA_1_19_1_RC1(3109, 1, 19, 1, "RC1"),
    JAVA_1_19_1_PRE2(3110, 1, 19, 1, "PRE2"),
    JAVA_1_19_1_PRE3(3111, 1, 19, 1, "PRE3"),
    JAVA_1_19_1_PRE4(3112, 1, 19, 1, "PRE4"),
    JAVA_1_19_1_PRE5(3113, 1, 19, 1, "PRE5"),
    JAVA_1_19_1_PRE6(3114, 1, 19, 1, "PRE6"),
    JAVA_1_19_1_RC2(3115, 1, 19, 1, "RC2"),
    JAVA_1_19_1_RC3(3116, 1, 19, 1, "RC3"),
    JAVA_1_19_1(3117, 1, 19, 1),
    JAVA_1_19_2_RC1(3118, 1, 19, 2, "RC1"),
    JAVA_1_19_2_RC2(3119, 1, 19, 2, "RC2"),
    JAVA_1_19_2(3120, 1, 19, 2),
    JAVA_1_19_3_22W42A(3205, 1, 19, 3, "22w42a"),
    JAVA_1_19_3_22W43A(3206, 1, 19, 3, "22w43a"),
    /** {@code Entities[].listener.selector} appears for the first time. */
    JAVA_1_19_3_22W44A(3207, 1, 19, 3, "22w44a"),
    JAVA_1_19_3_22W45A(3208, 1, 19, 3, "22w45a"),
    JAVA_1_19_3_22W46A(3210, 1, 19, 3, "22w46a"),
    JAVA_1_19_3_PRE1(3211, 1, 19, 3, "PRE1"),
    JAVA_1_19_3_PRE2(3212, 1, 19, 3, "PRE2"),
    JAVA_1_19_3_PRE3(3213, 1, 19, 3, "PRE3"),
    JAVA_1_19_3_RC1(3215, 1, 19, 3, "RC1"),
    JAVA_1_19_3_RC2(3216, 1, 19, 3, "RC2"),
    JAVA_1_19_3_RC3(3217, 1, 19, 3, "RC3"),
    JAVA_1_19_3(3218, 1, 19, 3),
    JAVA_1_19_4_23W03A(3320, 1, 19, 4, "23w03a"),
    JAVA_1_19_4_23W04A(3321, 1, 19, 4, "23w04a"),
    JAVA_1_19_4_23W05A(3323, 1, 19, 4, "23w05a"),
    JAVA_1_19_4_23W06A(3326, 1, 19, 4, "23w06a"),
    JAVA_1_19_4_23W07A(3329, 1, 19, 4, "23w07a"),
    JAVA_1_19_4_PRE1(3330, 1, 19, 4, "PRE1"),
    JAVA_1_19_4_PRE2(3331, 1, 19, 4, "PRE2"),
    JAVA_1_19_4_PRE3(3332, 1, 19, 4, "PRE3"),
    JAVA_1_19_4_PRE4(3333, 1, 19, 4, "PRE4"),
    JAVA_1_19_4_RC1(3334, 1, 19, 4, "RC1"),
    JAVA_1_19_4_RC2(3335, 1, 19, 4, "RC2"),
    JAVA_1_19_4_RC3(3336, 1, 19, 4, "RC3"),
    JAVA_1_19_4(3337, 1, 19, 4),
    JAVA_1_20_23W12A(3442, 1, 20, 0, "23w12a"),
    JAVA_1_20_23W13A(3443, 1, 20, 0, "23w13a"),
    JAVA_1_20_23W14A(3445, 1, 20, 0, "23w14a"),
    JAVA_1_20_23W16A(3449, 1, 20, 0, "23w16a"),
    JAVA_1_20_23W17A(3452, 1, 20, 0, "23w17a"),
    JAVA_1_20_23W18A(3453, 1, 20, 0, "23w18a"),
    JAVA_1_20_PRE1(3454, 1, 20, 0, "PRE1"),
    JAVA_1_20_PRE2(3455, 1, 20, 0, "PRE2"),
    JAVA_1_20_PRE3(3456, 1, 20, 0, "PRE3"),
    JAVA_1_20_PRE4(3457, 1, 20, 0, "PRE4"),
    JAVA_1_20_PRE5(3458, 1, 20, 0, "PRE5"),
    JAVA_1_20_PRE6(3460, 1, 20, 0, "PRE6"),
    JAVA_1_20_PRE7(3461, 1, 20, 0, "PRE7"),
    JAVA_1_20_RC1(3462, 1, 20, 0, "RC1"),
    JAVA_1_20_0(3463, 1, 20, 0),
    JAVA_1_20_1_RC1(3464, 1, 20, 1, "RC1"),
    JAVA_1_20_1(3465, 1, 20, 1),
    JAVA_1_20_2_23W31A(3567, 1, 20, 2, "23w31a"),
    JAVA_1_20_2_23W32A(3569, 1, 20, 2, "23w32a"),
    JAVA_1_20_2_23W33A(3570, 1, 20, 2, "23w33a"),
    JAVA_1_20_2_23W35A(3571, 1, 20, 2, "23w35a"),
    JAVA_1_20_2_PRE1(3572, 1, 20, 2, "PRE1"),
    JAVA_1_20_2_PRE2(3573, 1, 20, 2, "PRE2"),
    JAVA_1_20_2_PRE3(3574, 1, 20, 2, "PRE3"),
    JAVA_1_20_2_PRE4(3575, 1, 20, 2, "PRE4"),
    JAVA_1_20_2_RC1(3576, 1, 20, 2, "RC1"),
    JAVA_1_20_2_RC2(3577, 1, 20, 2, "RC2"),
    JAVA_1_20_2(3578, 1, 20, 2),
    JAVA_1_20_3_23W40A(3679, 1, 20, 3, "23w40a"),
    JAVA_1_20_3_23W41A(3681, 1, 20, 3, "23w41a"),
    JAVA_1_20_3_23W42A(3684, 1, 20, 3, "23w42a"),
    JAVA_1_20_3_23W43A(3686, 1, 20, 3, "23w43a"),
    JAVA_1_20_3_23W43B(3687, 1, 20, 3, "23w43b"),
    JAVA_1_20_3_23W44A(3688, 1, 20, 3, "23w44a"),
    JAVA_1_20_3_23W45A(3690, 1, 20, 3, "23w45a"),
    JAVA_1_20_3_23W46A(3691, 1, 20, 3, "23w46a"),
    JAVA_1_20_3_PRE1(3693, 1, 20, 3, "PRE1"),
    JAVA_1_20_3_PRE2(3694, 1, 20, 3, "PRE2"),
    JAVA_1_20_3_PRE3(3695, 1, 20, 3, "PRE3"),
    JAVA_1_20_3_PRE4(3696, 1, 20, 3, "PRE4"),
    JAVA_1_20_3_RC1(3697, 1, 20, 3, "RC1"),
    JAVA_1_20_3(3698, 1, 20, 3),
    JAVA_1_20_4_RC1(3699, 1, 20, 4, "RC1"),
    JAVA_1_20_4(3700, 1, 20, 4),
    JAVA_1_20_5_23W51A(3801, 1, 20, 5, "23w51a"),
    JAVA_1_20_5_23W51B(3802, 1, 20, 5, "23w51b"),
    JAVA_1_20_5_24W03A(3804, 1, 20, 5, "24w03a"),
    JAVA_1_20_5_24W03B(3805, 1, 20, 5, "24w03b"),
    JAVA_1_20_5_24W04A(3806, 1, 20, 5, "24w04a"),
    JAVA_1_20_5_24W05A(3809, 1, 20, 5, "24w05a"),
    JAVA_1_20_5_24W05B(3811, 1, 20, 5, "24w05b"),
    JAVA_1_20_5_24W06A(3815, 1, 20, 5, "24w06a"),
    JAVA_1_20_5_24W07A(3817, 1, 20, 5, "24w07a"),
    JAVA_1_20_5_24W09A(3819, 1, 20, 5, "24w09a"),
    JAVA_1_20_5_24W10A(3821, 1, 20, 5, "24w10a"),
    JAVA_1_20_5_24W11A(3823, 1, 20, 5, "24w11a"),
    JAVA_1_20_5_24W12A(3824, 1, 20, 5, "24w12a"),
    JAVA_1_20_5_24W13A(3826, 1, 20, 5, "24w13a"),
    JAVA_1_20_5_24W14A(3827, 1, 20, 5, "24w14a"),
    JAVA_1_20_5_PRE1(3829, 1, 20, 5, "PRE1"),
    JAVA_1_20_5_PRE2(3830, 1, 20, 5, "PRE2"),
    JAVA_1_20_5_PRE3(3831, 1, 20, 5, "PRE3"),
    JAVA_1_20_5_PRE4(3832, 1, 20, 5, "PRE4"),
    JAVA_1_20_5_RC1(3834, 1, 20, 5, "RC1"),
    JAVA_1_20_5_RC2(3835, 1, 20, 5, "RC2"),
    JAVA_1_20_5_RC3(3836, 1, 20, 5, "RC3"),
    JAVA_1_20_5(3837, 1, 20, 5),
    JAVA_1_20_6_RC1(3838, 1, 20, 6, "RC1"),
    JAVA_1_20_6(3839, 1, 20, 6),
    JAVA_1_21_24W18A(3940, 1, 21, 0, "24w18a"),
    JAVA_1_21_24W19A(3941, 1, 21, 0, "24w19a"),
    JAVA_1_21_24W19B(3942, 1, 21, 0, "24w19b"),
    JAVA_1_21_24W20A(3944, 1, 21, 0, "24w20a"),
    JAVA_1_21_24W21A(3946, 1, 21, 0, "24w21a"),
    JAVA_1_21_24W21B(3947, 1, 21, 0, "24w21b"),
    JAVA_1_21_PRE1(3948, 1, 21, 0, "PRE1"),
    JAVA_1_21_PRE2(3949, 1, 21, 0, "PRE2"),
    JAVA_1_21_PRE3(3950, 1, 21, 0, "PRE3"),
    JAVA_1_21_PRE4(3951, 1, 21, 0, "PRE4"),
    JAVA_1_21_RC1(3952, 1, 21, 0, "RC1"),
    JAVA_1_21_0(3953, 1, 21, 0),
    JAVA_1_21_1_RC1(3954, 1, 21, 1, "RC1"),
    JAVA_1_21_1(3955, 1, 21, 1),
    JAVA_1_21_2_24W33A(4058, 1, 21, 2, "24w33a"),
    JAVA_1_21_2_24W34A(4060, 1, 21, 2, "24w34a"),
    JAVA_1_21_2_24W35A(4062, 1, 21, 2, "24w35a"),
    JAVA_1_21_2_24W36A(4063, 1, 21, 2, "24w36a"),
    JAVA_1_21_2_24W37A(4065, 1, 21, 2, "24w37a"),
    JAVA_1_21_2_24W38A(4066, 1, 21, 2, "24w38a"),
    JAVA_1_21_2_24W39A(4069, 1, 21, 2, "24w39a"),
    JAVA_1_21_2_24W40A(4072, 1, 21, 2, "24w40a"),
    JAVA_1_21_2_PRE1(4073, 1, 21, 2, "PRE1"),
    JAVA_1_21_2_PRE2(4074, 1, 21, 2, "PRE2"),
    JAVA_1_21_2_PRE3(4075, 1, 21, 2, "PRE3"),
    JAVA_1_21_2_PRE4(4076, 1, 21, 2, "PRE4"),
    JAVA_1_21_2_PRE5(4077, 1, 21, 2, "PRE5"),
    JAVA_1_21_2_RC1(4078, 1, 21, 2, "RC1"),
    JAVA_1_21_2_RC2(4079, 1, 21, 2, "RC2"),
    JAVA_1_21_2(4080, 1, 21, 2),
    JAVA_1_21_3(4082, 1, 21, 3),
    JAVA_1_21_4_24W44A(4174, 1, 21, 4, "24w44a"),
    JAVA_1_21_4_24W45A(4177, 1, 21, 4, "24w45a"),
    JAVA_1_21_4_24W46A(4178, 1, 21, 4, "24w46a"),
    JAVA_1_21_4_PRE1(4179, 1, 21, 4, "PRE1"),
    JAVA_1_21_4_PRE2(4182, 1, 21, 4, "PRE2"),
    JAVA_1_21_4_PRE3(4183, 1, 21, 4, "PRE3"),
    JAVA_1_21_4_RC1(4184, 1, 21, 4, "RC1"),
    JAVA_1_21_4_RC2(4186, 1, 21, 4, "RC2"),
    JAVA_1_21_4_RC3(4188, 1, 21, 4, "RC3"),
    JAVA_1_21_4(4189, 1, 21, 4),
    JAVA_1_21_5_25W02A(4298, 1, 21, 5, "25w02a"),
    JAVA_1_21_5_25W03A(4304, 1, 21, 5, "25w03a"),
    JAVA_1_21_5_25W04A(4308, 1, 21, 5, "25w04a"),
    JAVA_1_21_5_25W05A(4310, 1, 21, 5, "25w05a"),
    JAVA_1_21_5_25W06A(4313, 1, 21, 5, "25w06a"),
    JAVA_1_21_5_25W07A(4315, 1, 21, 5, "25w07a"),
    JAVA_1_21_5_25W08A(4316, 1, 21, 5, "25w08a"),
    JAVA_1_21_5_25W09A(4317, 1, 21, 5, "25w09a"),
    JAVA_1_21_5_25W09B(4318, 1, 21, 5, "25w09b"),
    JAVA_1_21_5_25W10A(4319, 1, 21, 5, "25w10a"),
    JAVA_1_21_5_PRE1(4320, 1, 21, 5, "PRE1"),
    JAVA_1_21_5_PRE2(4321, 1, 21, 5, "PRE2"),
    JAVA_1_21_5_PRE3(4322, 1, 21, 5, "PRE3"),
    JAVA_1_21_5_RC1(4323, 1, 21, 5, "RC1"),
    JAVA_1_21_5_RC2(4324, 1, 21, 5, "RC2"),
    JAVA_1_21_5(4325, 1, 21, 5),
    JAVA_1_21_6_25W15A(4422, 1, 21, 6, "25w15a"),
    JAVA_1_21_6_25W16A(4423, 1, 21, 6, "25w16a"),
    JAVA_1_21_6_25W17A(4425, 1, 21, 6, "25w17a"),
    JAVA_1_21_6_25W18A(4426, 1, 21, 6, "25w18a"),
    JAVA_1_21_6_25W19A(4427, 1, 21, 6, "25w19a"),
    JAVA_1_21_6_25W20A(4428, 1, 21, 6, "25w20a"),
    JAVA_1_21_6_25W21A(4429, 1, 21, 6, "25w21a"),
    JAVA_1_21_6_PRE1(4430, 1, 21, 6, "PRE1"),
    JAVA_1_21_6_PRE2(4431, 1, 21, 6, "PRE2"),
    JAVA_1_21_6_PRE3(4432, 1, 21, 6, "PRE3"),
    JAVA_1_21_6_PRE4(4433, 1, 21, 6, "PRE4"),
    JAVA_1_21_6_RC1(4434, 1, 21, 6, "RC1"),
    JAVA_1_21_6(4435, 1, 21, 6),
    JAVA_1_21_7_RC1(4436, 1, 21, 7, "RC1"),
    JAVA_1_21_7_RC2(4437, 1, 21, 7, "RC2"),
    JAVA_1_21_7(4438, 1, 21, 7),
    JAVA_1_21_8_RC1(4439, 1, 21, 8, "RC1"),
    JAVA_1_21_8(4440, 1, 21, 8),
    JAVA_1_21_9_25W31A(4534, 1, 21, 9, "25w31a"),
    JAVA_1_21_9_25W32A(4536, 1, 21, 9, "25w32a"),
    JAVA_1_21_9_25W33A(4538, 1, 21, 9, "25w33a"),
    JAVA_1_21_9_25W34A(4539, 1, 21, 9, "25w34a"),
    JAVA_1_21_9_25W34B(4540, 1, 21, 9, "25w34b"),
    JAVA_1_21_9_25W35A(4542, 1, 21, 9, "25w35a"),
    JAVA_1_21_9_25W36A(4545, 1, 21, 9, "25w36a"),
    JAVA_1_21_9_25W36B(4546, 1, 21, 9, "25w36b"),
    JAVA_1_21_9_25W37A(4547, 1, 21, 9, "25w37a"),
    JAVA_1_21_9_PRE1(4549, 1, 21, 9, "PRE1"),
    JAVA_1_21_9_PRE2(4550, 1, 21, 9, "PRE2"),
    JAVA_1_21_9_PRE3(4551, 1, 21, 9, "PRE3"),
    JAVA_1_21_9_PRE4(4552, 1, 21, 9, "PRE4"),
    JAVA_1_21_9_RC1(4553, 1, 21, 9, "RC1"),
    JAVA_1_21_9(4554, 1, 21, 9),
    JAVA_1_21_10_RC1(4555, 1, 21, 10, "RC1"),
    JAVA_1_21_10(4556, 1, 21, 10),
    JAVA_1_21_11_25W41A(4653, 1, 21, 11, "25w41a"),
    JAVA_1_21_11_25W42A(4654, 1, 21, 11, "25w42a"),
    JAVA_1_21_11_25W43A(4655, 1, 21, 11, "25w43a"),
    JAVA_1_21_11_25W44A(4659, 1, 21, 11, "25w44a"),
    JAVA_1_21_11_25W45A(4660, 1, 21, 11, "25w45a"),
    JAVA_1_21_11_25W46A(4662, 1, 21, 11, "25w46a"),
    JAVA_1_21_11_PRE1(4663, 1, 21, 11, "PRE1"),
    JAVA_1_21_11_PRE2(4664, 1, 21, 11, "PRE2"),
    JAVA_1_21_11_PRE3(4665, 1, 21, 11, "PRE3"),
    JAVA_1_21_11_PRE4(4666, 1, 21, 11, "PRE4"),
    JAVA_1_21_11_PRE5(4667, 1, 21, 11, "PRE5"),
    JAVA_1_21_11_RC1(4668, 1, 21, 11, "RC1"),
    JAVA_1_21_11_RC2(4669, 1, 21, 11, "RC2"),
    JAVA_1_21_11_RC3(4670, 1, 21, 11, "RC3"),
    JAVA_1_21_11(4671, 1, 21, 11),
    JAVA_26_1_0_SNAPSHOT1(4764, 26, 1, 0, "SNAPSHOT-1"),
    JAVA_26_1_0_SNAPSHOT2(4765, 26, 1, 0, "SNAPSHOT-2"),
    JAVA_26_1_0_SNAPSHOT3(4767, 26, 1, 0, "SNAPSHOT-3"),
    JAVA_26_1_0_SNAPSHOT4(4768, 26, 1, 0, "SNAPSHOT-4"),
    JAVA_26_1_0_SNAPSHOT5(4770, 26, 1, 0, "SNAPSHOT-5"),
    JAVA_26_1_0_SNAPSHOT6(4774, 26, 1, 0, "SNAPSHOT-6"),
    JAVA_26_1_0_SNAPSHOT7(4775, 26, 1, 0, "SNAPSHOT-7"),
    JAVA_26_1_0_SNAPSHOT8(4776, 26, 1, 0, "SNAPSHOT-8"),
    JAVA_26_1_0_SNAPSHOT9(4777, 26, 1, 0, "SNAPSHOT-9"),
    JAVA_26_1_0_SNAPSHOT10(4778, 26, 1, 0, "SNAPSHOT-10"),
    JAVA_26_1_0_SNAPSHOT11(4779, 26, 1, 0, "SNAPSHOT-11"),
    JAVA_26_1_0_PRE1(4780, 26, 1, 0, "PRE-1"),
    JAVA_26_1_0_PRE2(4781, 26, 1, 0, "PRE-2"),
    JAVA_26_1_0_PRE3(4782, 26, 1, 0, "PRE-3"),
    JAVA_26_1_0_RC1(4783, 26, 1, 0, "RC-1"),
    JAVA_26_1_0_RC2(4784, 26, 1, 0, "RC-2"),
    JAVA_26_1_0_RC3(4785, 26, 1, 0, "RC-3"),
    JAVA_26_1_0(4786, 26, 1, 0),
    JAVA_26_1_1_RC1(4787, 26, 1, 1, "RC-1"),
    JAVA_26_1_1(4788, 26, 1, 1),
    // April-fools joke fork of 26.1.1, this enum value was redacted due to the use of an out-of-order data version.
    // JAVA_26_1_1_26W14A(5000, 26, 1, 1, "26w14a"),
    JAVA_26_1_2_RC1(4789, 26, 1, 2, "RC-1"),
    JAVA_26_1_2(4790, 26, 1, 2),
    JAVA_26_2_0_SNAPSHOT1(4883, 26, 2, 0, "SNAPSHOT-1"),
    JAVA_26_2_0_SNAPSHOT2(4884, 26, 2, 0, "SNAPSHOT-2"),
    JAVA_26_2_0_SNAPSHOT3(4886, 26, 2, 0, "SNAPSHOT-3"),
    JAVA_26_2_0_SNAPSHOT4(4887, 26, 2, 0, "SNAPSHOT-4"),
    JAVA_26_2_0_SNAPSHOT5(4889, 26, 2, 0, "SNAPSHOT-5"),
    JAVA_26_2_0_SNAPSHOT6(4890, 26, 2, 0, "SNAPSHOT-6"),
    JAVA_26_2_0_SNAPSHOT7(4891, 26, 2, 0, "SNAPSHOT-7"),
    JAVA_26_2_0_SNAPSHOT8(4893, 26, 2, 0, "SNAPSHOT-8"),
    JAVA_26_2_0_PRE1(4894, 26, 2, 0, "PRE-1"),
    JAVA_26_2_0_PRE2(4895, 26, 2, 0, "PRE-2"),
    JAVA_26_2_0_PRE3(4896, 26, 2, 0, "PRE-3"),
    JAVA_26_2_0_PRE4(4897, 26, 2, 0, "PRE-4"),
    JAVA_26_2_0_PRE5(4898, 26, 2, 0, "PRE-5"),
    JAVA_26_2_0_PRE6(4900, 26, 2, 0, "PRE-6"),
    JAVA_26_2_0_RC1(4901, 26, 2, 0, "RC-1"),
    JAVA_26_2_0_RC2(4902, 26, 2, 0, "RC-2"),
    JAVA_26_2_0(4903, 26, 2, 0),
    JAVA_26_3_0_SNAPSHOT1(4998, 26, 3, 0, "SNAPSHOT-1"),
    JAVA_26_3_0_SNAPSHOT2(4999, 26, 3, 0, "SNAPSHOT-2"),
    JAVA_26_3_0_SNAPSHOT3(5001, 26, 3, 0, "SNAPSHOT-3"),
    JAVA_26_3_0_SNAPSHOT4(5003, 26, 3, 0, "SNAPSHOT-4"),
    JAVA_26_3_0_SNAPSHOT5(5004, 26, 3, 0, "SNAPSHOT-5"),
    JAVA_26_3_0_SNAPSHOT6(5005, 26, 3, 0, "SNAPSHOT-6"),
    JAVA_26_3_0_SNAPSHOT7(5009, 26, 3, 0, "SNAPSHOT-7"),
    ;

    private static final int[] ids;
    private static final DataVersion latestFullReleaseVersion;
    private final int id;
    private final int major;
    private final int minor;
    private final int patch;
    private final boolean isFullRelease;
    private final boolean isWeeklyRelease;
    private final String buildDescription;
    private final String str;
    private final String simpleStr;

    static {
        // enum is maintained in order with a unit test to enforce the convention - so no need to sort
        ids = Arrays.stream(values()).mapToInt(DataVersion::id).toArray();
        latestFullReleaseVersion = Arrays.stream(values())
                .sorted(Comparator.reverseOrder())
                .filter(DataVersion::isFullRelease)
                .findFirst().get();
    }

    DataVersion(int id, int major, int minor, int patch) {
        this(id, major, minor, patch, null);
    }

    /**
     * @param id data version
     * @param minor minor version
     * @param patch patch number, LT0 to indicate this data version is not a full release version
     * @param buildDescription Suggested convention (unit test enforced): <ul>
     *                         <li>NULL (given value ignored) for full release</li>
     *                         <li>CT# for combat tests (e.g. CT6, CT6b)</li>
     *                         <li>XS# for experimental snapshots(e.g. XS1, XS2)</li>
     *                         <li>YYwWWz for weekly builds (e.g. 21w37a, 21w37b)</li>
     *                         <li>PRE# for pre-releases (e.g. PRE1, PRE2)</li>
     *                         <li>RC# for release candidates (e.g. RC1, RC2)</li></ul>
     */
    DataVersion(int id, int major, int minor, int patch, String buildDescription) {
        this.isFullRelease = buildDescription == null || "FINAL".equalsIgnoreCase(buildDescription);
        if (!isFullRelease && buildDescription.isEmpty())
            throw new IllegalArgumentException("buildDescription required for non-full releases");
        this.isWeeklyRelease = buildDescription != null && buildDescription.length() >= 5 && buildDescription.charAt(2) == 'w';
        this.id = id;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.buildDescription = isFullRelease ? "FINAL" : buildDescription;

        StringBuilder sb = new StringBuilder();
        if (major > 0 && minor > 0) {
            sb.append(id).append(" (").append(major).append('.').append(minor);
            if (patch > 0) sb.append('.').append(patch);
            if (!isFullRelease) sb.append(' ').append(buildDescription);
            this.str = sb.append(')').toString();
        } else {
            this.str = name();
        }

        StringBuilder simpleStrBuilder = new StringBuilder();
        if (isWeeklyRelease) {
            simpleStrBuilder.append(buildDescription);
        } else {
            simpleStrBuilder.append(major).append('.').append(minor);
            if (patch != 0) {
                simpleStrBuilder.append('.').append(patch);
            }
            if (buildDescription != null) {
                simpleStrBuilder.append('-').append(buildDescription.toLowerCase(Locale.ENGLISH));
            }
        }
        simpleStr = simpleStrBuilder.toString();
    }

    public int id() {
        return id;
    }

    /**
     * Version format: major.minor.patch
     */
    public int major() {
        return major;
    }

    /**
     * Version format: major.minor.patch
     */
    public int minor() {
        return minor;
    }

    /**
     * Version format: major.minor.patch
     */
    public int patch() {
        return patch;
    }

    /**
     * True for full release.
     * False for all other builds (e.g. experimental, pre-releases, and release-candidates).
     */
    public boolean isFullRelease() {
        return isFullRelease;
    }

    public boolean isWeeklyRelease() {
        return isWeeklyRelease;
    }

    /**
     * Description of the minecraft build which this {@link DataVersion} refers to.
     * You'll find {@link #toString()} to be more useful in general.
     * <p>Convention used: <ul>
     * <li>"FULL" for full release</li>
     * <li>YYwWWz for weekly builds (e.g. 21w37a, 21w37b)</li>
     * <li>CT# for combat tests (e.g. CT6, CT6b)</li>
     * <li>XS# for experimental snapshots(e.g. XS1, XS2)</li>
     * <li>PR# for pre-releases (e.g. PR1, PR2)</li>
     * <li>RC# for release candidates (e.g. RC1, RC2)</li></ul>
     */
    public String getBuildDescription() {
        return buildDescription;
    }

    /**
     * TRUE as of JAVA_1_14_PRE1
     * Indicates if point of interest .mca files exist. E.g. 'poi/r.0.0.mca'
     * <p>Technically poi files were introduced with {@link #JAVA_1_14_19W11A} but the nbt structure was quickly
     * changed and this 3 week span of weekly versions isn't worth the hassle of supporting.</p>
     * @since {@link #JAVA_1_14_PRE1}
     */
    public boolean hasPoiMca() {
        return this.id >= JAVA_1_14_PRE1.id;
    }

    /**
     * TRUE as of 1.17
     * Entities were pulled out of terrain 'region/r.X.Z.mca' files into their own .mca files. E.g. 'entities/r.0.0.mca'
     */
    public boolean hasEntitiesMca() {
        return this.id >= JAVA_1_17_20W45A.id;
    }

    public static DataVersion bestFor(int dataVersion) {
        int found = Arrays.binarySearch(ids, dataVersion);
        if (found < 0) {
            found = (found + 2) * -1;
            if (found < 0) return UNKNOWN;
        }
        return values()[found];
    }

    /**
     * @param simpleVersionStr such as "1.12", "21w13a", "1.19.1-pre3"
     * @return exact match or null
     */
    public static DataVersion find(String simpleVersionStr) {
        final String seeking = simpleVersionStr.toLowerCase(Locale.ENGLISH);
        return Arrays.stream(values()).filter(v -> v.simpleStr.equals(seeking)).findFirst().orElse(null);
    }

    /**
     * @return The previous known data version or null if there is none.
     */
    public DataVersion previous() {
        if (this.ordinal() > 0)
            return values()[this.ordinal() - 1];
        else
            return null;
    }

    /**
     * @return The next known data version or null if there is none.
     */
    public DataVersion next() {
        if (this.ordinal() < ids.length - 1)
            return values()[this.ordinal() + 1];
        else
            return null;
    }

    /**
     * @return The latest full release (non-weekly, non pre-release, etc) version defined.
     */
    public static DataVersion latest() {
        return latestFullReleaseVersion;
    }

    @Override
    public String toString() {
        return str;
    }

    public String toSimpleString() {
        return simpleStr;
    }

    /**
     * Indicates if this version would be crossed by the transition between versionA and versionB.
     * This is useful for determining if a data upgrade or downgrade would be required to support
     * changing from versionA to versionB. The order of A and B don't matter.
     *
     * <p>When using this function, call it on the data version in which a change exists. For
     * example if you need to know if changing from A to B would require changing to/from 3D
     * biomes then use {@code JAVA_1_15_19W36A.isCrossedByTransition(A, B)} as
     * {@link #JAVA_1_15_19W36A} is the version which added 3D biomes.</p>
     *
     * <p>In short, if this function returns true then the act of changing data versions from A
     * to B can be said to "cross" this version which is an indication that such a change should
     * either be considered illegal or that upgrade/downgrade action is required.</p>
     *
     * @param versionA older or newer data version than B
     * @param versionB older or newer data version than A
     * @return true if chaining from version A to version B, or form B to A, would result in
     * crossing this version. This version is considered to be crossed if {@code A != B} and
     * {@code min(A, B) < this.id <= max(A, B)}
     * @see #throwUnsupportedVersionChangeIfCrossed(int, int)
     */
    public boolean isCrossedByTransition(int versionA, int versionB) {
        if (versionA == versionB) return false;
        if (versionA < versionB) {
            return versionA < id && id <= versionB;
        } else {
            return versionB < id && id <= versionA;
        }
    }

    /**
     * Throws {@link UnsupportedVersionChangeException} if {@link #isCrossedByTransition(int, int)}
     * were to return true for the given arguments.
     */
    public void throwUnsupportedVersionChangeIfCrossed(int versionA, int versionB) {
        if (isCrossedByTransition(versionA, versionB)) {
            throw new UnsupportedVersionChangeException(this, versionA, versionB);
        }
    }
}
