package com.sysu.yizhu.business.entities;

import javax.persistence.*;

@Entity
@Table(name="response")
public class Response {
    private Integer responseId;
    private SOS sos;
    private User responseUser;

    public Response() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "response_id")
    public Integer getResponseId() {
        return responseId;
    }
    public void setResponseId(Integer responseId) {
        this.responseId = responseId;
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
    public User getResponseUser() {
        return responseUser;
    }
    public void setResponseUser(User responseUser) {
        this.responseUser = responseUser;
    }
}
