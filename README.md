# NBT
[![Build Status](https://travis-ci.org/Querz/NBT.svg?branch=master)](https://travis-ci.org/Querz/NBT) [![](https://jitpack.io/v/Querz/NBT.svg)](https://jitpack.io/#Querz/NBT)
#### A java implementation of the [NBT protocol](http://minecraft.gamepedia.com/NBT_format), including a way to implement custom tags.
---
### Specification
According to the [specification](http://minecraft.gamepedia.com/NBT_format), there are currently 13 different types of tags:

| Tag class    | Superclass | ID | Payload |
| ---------    | ---------- | -- | ----------- |
| [EndTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/EndTag.java)       | [Tag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/Tag.java)        | 0  | None |
| [ByteTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/ByteTag.java)      | [NumberTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/NumberTag.java)  | 1  | 1 byte / 8 bits, signed |
| [ShortTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/ShortTag.java)     | [NumberTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/NumberTag.java)  | 2  | 2 bytes / 16 bits, signed, big endian |
| [IntTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/IntTag.java)       | [NumberTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/NumberTag.java)  | 3  | 4 bytes / 32 bits, signed, big endian |
| [LongTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/LongTag.java)      | [NumberTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/NumberTag.java)  | 4  | 8 bytes / 64 bits, signed, big endian |
| [FloatTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/FloatTag.java)     | [NumberTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/NumberTag.java)  | 5  | 4 bytes / 32 bits, signed, big endian, IEEE 754-2008, binary32 |
| [DoubleTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/DoubleTag.java)    | [NumberTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/NumberTag.java)  | 6  | 8 bytes / 64 bits, signed, big endian, IEEE 754-2008, binary64 |
| [ByteArrayTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/ByteArrayTag.java) | [ArrayTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/ArrayTag.java)   | 7  | `IntTag`'s payload *size*, then *size* `ByteTag`'s payloads |
| [StringTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/StringTag.java)    | [Tag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/Tag.java)        | 8  | `ShortTag`'s payload *length*, then a UTF-8 string with size *length* |
| [ListTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/ListTag.java)      | [Tag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/Tag.java)        | 9  | `ByteTag`'s payload *tagId*, then `IntTag`'s payload *size*, then *size* tags' payloads, all of type *tagId* |
| [CompoundTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/CompoundTag.java)  | [Tag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/Tag.java)        | 10 | Fully formed tags, followed by an `EndTag` |
| [IntArrayTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/IntArrayTag.java)  | [ArrayTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/ArrayTag.java)   | 11 | `IntTag`'s payload *size*, then *size* `IntTag`'s payloads |
| [LongArrayTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/LongArrayTag.java) | [ArrayTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/ArrayTag.java)   | 12 | `IntTag`'s payload *size*, then *size* `LongTag`'s payloads |

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
An example on how to use `ListTag`:
```java
ListTag<FloatTag> fl = new ListTag<>();

fl.add(new FloatTag(1.234f);
fl.addFloat(5.678f);
```
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
Tag tag = NBTUtil.readTag("filename.dat");
```
#### Playing Minecraft?
Each tag can be converted into a JSON-like NBT String used in Minecraft commands.
Example usage:
```java
CompoundTag c = new CompoundTag();
c.putByte("blah", (byte) 5);
c.putString("foo", "bär");
String s = c.toTagString();

//output: {blah:5b,foo:"bär"}
```
There are also some tools to read, change and write MCA files:
For example:
```java
//This changes the InhabitedTime field of the chunk at x=68, z=81 to 0

MCAFile mcaFile = MCAUtil.readMCAFile("r.2.2.mca");
CompoundTag tag = mcaFile.getChunkData(68, 81);
tag.getCompoundTag("Level").setLong("InhabitedTime", 0L);
MCAUtil.writeMCAFile("r.2.2.mca", mcaFile);
```

---
### Custom tags
Interested in more advanced features, and the default NBT protocol just isn't enough? Simply create your own tags!
There are 4 example classes in `net.querz.nbt.custom` that show how to implement custom tags:

| Class         | ID  | Description |
| ------------- | :-: | ----------- |
| [ObjectTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/custom/ObjectTag.java)     | 90  | A wrapper tag that serializes and deserializes any object using the default java serialization. |
| [ShortArrayTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/custom/ShortArrayTag.java) | 100 | In addition to the already existing `ByteArrayTag`, `IntArrayTag` and `LongArrayTag`. |
| [CharTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/custom/CharTag.java)       | 110 | `Character` (char) tag. |
| [StructTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/custom/StructTag.java)     | 120 | Similar to the `ListTag`, but with the ability to store multiple types. |

To be able to use a custom tag with deserialization, it needs to have a public no-args constructor and its id and class must be registered during runtime with `TagFactory.registerCustomTag()`.
