package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Bill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BillRepository extends CrudRepository<Bill, Long> {

    @Query(value = "SELECT * FROM bill " +
            "INNER JOIN participation ON bill.participationId = participation.id " +
            "WHERE participation.challengeId = ?1"
            , nativeQuery = true)
    List<Bill> getBillsForChallenge(Long challengeId);

    @Query(value = "SELECT * FROM bill " +
            "INNER JOIN participation ON bill.participationId = participation.id " +
            "WHERE participation.shooterId = ?1"
            , nativeQuery = true)
    List<Bill> getBillsForShooter(Long shooterId);

}
