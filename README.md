# NBT
[![Build Status](https://travis-ci.org/Querz/NBT.svg?branch=master)](https://travis-ci.org/Querz/NBT) [![Coverage Status](https://img.shields.io/coveralls/github/Querz/NBT/master.svg)](https://coveralls.io/github/Querz/NBT?branch=master) [![Release](https://jitpack.io/v/Querz/NBT.svg)](https://jitpack.io/#Querz/NBT)
#### A java implementation of the [NBT protocol](http://minecraft.gamepedia.com/NBT_format), including a way to implement custom tags.
---
### Specification
According to the [specification](https://minecraft.gamepedia.com/NBT_format), there are currently 13 different types of tags:

| Tag class    | Superclass | ID | Payload |
| ---------    | ---------- | -- | ----------- |
| [EndTag](src/main/java/net/querz/nbt/EndTag.java)             | [Tag](src/main/java/net/querz/nbt/Tag.java)               | 0  | None |
| [ByteTag](src/main/java/net/querz/nbt/ByteTag.java)           | [NumberTag](src/main/java/net/querz/nbt/NumberTag.java)   | 1  | 1 byte / 8 bits, signed |
| [ShortTag](src/main/java/net/querz/nbt/ShortTag.java)         | [NumberTag](src/main/java/net/querz/nbt/NumberTag.java)   | 2  | 2 bytes / 16 bits, signed, big endian |
| [IntTag](src/main/java/net/querz/nbt/IntTag.java)             | [NumberTag](src/main/java/net/querz/nbt/NumberTag.java)   | 3  | 4 bytes / 32 bits, signed, big endian |
| [LongTag](src/main/java/net/querz/nbt/LongTag.java)           | [NumberTag](src/main/java/net/querz/nbt/NumberTag.java)   | 4  | 8 bytes / 64 bits, signed, big endian |
| [FloatTag](src/main/java/net/querz/nbt/FloatTag.java)         | [NumberTag](src/main/java/net/querz/nbt/NumberTag.java)   | 5  | 4 bytes / 32 bits, signed, big endian, IEEE 754-2008, binary32 |
| [DoubleTag](src/main/java/net/querz/nbt/DoubleTag.java)       | [NumberTag](src/main/java/net/querz/nbt/NumberTag.java)   | 6  | 8 bytes / 64 bits, signed, big endian, IEEE 754-2008, binary64 |
| [ByteArrayTag](src/main/java/net/querz/nbt/ByteArrayTag.java) | [ArrayTag](src/main/java/net/querz/nbt/ArrayTag.java)     | 7  | `IntTag` payload *size*, then *size* `ByteTag` payloads |
| [StringTag](src/main/java/net/querz/nbt/StringTag.java)       | [Tag](src/main/java/net/querz/nbt/Tag.java)               | 8  | `ShortTag` payload *length*, then a UTF-8 string with size *length* |
| [ListTag](src/main/java/net/querz/nbt/ListTag.java)           | [Tag](src/main/java/net/querz/nbt/Tag.java)               | 9  | `ByteTag` payload *tagId*, then `IntTag` payload *size*, then *size* tags' payloads, all of type *tagId* |
| [CompoundTag](src/main/java/net/querz/nbt/CompoundTag.java)   | [Tag](src/main/java/net/querz/nbt/Tag.java)               | 10 | Fully formed tags, followed by an `EndTag` |
| [IntArrayTag](src/main/java/net/querz/nbt/IntArrayTag.java)   | [ArrayTag](src/main/java/net/querz/nbt/ArrayTag.java)     | 11 | `IntTag` payload *size*, then *size* `IntTag` payloads |
| [LongArrayTag](src/main/java/net/querz/nbt/LongArrayTag.java) | [ArrayTag](src/main/java/net/querz/nbt/ArrayTag.java)     | 12 | `IntTag` payload *size*, then *size* `LongTag` payloads |

* The `EndTag` is only used to mark the end of a `CompoundTag` in its serialized state or an empty `ListTag`.

* The maximum depth of the NBT structure is 512. If the depth exceeds this restriction during serialization, deserialization or String conversion, a `MaxDepthReachedException` is thrown. This usually happens when a circular reference exists in the NBT structure. The NBT specification does not allow circular references, as there is no tag to represent this.

---
### Example usage:
The following code snippet shows how to create a `CompoundTag`:
```java
CompoundTag ct = new CompoundTag();

ct.put("byte", new ByteTag((byte) 1));
ct.put("double", new DoubleTag(1.234));
ct.putString("string", "stringValue");
```
An example how to use a `ListTag`:
```java
ListTag<FloatTag> fl = new ListTag<>(FloatTag.class);

fl.add(new FloatTag(1.234f);
fl.addFloat(5.678f);
```

#### Nesting
All methods serializing instances or deserializing data track the nesting levels to prevent circular references or malicious data which could, when deserialized, result in thousands of instances causing a denial of service.

These methods have a parameter for the maximum nesting depth they are allowed to traverse. A value of `0` means that only the object itself, but no nested object may be processed.

If an instance is nested further than allowed, a [MaxDepthReachedException](src/main/java/net/querz/nbt/MaxDepthReachedException.java) will be thrown. A negative maximum depth will cause an `IllegalArgumentException`.

Some methods do not provide a parameter to specify the maximum depth, but instead use `Tag.DEFAULT_MAX_DEPTH` (`512`) which is also the maximum used in Minecraft.

---
### Utility
There are several utility methods to make your life easier if you use this library.
#### NBTUtil
`NBTUtil.writeTag()` lets you write a Tag into a gzip compressed or uncompressed file in one line (not counting exception handling). Files are gzip compressed by default.

Example usage:
```java
NBTUtil.writeTag(tag, "filename.dat");
```
`NBTUtil.readTag()` reads any file containing NBT data. No worry about compression, it will automatically uncompress gzip compressed files.

Example usage:
```java
Tag<?> tag = NBTUtil.readTag("filename.dat");
```
#### Playing Minecraft?
Each tag can be converted into a JSON-like NBT String used in Minecraft commands.

Example usage:
```java
CompoundTag c = new CompoundTag();
c.putByte("blah", (byte) 5);
c.putString("foo", "bär");
System.out.println(c.toTagString()); // {blah:5b,foo:"bär"}

ListTag<StringTag> s = new ListTag<>(StringTag.class);
s.addString("test");
s.add(new StringTag("text"));
c.add("list", s);
System.out.println(c.toTagString()); // {blah:5b,foo:"bär",list:[test,text]}

```
There is also a tool to read, change and write MCA files.

Here are some examples:
```java
// This changes the InhabitedTime field of the chunk at x=68, z=81 to 0
MCAFile mcaFile = MCAUtil.readMCAFile("r.2.2.mca");
Chunk chunk = mcaFile.getChunk(68, 81);
chunk.setInhabitedTime(0);
MCAUtil.writeMCAFile("r.2.2.mca", mcaFile);
```
There is also an optimized api to retrieve and set block information (BlockStates) in MCA files.

Example:
```java
// Retrieves block information from the MCA file
CompoundTag blockState = mcaFile.getBlockStateAt(1090, 25, 1301);

// Retrieves block information from a single chunk
CompoundTag blockState = chunk.getBlockStateAt(2, 25, 5);

// Set block information
CompoundTag stone = new CompoundTag();
stone.putString("Name", "minecraft:stone");
mcaFile.setBlockStateAt(1090, 25, 1301, stone, false);
```
To ensure good performance even when setting a lot of blocks and / or editing sections with a huge palette of block states, the size of the BlockStates array is only updated when the size of the palette requires it. This means there might be blocks in the palette that are not actually used in the BlockStates array.
You can trigger a cleanup process by calling one of the following three methods, depending on the desired depth:
```java
mcaFile.cleanupPalettesAndBlockStates();
chunk.cleanupPalettesAndBlockStates();
section.cleanupPaletteAndBlockStates();
```

---
### Custom tags
Interested in more advanced features, and the default NBT protocol just isn't enough? Simply create your own tags!
There are 4 example classes in `net.querz.nbt.custom` that show how to implement custom tags:

| Class         | ID  | Description |
| ------------- | :-: | ----------- |
| [ObjectTag](src/main/java/net/querz/nbt/custom/ObjectTag.java)            | 90  | A wrapper tag that serializes and deserializes any object using the default java serialization. |
| [ShortArrayTag](src/main/java/net/querz/nbt/custom/ShortArrayTag.java)    | 100 | In addition to the already existing `ByteArrayTag`, `IntArrayTag` and `LongArrayTag`. |
| [CharTag](src/main/java/net/querz/nbt/custom/CharTag.java)                | 110 | `Character` (char) tag. |
| [StructTag](src/main/java/net/querz/nbt/custom/StructTag.java)            | 120 | Similar to the `ListTag`, but with the ability to store multiple types. |

To be able to use a custom tag with deserialization, a `Supplier` and the custom tag class must be registered at runtime alongside its id with `TagFactory.registerCustomTag()`. The `Supplier` can be anything that returns a new instance of this custom tag. Here is an example using the custom tags no-args constructor:
```java
TagFactory.registerCustomTag(90, ObjectTag::new, ObjectTag.class);
```

#### Nesting
As mentioned before, serialization and deserialization methods are provided with a parameter indicating the maximum processing depth of the structure. This is not guaranteed when using custom tags, it is the responsibility of the creator of that custom tag to call `Tag#decrementMaxDepth(int)` to correctly update the nesting depth.

It is also highly encouraged to document the custom tag behaviour when it does so to make users aware of the possible exceptions thrown by `Tag#decrementMaxDepth(int)`.