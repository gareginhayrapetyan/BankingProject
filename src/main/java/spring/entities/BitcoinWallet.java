package spring.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table
public class BitcoinWallet {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String currentAddress;

    private BigDecimal currentBalance;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "owner_id")
    private User owner;

    public BitcoinWallet() {
        super();
    }

    public BitcoinWallet(String currentAddress, BigDecimal currentBalance) {
        this.currentAddress = currentAddress;
        this.currentBalance = currentBalance;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCurrentAddress() {
        return this.currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
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
