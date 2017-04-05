# NBT
[![Build Status](https://travis-ci.org/Querz/NBT.svg?branch=master)](https://travis-ci.org/Querz/NBT) [![](https://jitpack.io/v/Querz/NBT.svg)](https://jitpack.io/#Querz/NBT)
#### A java implementation of the [NBT protocol](http://minecraft.gamepedia.com/NBT_format), including a way to implement custom tags.
---
### Specification
According to the [specification](http://minecraft.gamepedia.com/NBT_format), there are currently 12 different types of tags:

| Tag class    | Superclass | ID | Payload |
| ---------    | ---------- | -- | ----------- |
| EndTag       | Tag        | 0  | None |
| ByteTag      | NumberTag  | 1  | 1 byte / 8 bits, signed |
| ShortTag     | NumberTag  | 2  | 2 bytes / 16 bits, signed, big endian |
| IntTag       | NumberTag  | 3  | 4 bytes / 32 bits, signed, big endian |
| LongTag      | NumberTag  | 4  | 8 bytes / 64 bits, signed, big endian |
| FloatTag     | NumberTag  | 5  | 4 bytes / 32 bits, signed, big endian, IEEE 754-2008, binary32 |
| DoubleTag    | NumberTag  | 6  | 8 bytes / 64 bits, signed, big endian, IEEE 754-2008, binary64 |
| ByteArrayTag | ArrayTag   | 7  | IntTag's payload *size*, then *size* ByteTag's payloads |
| StringTag    | Tag        | 8  | ShortTag's payload *length*, then a UTF-8 string with size *length* |
| ListTag      | Tag        | 9  | ByteTag's payload *tagId*, then IntTag's payload *size*, then *size* tags' payloads, all of type *tagId* |
| CompoundTag  | Tag        | 10 | Fully formed tags, followed by an EndTag |
| IntArrayTag  | ArrayTag   | 11 | IntTag's payload *size*, then *size* IntTag's payloads |

* The [EndTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/EndTag.java) is only used to mark the end of a [CompoundTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/CompoundTag.java) in its serialized state or an empty [ListTag](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/ListTag.java).

* The maximum depth of the NBT structure is 512. If the depth exceeds this restriction during serialization, deserialization or String conversion, a [MaxDepthReachedException](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/MaxDepthReachedException.java) is thrown. This usually happens when a circular reference exists in the NBT structure. The NBT specification does not allow circular references, as there is no tag to represent this.

---
### Example usage:
```
ByteTag bt = new ByteTag("8bitNumber", (byte) 1);
DoubleTag dt = new DoubleTag("64bitFloatingPointNumber", 1.234);

CompoundTag ct = new CompoundTag("compound");

ct.set(bt);
ct.set(dt);
```
---
### Utility
There are several utility classes that make your life easier using this library.
#### NBTFileWriter
The NBTFileWriter lets you write a Tag into a gzip compressed or uncompressed file in one line. Files are gzip compressed by default.
Example usage:
```
new NBTFileWriter("filename.dat").write(tag);
```
#### NBTFileReader
The NBTFileReader reads any file containing NBT data. No worry about compression, it will automatically uncompress gzip compressed files.
Example usage:
```
Tag tag = new NBTFileReader("filename.dat").read();
```
#### Playing Minecraft?
Each tag can be converted into a JSON-like NBT String used in Minecraft commands.
Example usage:
```
CompoundTag c = new CompoundTag("compound");
c.setByte("byte", (byte) 5);
c.setString("string", "test");
String s = c.toTagString();

//output: c:{byte:5b,string:"test"}
```
---
### Custom tags
Interested in more advanced features, and the default NBT protocol just isn't enough? Simply create your own tags!
There are 3 example classes in net.querz.nbt.custom that show how to implement custom tags:

| Class         | ID  | Description |
| ------------- | :-: | ----------- |
| CharTag       | 110 | Character (char) tag. |
| ShortArrayTag | 100 | In addition to the already existing ByteArrayTag and IntArrayTag. |
| StructTag     | 120 | Similar to the ListTag, but with the ability to store multiple types. |

To be able to use a custom tag, it must be registered during runtime with [TagType](https://github.com/Querz/NBT/blob/master/src/main/java/net/querz/nbt/TagType.java)#registerCustomTag(int, Class).
