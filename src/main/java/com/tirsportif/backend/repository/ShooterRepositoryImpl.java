package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.projection.SearchShooterProjection;
import com.tirsportif.backend.model.projection.SearchShooterProjectionImpl;
import org.springframework.lang.Nullable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.List;
import java.util.stream.Collectors;

public class ShooterRepositoryImpl implements ShooterRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<SearchShooterProjection> search(String searchName, @Nullable Long clubId, @Nullable List<Long> categoryIds) {
        List<Tuple> results = entityManager.createNativeQuery(
                "SELECT s.id, s.lastname, s.firstname, c.name AS clubName, cat.label AS categoryLabel "+
                        "FROM shooter s "+
                        "LEFT JOIN club c ON s.clubId = c.id "+
                        "INNER JOIN category cat ON s.categoryId = cat.id "+
                        "WHERE CONCAT(s.firstname, ' ', s.lastname) LIKE CONCAT('%',?1,'%') " +
                        "OR CONCAT(s.lastname, ' ', s.firstname) LIKE CONCAT('%',?1,'%')"
                , Tuple.class
        ).setParameter(1, searchName).getResultList();
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
