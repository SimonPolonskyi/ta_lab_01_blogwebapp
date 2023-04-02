package edu.sumdu.blogwebapp.entity;


import jakarta.persistence.*;
import jakarta.persistence.Id;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;


@Entity
public class Message {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="message_id")
    private Long messageId;

    @NotBlank(message = "Please fill the message")
    @Length(max = 4096, message = "Message too long (more than 4096 characters)")
    private String text;
    @Length(max = 255, message = "The list of tag is too long (more than 255)")
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String filename;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    public Message() {
    }

    public Message(String text, String tag, User user) {
        this.author = user;
        this.text = text;
        this.tag = tag;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<none>";
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
