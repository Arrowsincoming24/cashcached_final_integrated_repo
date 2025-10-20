package com.bank.fdsimulator.repository;

import com.bank.fdsimulator.entity.ProductAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductAuditLogRepository extends JpaRepository<ProductAuditLog, Long> {
    List<ProductAuditLog> findByProductIdOrderByCreatedAtDesc(Long productId);
}
