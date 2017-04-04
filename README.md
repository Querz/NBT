# NBT [![Build Status](https://travis-ci.org/Querz/NBT.svg?branch=master)](https://travis-ci.org/Querz/NBT) [![](https://jitpack.io/v/Querz/NBT.svg)](https://jitpack.io/#Querz/NBT)
#### A java implementation of the NBT protocol, including a way to implement custom tags.
---
### Creating Tags
Tags can be used by creating instances of the corresponding tag classes:

| Tag class    | Superclass | ID |
| ---------    | ---------- | -: |
| ByteTag      | NumberTag  | 1  |
| ShortTag     | NumberTag  | 2  |
| IntTag       | NumberTag  | 3  |
| LongTag      | NumberTag  |
| FloatTag     | NumberTag  |
| DoubleTag    | NumberTag  |
| ByteArrayTag | ArrayTag   |
| IntArrayTag  | ArrayTag   |
| StringTag    | Tag        |
| ListTag      | Tag        |
| CompoundTag  | Tag        |

```
ByteTag bt = new ByteTag("8bitRetroNumber", (byte) 1);
DoubleTag dt = new DoubleTag("64bitFloatingPointNumber", 1.234);

CompoundTag ct = new CompoundTag("compound");

ct.set(bt);
ct.set(dt);
```
