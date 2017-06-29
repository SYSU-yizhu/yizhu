package com.sysu.yizhu.business.entities;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="question")
public class Question {
    private Integer questionId;
    private User askUser;
    private String title;
    private Date createDate;
    private String content;

    public Question() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "question_id")
    public Integer getQuestionId() {
        return questionId;
    }
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getAskUser() {
        return askUser;
    }
    public void setAskUser(User askUser) {
        this.askUser = askUser;
    }

    @Column(name="title", length = 128, nullable = false)
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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
}
