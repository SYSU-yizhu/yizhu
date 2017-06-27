package com.sysu.yizhu.business.entities;

import javax.persistence.*;

@Entity
@Table(name="help_response")
public class HelpResponse {
    private Integer helpResponseId;
    private Help help;
    private User helpResponseUser;

    public HelpResponse() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "help_response_id")
    public Integer getHelpResponseId() {
        return helpResponseId;
    }
    public void setHelpResponseId(Integer helpResponseId) {
        this.helpResponseId = helpResponseId;
    }

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "help_id")
    public Help getHelp() {
        return help;
    }
    public void setHelp(Help help) {
        this.help = help;
    }

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getHelpResponseUser() {
        return helpResponseUser;
    }
    public void setHelpResponseUser(User helpResponseUser) {
        this.helpResponseUser = helpResponseUser;
    }
}
