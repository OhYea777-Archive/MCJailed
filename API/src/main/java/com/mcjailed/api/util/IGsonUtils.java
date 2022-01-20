package com.mcjailed.api.util;

import java.io.File;

public interface IGsonUtils {

    /**
     * Returns a new instance of a class, from a file, given a type.
     *
     * @param file The file for the type to be created from.
     * @param type The class for the type to created from.
     * @param <T> The type to be created.
     */
    <T> T createFromFile(File file, Class<T> type);

    /**
     * Returns an instance of a class, from a file, given a type as it is saved.
     *
     * @param file The file for the instance to be saved to.
     * @param toSave The instance to be saved.
     * @param <T> The type to be saved.
     */
    <T> T saveToFile(File file, T toSave);

}
