package edu.sumdu.blogwebapp.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="comment_id")
    private Long commentId;

    @NotBlank(message = "Please fill the message")
    @Length(max = 4096, message = "Message too long (more than 4096 characters)")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "message_id")
    private Message message;


    private Long order_num;

    public Comment() {
    }

    public Comment(String text, User author, Message message, Long order_num) {
        this.text = text;
        this.author = author;
        this.message = message;
        this.order_num = order_num;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Long getOrder_num() {
        return order_num;
    }

    public void setOrder_num(Long order_num) {
        this.order_num = order_num;
    }
}
