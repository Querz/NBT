package net.querz.nbt;

import net.querz.NBTTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestListTag extends NBTTestCase {

    @Test
    public void testIterateEmpty() {
        ListTag list = new ListTag();
        for (ByteTag t : list.iterateType(ByteTag.class)) {
            fail("iteration for an empty list should not have elements, got: "+t.toString());
        }
    }

    @Test
    public void testModifyIteration() {
        ListTag list = new ListTag();
        List<ByteTag> iteration = list.iterateType(ByteTag.class);
        ByteTag tag = ByteTag.valueOf((byte) 123);
        iteration.add(tag);
        assertEquals(tag, list.get(0));
    }

    @Test
    public void testIterateLaterAddedWrongType() {
        ListTag list = new ListTag();
        List<ByteTag> iteration = list.iterateType(ByteTag.class);
        list.add(IntTag.valueOf(123));
        assertThrows(ClassCastException.class, () -> iteration.get(0));
    }

    @Test
    public void testIterateLaterAddedCorrectType() {
        ListTag list = new ListTag();
        List<ByteTag> iteration = list.iterateType(ByteTag.class);
        list.addByte((byte) 123);
        assertEquals(123, iteration.get(0).asByte());
    }

}
