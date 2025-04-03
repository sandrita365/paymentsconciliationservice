package com.payments.paymentsconciliationservice.infrastructure.persistencie.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "CDINFILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForexEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 1)
    private String indicator;
    @Column(length = 17)
    private String transactionReference;
    @Column(length = 35)
    private String accountNumber;
    @Column(length = 16)
    private String bankReference;
    @Column(length = 8)
    private String valueDate;
    @Column(length = 3)
    private String currency;
    @Column(length = 31)
    private String transactionAmount;

}
