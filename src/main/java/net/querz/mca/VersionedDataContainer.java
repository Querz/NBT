package net.querz.mca;

/**
 * Interface for any NBT data container which has the "DataVersion" tag.
 */
public interface VersionedDataContainer {

    /**
     * @return The exact data version of this container.
     */
    int getDataVersion();

    /**
     * Sets the data version value for this container. This does not check if the data of this container
     * conforms to that of the data version specified, that is the responsibility of the developer.
     * @param dataVersion The numeric data version to be set.
     */
    void setDataVersion(int dataVersion);

    /**
     * Indicates if the held data version has been set.
     */
    default boolean hasDataVersion() {
        return getDataVersionEnum() != DataVersion.UNKNOWN;
    }

    /**
     * Equivalent to calling {@link DataVersion#bestFor(int)} with {@link #getDataVersion()}.
     * @return The best matching {@link DataVersion} of this chunk.
     */
    default DataVersion getDataVersionEnum() {
        return DataVersion.bestFor(getDataVersion());
    }

    /**
     * Equivalent to calling {@link #setDataVersion(int)} with {@link DataVersion#id()}.
     * @param dataVersion The {@link DataVersion} to set.
     */
    default void setDataVersion(DataVersion dataVersion) {
        setDataVersion(dataVersion.id());
    }
}
