package com.spring.test;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class UserTest {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String phone;
    @Temporal(TemporalType.DATE)
    private Date createDate;

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, optional = false)
    // @JoinColumn(name = "自定義 wallet 在 DB 的名稱",referencedColumnName = "要關聯
    // WalletTest 的哪個屬性欄位名稱");
    private WalletTest wallet;

    public UserTest() {
    }

    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return Date return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return WalletTest return the wallet
     */
    public WalletTest getWallet() {
        return wallet;
    }

    /**
     * @param wallet the wallet to set
     */
    public void setWallet(WalletTest wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "UserTest [id=" + id + ", name=" + name + ", phone=" + phone + ", createDate=" + createDate + ", wallet="
                + wallet.toString() + "]";
    }

}
