package com.sysu.yizhu.business.entities;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

/**
 * Created by CrazeWong on 2017/4/6.
 */
@Entity
@Table(name="user")
public class User {
    private String userId;
    private String password;

    public User() {
    }

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    @Id
    @Column(name="user_id", length = 11)
    @Pattern(regexp = "[0-9]{11}")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name="password", length = 32)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return getUserId().equals(user.getUserId());
    }

    @Override
    public int hashCode() {
        return getUserId().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User [");
        sb.append("userId=").append(userId);
        sb.append(']');
        return sb.toString();
    }
}
