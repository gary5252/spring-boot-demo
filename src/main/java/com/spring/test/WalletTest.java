package com.spring.test;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class WalletTest {

    @Id
    @GeneratedValue
    private int id;
    private int balance;
    @OneToOne(mappedBy = "wallet")
    private UserTest user;

    public WalletTest() {
    }

    public WalletTest(int balance) {
        this.balance = balance;
    }

    /**
     * @return int return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return int return the balance
     */
    public int getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * @return UserTest return the user
     */
    public UserTest getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserTest user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "WalletTest [id=" + id + ", balance=" + balance + ", user=" + user + "]";
    }

}
