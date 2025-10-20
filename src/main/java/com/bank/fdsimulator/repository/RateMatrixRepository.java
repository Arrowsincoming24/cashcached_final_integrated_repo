package com.bank.fdsimulator.repository;

import com.bank.fdsimulator.entity.RateMatrix;
import com.bank.fdsimulator.entity.RateMatrix.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RateMatrixRepository extends JpaRepository<RateMatrix, Long> {
    List<RateMatrix> findByProductId(Long productId);
    List<RateMatrix> findByProductIdAndCustomerType(Long productId, CustomerType customerType);
}
