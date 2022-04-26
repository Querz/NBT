# NBT

#### A Java implementation of the [NBT protocol](http://minecraft.gamepedia.com/NBT_format) for Minecraft Java Edition.
---

## Description

This NBT library focuses on speed and memory efficiency and therefore uses heavy caching of the primitive types.
It also contains a package to parse and manipulate MCA files with the ability to create custom implementations to
parse individual aspects of their chunk format, or how to handle MCC files.

LittleEndian streams are also provided for the ability to read and write NBT data for Minecraft Bedrock.

## Usage

### Creating Tags

Tags can be created by using their `valueOf` static methods for primitive types and Strings or their constructor:

```java
IntTag it = IntTag.valueOf(5);
StringTag st = StringTag.valueOf("hello there");
ByteArrayTag bat = new ByteArrayTag(new byte[]{1, 2, 3, 4, 5});
CompoundTag tag = new CompoundTag();

// tags can then be added to container tags
tag.put("fingers", it);
tag.put("general_kenobi", st);
tag.put("one_to_five", bat);

// or use the explicit helper functions
tag.putFloat("levitation", 123.456f);

// let's make a list!
ListTag lt = new ListTag();
lt.add(new StringTag("cough"));
// now we can only add strings to the list
lt.addString("4 arms");

tag.put("so_uncivilized", lt);
```

### Converting tags to SNBT

To convert a tag to SNBT use the helper functions provided in `NBTUtil`:
```java
String snbt = NBTUtil.toSNBT(tag, "\t");
```
Alternatively the `SNBTWriter` provides building methods to convert to String or write directly to a File or an `OutputStream`.

### Parsing SNBT

To parse an SNBT string to a `Tag` object, use the helper functions provided in `NBTUtil`:
```java
Tag tag = NBTUtil.fromSNBT(snbt);
```
Alternatively use the `SNBTReader` to read directly from a file or `InputStream`.

### Writing tags to stream

To write tags to file or stream, use the helper functions provided in `NBTUtil`:
```java
NBTUtil.write(new File("level.dat"), tag);
```
Alternatively the streams can be assembled using the `NBTWriter` builder class.
Or everything can be done manually by calling the `Tag#write()` method on the `Tag` directly.

### Reading tags from stream

To read tags from file or stream, use the helper functions provided in `NBTUtil`:
```java
Tag tag = NBTUtil.read(new File("level.dat"));
```
Additional helper functions allow selectively loading NBT tags by specifying their paths and their type:
```java
Tag tag = NBTUtil.read(file, new TagSelector("Data", "WorldGenSettings", "dimensions", CompoundTag.TYPE));
```
Alternatively the streams can be assembled using the `NBTReader` builder class.
Or everything can be done manually by calling the TagType implementations `TagType#read()` method.
To have even more control over what is loaded and parsed, a custom implementation of `TagTypeVisitor` can be passed to `NBTUtil.parseStream()`.

### Reading MCA files

MCA files can be easily read like this:
```java
File file = new File("r.0.0.mca");
MCAFile mcaFile = new MCAFile(file);
mcaFile.load();
```
Individual chunks can be accessed either by using the `MCAFile#getChunkAt()` methods or by iterating over the `MCAFile` object.

### Parse data from chunks and sections

The `net.querz.mca.parsers` package provides some predefined data parsers for various kinds of data in a chunk. Some useful
parsers are for example the `BlockParser` or the `HeightmapParser`.
Here is an example for how to iterate over all block states in a chunk:
```java
for (CompoundTag section : chunk.getSectionParser()) {
    for (CompoundTag blockState : ParserHandler.getBlockParser(chunk.getDataVersion(), section)) {
		    // do something with the block state
    }
}
```
For automation, custom parsers can ge registered to DataVersions in the `ParserHandler` class. When calling the parsers getter
functions, it will then automatically create a new instance of that parser for the desired DataVersion.

### Handle MCC files

The `MCAFile#load()` method allows explicit handling of MCC files (MCC files are files containing a single chunk if the
size of the compressed chunk data exceeds a specific number):
```java
// replace MCCFileHandler.DEFAULT_HANDLER with the custom implementation of MCCFileHandler
MCAFileHandle handle = new MCAFileHandle(file.getParentFile(), new SeekableFile(file, "r"), MCCFileHandler.DEFAULT_HANDLER);
MCAFile mcaFile = new MCAFile(file);
mcaFile.load(handle);
```

## Add the library as a dependency using Gradle:

Add Jitpack to your `repositories`:
```
repositories {
	...
	maven { url 'https://jitpack.io/' }
}
```
And then add it as a dependency as usual:
```
dependencies {
	...
	implementation 'com.github.Querz:NBT:7.0'
}
```

## Add the library as a dependency using Maven:
Add Jitpack:
```
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```
Dependency:
```
<dependency>
	<groupId>com.github.Querz</groupId>
	<artifactId>NBT</artifactId>
	<version>7.0</version>
</dependency>
```