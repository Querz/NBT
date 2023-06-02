package net.querz.mca;

import java.util.Iterator;

/**
 * Enhanced iterator for iterating over {@link ChunkBase} data.
 * All 1024 chunks will be returned by successive calls to {@link #next()}, even
 * those which are {@code null}.
 * See {@link MCAFileBase#iterator()}
 */
public interface ChunkIterator<I extends ChunkBase> extends Iterator<I> {
    /**
     * Replaces the current chunk with the one given by calling {@link MCAFileBase#setChunk(int, ChunkBase)}
     * with the {@link #currentIndex()}. Take care as the given chunk is NOT copied by this call.
     * @param chunk Chunk to set, may be null.
     */
    void set(I chunk);

    /**
     * @return Current chunk index (in range 0-1023)
     */
    int currentIndex();

    /**
     * @return Current chunk x within region (in range 0-31)
     */
    int currentX();

    /**
     * @return Current chunk z within region (in range 0-31)
     */
    int currentZ();
}
