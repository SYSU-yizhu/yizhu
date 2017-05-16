package com.sysu.yizhu.business.entities;

import javax.persistence.*;

@Entity
@Table(name="answer_agree",
        uniqueConstraints = {@UniqueConstraint(columnNames={"user_id", "answer_id"})})
public class AnswerAgree {
    private Integer answerAgreeId;
    private User user;
    private Answer answer;
    private Boolean type; // 1:赞同 0:不赞同

    public AnswerAgree() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "answer_agree_id")
    public Integer getAnswerAgreeId() {
        return answerAgreeId;
    }
    public void setAnswerAgreeId(Integer answerAgreeId) {
        this.answerAgreeId = answerAgreeId;
    }

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    public Answer getAnswer() {
        return answer;
    }
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    @Column(name = "type", nullable = false)
    public Boolean getType() {
        return type;
    }
    public void setType(Boolean type) {
        this.type = type;
    }
}
