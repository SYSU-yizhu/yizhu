package com.sysu.yizhu.business.entities;

import javax.persistence.*;

@Entity
@Table(name="sos_response")
public class SOSResponse {
    private Integer sosResponseId;
    private SOS sos;
    private User sosResponseUser;

    public SOSResponse() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sos_response_id")
    public Integer getSosResponseId() {
        return sosResponseId;
    }
    public void setSosResponseId(Integer sosResponseId) {
        this.sosResponseId = sosResponseId;
    }

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "sos_id")
    public SOS getSos() {
        return sos;
    }
    public void setSos(SOS sos) {
        this.sos = sos;
    }

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getSosResponseUser() {
        return sosResponseUser;
    }
    public void setSosResponseUser(User sosResponseUser) {
        this.sosResponseUser = sosResponseUser;
    }
}
