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
    private Double latitude;
    private Double longitude;
    private String objectId;

    public User() {
        super();
    }

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    @Id
    @Column(name="user_id", length = 11, nullable = false)
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

    @Column(name = "latitude")
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Column(name = "longitude")
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Column(name="object_id", length = 32)
    public String getObjectId() {
        return objectId;
    }
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
