package net.net.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements Serializable {


    @Column(name = "name")
    private String name;

    @Column(name = "role")
    private String role;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "passWord")
    private String passWord;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String mailAddress;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "status")
    private long status;

    @Column(name = "token")
    private String token;

    @Column(name = "is_confirmed")
    private int confirmed;

    public User() {
    }

//    public User(String fName, String lName, String role, String phoneNumber, String passWord, String userName, String mailAddress) {
//        this.fName = fName;
//        this.lName = lName;
//        this.role = role;
//        this.phoneNumber = phoneNumber;
//        this.passWord = passWord;
//        this.userName = userName;
//        this.mailAddress = mailAddress;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }
}
