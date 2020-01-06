package com.tirsportif.backend.utils;

import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.exception.BadRequestErrorException;
import com.tirsportif.backend.exception.NotFoundErrorException;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * All utility methods handling old {@link org.springframework.stereotype.Repository} or {@link com.tirsportif.backend.cache.Store} classes.
 */
public final class RepositoryUtils {

    /**
     * Apply a repository get() function, throwing if Optional.empty(), returning if not.
     *
     * @param getFunction   Repository get() function
     * @param id            Id of the element to retrieve
     * @param <I>           Id type
     * @param <R>           Model instance type
     * @return Model from database
     */
    public static <I,R> R findById(Function<I, Optional<R>> getFunction, I id) {
        return getFunction.apply(id)
                .orElseThrow(() -> new BadRequestErrorException(GenericClientError.RESOURCE_NOT_FOUND, id.toString()));
    }

    /**
     * Apply a repository getAll() function, throwing if ids parameter size differs from results, returning if not.
     *
     * @param getFunction   Repository get() function
     * @param id            Id of the element to retrieve
     * @param <I>           Id type
     * @param <R>           Model instance type
     * @return Model from database
     */
    public static <I,R> Set<R> findByIds(Function<Iterable<I>, Iterable<R>> getFunction, Set<I> ids) {
        Set<R> models = IterableUtils.toSet(getFunction.apply(ids));
        if (models.size() != ids.size()) {
            throw new NotFoundErrorException(GenericClientError.RESOURCES_NOT_FOUND);
        }
        return models;
    }

}
