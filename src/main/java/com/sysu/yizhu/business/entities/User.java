package com.sysu.yizhu.business.entities;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.sql.Date;


@Entity
@Table(name="user")
public class User {
    private String userId;
    private String password;
    private String headImgUrl;
    private String name;
    private Date birthDate;
    private String gender;
    private String location;

    public User() {
        super();
    }

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    @Id
    @Column(name="user_id", length = 11, nullable = false)
    @Pattern(regexp = "[0-9]{11}")
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name="password", length = 32, nullable = false)
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name="head_img_url", length = 128)
    public String getHeadImgUrl() {
        return headImgUrl;
    }
    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    @Column(name="name", length = 32)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name="birth_date", nullable = false)
    public Date getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Column(name="gender", length = 8, nullable = false)
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    @Column(name="location", length = 128)
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
}
