package com.payments.paymentsconciliationservice.infrastructure.persistencie;

import com.payments.paymentsconciliationservice.infrastructure.persistencie.entity.ForexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForexJpaRepository extends JpaRepository<ForexEntity, Long> {
}

