package net.querz.nbt.mca;

import net.querz.nbt.CompoundTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.NBTTestCase;

public abstract class MCATestCase extends NBTTestCase {

	public CompoundTag block(String name) {
		CompoundTag c = new CompoundTag();
		c.putString("Name", name);
		return c;
	}

	public CompoundTag getSection(CompoundTag chunk, int y) {
		for (CompoundTag section : chunk.getCompoundTag("Level").getListTag("Sections").asCompoundTagList()) {
			if (section.getByte("Y") == y) {
				return section;
			}
		}
		fail("could not find section");
		return null;
	}

	public CompoundTag getSomeCompoundTag() {
		CompoundTag c = new CompoundTag();
		c.putString("Dummy", "dummy");
		return c;
	}

	public ListTag<CompoundTag> getSomeCompoundTagList() {
		ListTag<CompoundTag> l = new ListTag<>(CompoundTag.class);
		l.add(getSomeCompoundTag());
		l.add(getSomeCompoundTag());
		return l;
	}

	public ListTag<ListTag<?>> getSomeListTagList() {
		ListTag<ListTag<?>> l = new ListTag<>(ListTag.class);
		l.add(getSomeCompoundTagList());
		l.add(getSomeCompoundTagList());
		return l;
	}

	public static String longToBinaryString(long n) {
		StringBuilder s = new StringBuilder(Long.toBinaryString(n));
		for (int i = s.length(); i < 64; i++) {
			s.insert(0, "0");
		}
		return s.toString();
	}

	public static String intToBinaryString(int n) {
		StringBuilder s = new StringBuilder(Integer.toBinaryString(n));
		for (int i = s.length(); i < 32; i++) {
			s.insert(0, "0");
		}
		return s.toString();
	}
}
