package com.bank.fdsimulator.repository;

import com.bank.fdsimulator.entity.ProductTermProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductTermProfileRepository extends JpaRepository<ProductTermProfile, Long> {
    List<ProductTermProfile> findByProductId(Long productId);
}
