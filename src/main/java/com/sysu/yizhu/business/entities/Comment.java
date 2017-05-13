package com.sysu.yizhu.business.entities;

import javax.persistence.*;

@Entity
@Table(name="comment")
public class Comment {
    private Integer commentId;
    private User toUser;
    private User fromUser;
    private String content;
    private Integer point;

    public Comment() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    public Integer getCommentId() {
        return commentId;
    }
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", referencedColumnName = "user_id")
    public User getToUser() {
        return toUser;
    }
    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id", referencedColumnName = "user_id")
    public User getFromUser() {
        return fromUser;
    }
    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    @Column(name="content", length = 256, nullable = false)
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    @Column(name="point")
    public Integer getPoint() {
        return point;
    }
    public void setPoint(Integer point) {
        this.point = point;
    }
}
