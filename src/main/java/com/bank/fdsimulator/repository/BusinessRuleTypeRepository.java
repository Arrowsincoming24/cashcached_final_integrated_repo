package com.bank.fdsimulator.repository;

import com.bank.fdsimulator.entity.BusinessRuleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BusinessRuleTypeRepository extends JpaRepository<BusinessRuleType, Long> {
    Optional<BusinessRuleType> findByCode(String code);
}
