package com.tirsportif.backend.utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * All utility methods handling {@link Iterable} class.
 */
public final class IterableUtils {

    /**
     * get a {@link Set} from an Iterable.
     *
     * @param iterables
     * @param <T>
     * @return {@link Set}
     */
    public static <T> Set<T> toSet(Iterable<T> iterables) {
        return StreamSupport.stream(iterables.spliterator(), false)
                .collect(Collectors.toSet());
    }

    /**
     * get a {@link List} from an Iterable.
     *
     * @param iterables
     * @param <T>
     * @return {@link List}
     */
    public static <T> List<T> toList(Iterable<T> iterables) {
        return StreamSupport.stream(iterables.spliterator(), false)
                .collect(Collectors.toList());
    }

}
