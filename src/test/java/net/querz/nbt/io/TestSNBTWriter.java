package net.querz.nbt.io;

import net.querz.NBTTestCase;
import net.querz.nbt.NBTUtil;
import net.querz.nbt.Tag;
import net.querz.nbt.io.snbt.SNBTParser;
import net.querz.nbt.io.snbt.SNBTWriter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSNBTWriter extends NBTTestCase {

    @Test
    public void symmetric() throws IOException {
        Tag tag = NBTUtil.read(resourceAsStream(this, "everything.nbt"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new SNBTWriter().write(baos, tag);
        Tag parsed = new SNBTParser(baos.toString(StandardCharsets.UTF_8)).parse();

        assertEquals(tag, parsed);
    }

}
