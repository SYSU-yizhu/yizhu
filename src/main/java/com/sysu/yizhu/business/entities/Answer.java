package com.sysu.yizhu.business.entities;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="answer")
public class Answer {
    private Integer answerId;
    private User answerUser;
    private Question question;
    private Date createDate;
    private String content;
    private Integer good;
    private Integer bad;

    public Answer() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "answer_id")
    public Integer getAnswerId() {
        return answerId;
    }
    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getAnswerUser() {
        return answerUser;
    }
    public void setAnswerUser(User answerUser) {
        this.answerUser = answerUser;
    }

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    public Question getQuestion() {
        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }

    @Column(name="create_date", nullable = false)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name="content", length = 65536, nullable = false)
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "good")
    public Integer getGood() {
        return good;
    }
    public void setGood(Integer good) {
        this.good = good;
    }

    @Column(name = "bad")
    public Integer getBad() {
        return bad;
    }
    public void setBad(Integer bad) {
        this.bad = bad;
    }
}
