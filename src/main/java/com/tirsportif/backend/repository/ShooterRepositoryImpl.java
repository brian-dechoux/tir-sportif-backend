package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.projection.SearchShooterProjection;
import com.tirsportif.backend.model.projection.SearchShooterProjectionImpl;
import org.springframework.lang.Nullable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.util.List;
import java.util.stream.Collectors;

public class ShooterRepositoryImpl implements ShooterRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<SearchShooterProjection> search(String searchName, @Nullable boolean freeClubOnly, @Nullable List<Long> categoryIds) {
        Query query = entityManager.createNativeQuery(
                "SELECT s.id, s.lastname, s.firstname, c.name AS clubName, cat.label AS categoryLabel "+
                        "FROM shooter s "+
                        "LEFT JOIN club c ON s.clubId = c.id " +
                        "INNER JOIN category cat ON s.categoryId = cat.id "+
                        "WHERE (LOWER(CONCAT(s.firstname, ' ', s.lastname)) LIKE CONCAT('%',LOWER(?1),'%') " +
                        "OR LOWER(CONCAT(s.lastname, ' ', s.firstname)) LIKE CONCAT('%',LOWER(?1),'%')) " +
                        ((freeClubOnly) ? "AND s.clubId IS NULL " : "") +
                        ((categoryIds != null) ? "AND s.categoryId IN (?2) " : "")
                , Tuple.class
        ).setParameter(1, searchName);

        if (categoryIds != null) {
            query = query.setParameter(2, categoryIds);
        }

        List<Tuple> results = query.getResultList();
        return results.stream()
                .map(tuple -> new SearchShooterProjectionImpl(
                        ((Integer) tuple.get("id")).longValue(),
                        (String) tuple.get("lastname"),
                        (String) tuple.get("firstname"),
                        (String) tuple.get("clubName"),
                        (String) tuple.get("categoryLabel")
                ))
                .collect(Collectors.toList());
    }

}
