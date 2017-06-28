package com.sysu.yizhu.business.entities;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name="help")
public class Help {
    private Integer helpId;
    private User pushUser;
    private Boolean finished;
    private Time createTime;
    private Double latitude;
    private Double longitude;
    private String title;
    private String detail;
    private Integer needs;
    private Integer responseNum;

    public Help() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "help_id")
    public Integer getHelpId() {
        return helpId;
    }
    public void setHelpId(Integer helpId) {
        this.helpId = helpId;
    }

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getPushUser() {
        return pushUser;
    }
    public void setPushUser(User pushUser) {
        this.pushUser = pushUser;
    }

    @Column(name = "finished", nullable = false)
    public Boolean getFinished() {
        return finished;
    }
    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    @Column(name="create_time", nullable = false)
    public Time getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Time createTime) {
        this.createTime = createTime;
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

    @Column(name="title", length = 128, nullable = false)
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name="detail", length = 65536, nullable = false)
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Column(name = "needs", nullable = false)
    public Integer getNeeds() {
        return needs;
    }
    public void setNeeds(Integer needs) {
        this.needs = needs;
    }

    @Column(name = "response_num", nullable = false)
    public Integer getResponseNum() {
        return responseNum;
    }
    public void setResponseNum(Integer responseNum) {
        this.responseNum = responseNum;
    }
}
