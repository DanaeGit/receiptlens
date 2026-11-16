package com.receiptlens.receipt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {

    List<Receipt> findByUserIdOrderByPurchasedAtDesc(UUID userId);
    boolean existsByUserIdAndFileHash(UUID userId, String fileHash);
}
