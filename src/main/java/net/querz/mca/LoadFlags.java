package net.querz.mca;

public final class LoadFlags {

	public static final long BIOMES               = 0x00001;
	public static final long HEIGHTMAPS           = 0x00002;
	public static final long CARVING_MASKS        = 0x00004;
	public static final long ENTITIES             = 0x00008;
	public static final long TILE_ENTITIES        = 0x00010;
	public static final long TILE_TICKS           = 0x00040;
	public static final long LIQUID_TICKS         = 0x00080;
	public static final long TO_BE_TICKED         = 0x00100;
	public static final long POST_PROCESSING      = 0x00200;
	public static final long STRUCTURES           = 0x00400;
	public static final long BLOCK_LIGHTS         = 0x00800;
	public static final long BLOCK_STATES         = 0x01000;
	public static final long SKY_LIGHT            = 0x02000;
	public static final long LIGHTS               = 0x04000;
	public static final long LIQUIDS_TO_BE_TICKED = 0x08000;
	public static final long RAW                  = 0x10000;

	public static final long ALL_DATA             = 0xffffffffffffffffL;
}
