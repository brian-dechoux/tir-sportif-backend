package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Bill;
import com.tirsportif.backend.model.projection.ShooterBillProjection;
import com.tirsportif.backend.model.projection.ShooterWithBillsListElementProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query(value =
            "SELECT b.id, b.value, b.paid, b.paidDate, pr.type, c.name AS challengeName, c.startDate AS challengeStartDate, l.subscriptionDate AS licenseSubscriptionDate FROM bill b " +
            "INNER JOIN price pr ON b.priceId = pr.id " +
            "LEFT JOIN participation p ON b.participationId = p.id " +
            "LEFT JOIN challenge c ON p.challengeId = c.id " +
            "LEFT JOIN licensee l ON b.licenseeId = l.id " +
            "WHERE (p.shooterId = ?1 " +
            "OR l.shooterId = ?1)"
            , nativeQuery = true)
    List<ShooterBillProjection> getBillsForShooter(Long shooterId);

    @Query(value =
            "SELECT s.id, s.lastname, s.firstname FROM shooter s " +
            "WHERE s.id IN (" +
            "    (SELECT p.shooterId " +
            "    FROM bill b " +
            "    INNER JOIN participation p ON b.participationId = p.id)" +
            "    UNION " +
            "    (SELECT l.shooterId " +
            "    FROM bill b " +
            "    INNER JOIN licensee l ON b.licenseeId = l.id) " +
            ") " +
            "ORDER BY lastname, firstname"
            , countQuery =
            "SELECT COUNT(*) FROM shooter s " +
            "WHERE s.id IN (" +
            "     (SELECT p.shooterId " +
            "     FROM bill b " +
            "     INNER JOIN participation p ON b.participationId = p.id) " +
            "     UNION " +
            "     (SELECT l.shooterId " +
            "     FROM bill b " +
            "     INNER JOIN licensee l ON b.licenseeId = l.id)" +
            ")"
            , nativeQuery = true)
    Page<ShooterWithBillsListElementProjection> findShootersWithBillsAsListElements(Pageable page);

}
