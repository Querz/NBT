package net.querz.nbt;

import java.util.Objects;

public class NamedTag {
    private final String name;
    private final Tag tag;

    public NamedTag(String name, Tag tag) {
        this.name = name;
        this.tag = tag;
    }

    public String name() {
        return name;
    }

    public Tag tag() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedTag namedTag = (NamedTag) o;
        return Objects.equals(name, namedTag.name) && Objects.equals(tag, namedTag.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tag);
    }

}
