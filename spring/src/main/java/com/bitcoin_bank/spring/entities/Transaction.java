package com.bitcoin_bank.spring.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class Transaction {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Timestamp date;
    @Column(nullable = false)
    private BigDecimal transactionAmount;
    private String transactionDescription;

    @ManyToOne
    private Wallet senderWallet;

    @ManyToOne
    private Wallet receiverWallet;

    public Transaction() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public Wallet getSenderWallet() {
        return senderWallet;
    }

    public void setSenderWallet(Wallet senderWallet) {
        this.senderWallet = senderWallet;
    }

    public Wallet getReceiverWallet() {
        return receiverWallet;
    }

    public void setReceiverWallet(Wallet receiverWallet) {
        this.receiverWallet = receiverWallet;
    }
}
