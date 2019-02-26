package com.bitcoin_bank.backend.entities;


import org.jboss.aerogear.security.otp.api.Base32;

import javax.persistence.*;
import java.util.Set;

@Entity
public class User {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String userName;

    private String email;

    @Column(length = 60)
    private String password;

    private String currentAddress;

    private String secret;
    private boolean isUsing2FA;

    @OneToMany(mappedBy = "owner")
    private Set<Wallet> wallets;

    public User() {
        super();
        this.secret = Base32.random();
    }

    public User(String firstName, String lastName, String userName, String email, String password, boolean isUsing2FA) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.isUsing2FA = isUsing2FA;
        this.secret = Base32.random();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }


    public void setCurrentAddress(String address) {
        this.currentAddress = address;
    }

    public String getCurrentAddress() {
        return this.currentAddress;
    }

    public boolean isUsing2FA() {
        return isUsing2FA;
    }

    public void setUsing2FA(boolean isUsing2FA) {
        this.isUsing2FA = isUsing2FA;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Set<Wallet> getWallets() {
        return wallets;
    }


    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User user = (User) obj;
        if (!email.equals(user.email)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BankUser [id=").append(id).append(", firstName=").append(firstName)
                .append(", lastName=").append(lastName)
                .append(", email=").append(email)
                .append(", password=").append(password).append("]");
        return builder.toString();
    }

}
