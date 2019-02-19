package com.bitcoin_bank.spring.entities;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Wallet {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String address;

    private BigDecimal currentBalance;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "owner_id")
    private User owner;

    public Wallet() {
        super();
    }

    public Wallet(String currentAddress, BigDecimal currentBalance) {
        this.address = currentAddress;
        this.currentBalance = currentBalance;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCurrentAddress() {
        return this.address;
    }

    public void setCurrentAddress(String currentAddress) {
        this.address = currentAddress;
    }

    public BigDecimal getCurrentBalance() {
        return this.currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
