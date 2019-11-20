package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Price;
import com.tirsportif.backend.model.PriceType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PriceRepository extends CrudRepository<Price, Long> {

    Optional<Price> findByTypeAndForLicenseeOnly(PriceType priceType, boolean licenseeOnly);


}
