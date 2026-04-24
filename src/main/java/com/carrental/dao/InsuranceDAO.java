package com.carrental.dao;

import com.carrental.model.InsuranceType;
import com.carrental.model.InsurancePrice;
import java.util.List;
import java.util.Optional;

public interface InsuranceDAO {
    List<InsuranceType> findAllTypes();
    Optional<InsuranceType> findTypeById(int typeId);
    Optional<InsurancePrice> findPriceByTypeId(int typeId);
}
