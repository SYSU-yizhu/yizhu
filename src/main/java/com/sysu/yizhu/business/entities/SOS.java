package com.sysu.yizhu.business.entities;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name="sos")
public class SOS {
    private Integer sosId;
    private User pushUser;
    private Boolean finished;
    private Time createTime;
    private Double latitude;
    private Double longitude;

    public SOS() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sos_id")
    public Integer getSosId() {
        return sosId;
    }
    public void setSosId(Integer sosId) {
        this.sosId = sosId;
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
}
