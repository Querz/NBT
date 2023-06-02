package net.querz.mca;

import java.util.Iterator;

public interface SectionIterator<T extends SectionBase<?>> extends Iterator<T> {
    /** current section y within chunk */
    int sectionY();
    /** current block world level y of the bottom most block in the current section */
    int sectionBlockMinY();
    /** current block world level y of the top most block in the current section */
    int sectionBlockMaxY();
}
