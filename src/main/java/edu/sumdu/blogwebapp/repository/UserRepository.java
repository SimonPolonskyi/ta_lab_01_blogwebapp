package edu.sumdu.blogwebapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.sumdu.blogwebapp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByActivationCode(String code);
}
