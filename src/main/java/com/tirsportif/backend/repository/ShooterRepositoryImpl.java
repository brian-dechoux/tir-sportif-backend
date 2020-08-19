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
    public List<SearchShooterProjection> search(String searchName, @Nullable Long clubId, @Nullable List<Long> categoryIds) {
        Query query = entityManager.createNativeQuery(
                "SELECT s.id, s.lastname, s.firstname, c.name AS clubName, cat.label AS categoryLabel "+
                        "FROM shooter s "+
                        ((clubId != null) ? "INNER JOIN club c ON s.clubId = c.id " : "LEFT JOIN club c ON s.clubId = c.id ") +
                        "INNER JOIN category cat ON s.categoryId = cat.id "+
                        "WHERE (CONCAT(s.firstname, ' ', s.lastname) LIKE CONCAT('%',?1,'%') " +
                        "OR CONCAT(s.lastname, ' ', s.firstname) LIKE CONCAT('%',?1,'%')) " +
                        ((clubId != null) ? "AND c.id = ?2 " : "") +
                        ((categoryIds != null) ? "AND cat.id IN (?3) " : "")
                , Tuple.class
        ).setParameter(1, searchName);

        if (clubId != null) {
            query = query.setParameter(2, clubId);
        }
        if (categoryIds != null) {
            query = query.setParameter(3, categoryIds);
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
