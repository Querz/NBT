package net.querz.mca;

import net.querz.mca.parsers.*;
import net.querz.mca.parsers.impl.anvil117.EntityParser117;
import net.querz.mca.parsers.impl.anvil118.*;
import net.querz.nbt.CompoundTag;
import java.util.*;
import java.util.function.Function;

public final class ParserHandler {

	private ParserHandler() {}

	private static final NavigableMap<Integer, Function<CompoundTag, BlockParser<?>>> blockParsers = new TreeMap<>();
	private static final NavigableMap<Integer, Function<CompoundTag, BiomeParser<?>>> biomeParsers = new TreeMap<>();
	private static final NavigableMap<Integer, Function<CompoundTag, HeightmapParser>> heightmapParsers = new TreeMap<>();
	private static final NavigableMap<Integer, Function<CompoundTag, SectionParser>> sectionParsers = new TreeMap<>();
	private static final NavigableMap<Integer, Function<CompoundTag, BlockEntityParser>> blockEntityParsers = new TreeMap<>();
	private static final NavigableMap<Integer, Function<CompoundTag, EntityParser>> entityParsers = new TreeMap<>();

	static {
		registerBlockParser(2860, BlockParser118::new);

		registerBiomeParser(2860, BiomeParser118::new);

		registerHeightmapParser(2860, HeightmapParser118::new);

		registerSectionParser(2860, SectionParser118::new);

		registerBlockEntityParser(2860, BlockEntityParser118::new);

		registerEntityParser(2724, EntityParser117::new);

		fillMap(blockParsers);
		fillMap(biomeParsers);
		fillMap(heightmapParsers);
		fillMap(sectionParsers);
		fillMap(blockEntityParsers);
		fillMap(entityParsers);
	}

	private static <T extends DataParser<?>> void fillMap(NavigableMap<Integer, Function<CompoundTag, T>> map) {
		Map.Entry<Integer, Function<CompoundTag, T>> last = map.lastEntry();
		Map.Entry<Integer, Function<CompoundTag, T>> ceil = map.firstEntry();
		for (int i = 100; i <= last.getKey(); i++) {
			if (ceil.getKey() <= i) {
				map.put(i, ceil.getValue());
			} else {
				ceil = map.ceilingEntry(i);
			}
		}
	}

	public static void registerBlockParser(int dataVersion, Function<CompoundTag, BlockParser<?>> constructor) {
		blockParsers.put(dataVersion, constructor);
	}

	public static void registerBiomeParser(int dataVersion, Function<CompoundTag, BiomeParser<?>> constructor) {
		biomeParsers.put(dataVersion, constructor);
	}

	public static void registerHeightmapParser(int dataVersion, Function<CompoundTag, HeightmapParser> constructor) {
		heightmapParsers.put(dataVersion, constructor);
	}

	public static void registerSectionParser(int dataVersion, Function<CompoundTag, SectionParser> constructor) {
		sectionParsers.put(dataVersion, constructor);
	}

	public static void registerBlockEntityParser(int dataVersion, Function<CompoundTag, BlockEntityParser> constructor) {
		blockEntityParsers.put(dataVersion, constructor);
	}

	public static void registerEntityParser(int dataVersion, Function<CompoundTag, EntityParser> constructor) {
		entityParsers.put(dataVersion, constructor);
	}

	private static <T extends DataParser<?>> T checkConstructor(NavigableMap<Integer, Function<CompoundTag, T>> map, int dataVersion, CompoundTag input) {
		Function<CompoundTag, T> constructor = map.get(dataVersion);
		if (constructor == null) {
			constructor = map.floorEntry(dataVersion).getValue();
			map.put(dataVersion, constructor);
		}
		return constructor.apply(input);
	}

	public static BlockParser<?> getBlockParser(int dataVersion, CompoundTag section) {
		return checkConstructor(blockParsers, dataVersion, section);
	}

	public static BiomeParser<?> getBiomeParser(int dataVersion, CompoundTag chunkOrSection) {
		return checkConstructor(biomeParsers, dataVersion, chunkOrSection);
	}

	public static HeightmapParser getHeightmapParser(int dataVersion, CompoundTag chunk) {
		return checkConstructor(heightmapParsers, dataVersion, chunk);
	}

	public static SectionParser getSectionParser(int dataVersion, CompoundTag chunk) {
		return checkConstructor(sectionParsers, dataVersion, chunk);
	}

	public static BlockEntityParser getBlockEntityParser(int dataVersion, CompoundTag chunk) {
		return checkConstructor(blockEntityParsers, dataVersion, chunk);
	}

	public static EntityParser getEntityParser(int dataVersion, CompoundTag chunk) {
		return checkConstructor(entityParsers, dataVersion, chunk);
	}
}
