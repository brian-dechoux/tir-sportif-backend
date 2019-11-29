package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetBillResponse;
import com.tirsportif.backend.model.Bill;
import org.springframework.stereotype.Component;

@Component
public class BillMapper {

    public GetBillResponse mapCategoryToResponse(Bill bill) {
        return new GetBillResponse(
                bill.getValue(),
                bill.isPaid()
        );
    }

}
