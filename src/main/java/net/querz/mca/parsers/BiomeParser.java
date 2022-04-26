package net.querz.mca.parsers;

public interface BiomeParser<T> extends DataParser<T>, CachedParser {

	String getBiomeAt(int blockX, int blockY, int blockZ);

	void setBiomeAt(int blockX, int blockY, int blockZ, String data);
}
