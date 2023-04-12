package edu.sumdu.blogwebapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pending_user_changes")
public class PendingUserChanges {

 /*   @Id
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private  User user;

    @Id
    @Enumerated(EnumType.STRING)
    private ChangedParameters parameter;
*/
    @EmbeddedId
    private PendingUserChangesId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="new_value")
    private String newValue;


    private String confirmationCode;

    public PendingUserChanges() {
    }

    public PendingUserChanges(User user, PendingUserChangesId id, String newValue, String confirmationCode) {
        this.user = user;
        this.id = id;
        this.newValue = newValue;
        this.confirmationCode = confirmationCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PendingUserChangesId getId() {
        return id;
    }

    public void setId(PendingUserChangesId id) {
        this.id = id;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
}
