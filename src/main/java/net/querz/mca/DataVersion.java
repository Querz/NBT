package net.querz.mca;

import java.util.Arrays;
import java.util.Comparator;

// source: version.json file, found in the root directory of the client and server jars
// table of versions can also be found on https://minecraft.fandom.com/wiki/Data_version#List_of_data_versions
public enum DataVersion {
    // Kept in ASC order
    UNKNOWN(0, 0, 0),
    JAVA_1_9_0(169, 9, 0),
    JAVA_1_9_1(175, 9, 1),
    JAVA_1_9_2(176, 9, 2),
    JAVA_1_9_3(183, 9, 3),
    JAVA_1_9_4(184, 9, 4),

    JAVA_1_10_0(510, 10, 0),
    JAVA_1_10_1(511, 10, 1),
    JAVA_1_10_2(512, 10, 2),

    JAVA_1_11_0(819, 11, 0),
    JAVA_1_11_1(921, 11, 1),
    JAVA_1_11_2(922, 11, 2),

    JAVA_1_12_0(1139, 12, 0),
    JAVA_1_12_1(1241, 12, 1),
    JAVA_1_12_2(1343, 12, 2),

    JAVA_1_13_0(1519, 13, 0),
    JAVA_1_13_1(1628, 13, 1),
    JAVA_1_13_2(1631, 13, 2),

    // poi/r.X.Z.mca files introduced
    JAVA_1_14_0(1952, 14, 0),
    JAVA_1_14_1(1957, 14, 1),
    JAVA_1_14_2(1963, 14, 2),
    JAVA_1_14_3(1968, 14, 3),
    JAVA_1_14_4(1976, 14, 4),

    // 3D Biomes added. Biomes array in the  Level tag for each chunk changed
    // to contain 1024 integers instead of 256 see {@link Chunk}
    JAVA_1_15_19W36A(2203, 10, -1, "19w36a"),
    JAVA_1_15_0(2225, 15, 0),
    JAVA_1_15_1(2227, 15, 1),
    JAVA_1_15_2(2230, 15, 2),

    // block pallet packing changed in this version - see {@link Section}
    JAVA_1_16_20W17A(2529, 16, -1, "20w17a"),
    JAVA_1_16_0(2566, 16, 0),
    JAVA_1_16_1(2567, 16, 1),
    JAVA_1_16_2(2578, 16, 2),
    JAVA_1_16_3(2580, 16, 3),
    JAVA_1_16_4(2584, 16, 4),
    JAVA_1_16_5(2586, 16, 5),

    // entities/r.X.Z.mca files introduced
    // entities no longer inside region/r.X.Z.mca - except in un-migrated chunks of course
    JAVA_1_17_0(2724, 17, 0),
    JAVA_1_17_1(2730, 17, 1),

    // fist experimental 1.18 build
    JAVA_1_18_XS1(2825, 18, -1, "XS1"),

    // https://www.minecraft.net/en-us/article/minecraft-snapshot-21w39a
    // Level.Sections[].BlockStates & Level.Sections[].Palette have moved to a container structure in Level.Sections[].block_states
    // Level.Biomes are now paletted and live in a similar container structure in Level.Sections[].biomes
    // Level.CarvingMasks[] is now long[] instead of byte[]
    JAVA_1_18_21W39A(2836, 18, -1, "21w39a"),

    // https://www.minecraft.net/en-us/article/minecraft-snapshot-21w43a
    // Removed chunk’s Level and moved everything it contained up
    // Chunk’s Level.Entities has moved to entities
    // Chunk’s Level.TileEntities has moved to block_entities
    // Chunk’s Level.TileTicks and Level.ToBeTicked have moved to block_ticks
    // Chunk’s Level.LiquidTicks and Level.LiquidsToBeTicked have moved to fluid_ticks
    // Chunk’s Level.Sections has moved to sections
    // Chunk’s Level.Structures has moved to structures
    // Chunk’s Level.Structures.Starts has moved to structures.starts
    // Chunk’s Level.Sections[].BlockStates and Level.Sections[].Palette have moved to a container structure in sections[].block_states
    // Chunk’s Level.Biomes are now paletted and live in a similar container structure in sections[].biomes
    // Added yPos the minimum section y position in the chunk
    // Added below_zero_retrogen containing data to support below zero generation
    // Added blending_data containing data to support blending new world generation with existing chunks
    JAVA_1_18_21W43A(2844, 18, -1, "21w43a");


    private static final int[] ids;
    private static final DataVersion latestFullReleaseVersion;
    private final int id;
    private final int minor;
    private final int patch;
    private final boolean isFullRelease;
    private final String buildDescription;
    private final String str;

    static {
        ids = Arrays.stream(values()).sorted().mapToInt(DataVersion::id).toArray();
        latestFullReleaseVersion = Arrays.stream(values())
                .sorted(Comparator.reverseOrder())
                .filter(DataVersion::isFullRelease)
                .findFirst().get();
    }

    DataVersion(int id, int minor, int patch) {
        this(id, minor, patch, null);
    }

    /**
     * @param id data version
     * @param minor minor version
     * @param patch patch number, LT0 to indicate this data version is not a full release version
     * @param buildDescription Suggested convention: <ul>
     *                         <li>NULL for full release</li>
     *                         <li>CT# for combat tests (e.g. CT6, CT6b)</li>
     *                         <li>XS# for experimental snapshots(e.g. XS1, XS2)</li>
     *                         <li>YYwWWz for weekly builds (e.g. 21w37a, 21w37b)</li>
     *                         <li>PR# for pre-releases (e.g. PR1, PR2)</li>
     *                         <li>RC# for release candidates (e.g. RC1, RC2)</li></ul>
     */
    DataVersion(int id, int minor, int patch, String buildDescription) {
        this.isFullRelease = patch >= 0;
        if (!isFullRelease && (buildDescription == null || buildDescription.isEmpty()))
            throw new IllegalArgumentException("buildDescription required for non-full releases");
        if (isFullRelease && (buildDescription != null && !buildDescription.isEmpty()))
            throw new IllegalArgumentException("buildDescription not allowed for full releases");
        this.id = id;
        this.minor = minor;
        this.patch = isFullRelease ? patch : -1;
        this.buildDescription = isFullRelease ? "FINAL" : buildDescription;
        if (minor > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(id).append(" (1.").append(minor);
            if (patch > 0) sb.append('.').append(patch);
            if (!isFullRelease) sb.append(' ').append(buildDescription);
            this.str = sb.append(')').toString();
        } else {
            this.str = name();
        }
    }

    public int id() {
        return id;
    }

    /**
     * Version format: major.minor.patch
     */
    public int major() {
        return 1;
    }

    /**
     * Version format: major.minor.patch
     */
    public int minor() {
        return minor;
    }

    /**
     * Version format: major.minor.patch
     * <p>This value will be &lt; 0 if this is not a full release version.</p>
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
     * TRUE as of 1.14
     * Indicates if point of interest .mca files exist. E.g. 'poi/r.0.0.mca'
     */
    public boolean hasPoiMca() {
        return minor >= 14;
    }

    /**
     * TRUE as of 1.17
     * Entities were pulled out of region .mca files into their own .mca files. E.g. 'entities/r.0.0.mca'
     */
    public boolean hasEntitiesMca() {
        return minor >= 17;
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
     * @return The latest full release version defined.
     */
    public static DataVersion latest() {
        return latestFullReleaseVersion;
    }

    @Override
    public String toString() {
        return str;
    }
}
