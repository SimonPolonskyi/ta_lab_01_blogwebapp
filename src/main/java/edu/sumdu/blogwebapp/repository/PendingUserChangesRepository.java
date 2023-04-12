package edu.sumdu.blogwebapp.repository;

import edu.sumdu.blogwebapp.entity.PendingUserChanges;
import edu.sumdu.blogwebapp.entity.PendingUserChangesId;
import edu.sumdu.blogwebapp.enums.ChangedParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import edu.sumdu.blogwebapp.entity.User;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PendingUserChangesRepository extends JpaRepository<PendingUserChanges, PendingUserChangesId> {
    PendingUserChanges findPendingUserChangesByConfirmationCode(String code);

    @Query("SELECT COUNT(p) FROM PendingUserChanges p WHERE p.id.userId = :userId AND p.id.parameter = :parameter")
    int countByUserIdAndParameter(@Param("userId") Long userId, @Param("parameter") ChangedParameters parameter);

    Optional<PendingUserChanges> findByIdUserIdAndIdParameter(Long userId, ChangedParameters parameter);

    PendingUserChanges findPendingUserChangesByUser(User user);



}