package edu.sumdu.blogwebapp.entity;


import edu.sumdu.blogwebapp.enums.ChangedParameters;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PendingUserChangesId implements Serializable {
    private Long userId;
    @Enumerated(EnumType.STRING)
    private ChangedParameters parameter;

    public PendingUserChangesId() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ChangedParameters getParameter() {
        return parameter;
    }

    public void setParameter(ChangedParameters parameter) {
        this.parameter = parameter;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PendingUserChangesId that = (PendingUserChangesId) o;
        return Objects.equals(userId, that.userId) && parameter == that.parameter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, parameter);
    }
}