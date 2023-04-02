package edu.sumdu.blogwebapp.repository;

import edu.sumdu.blogwebapp.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByTag(String tag);

    Message findByMessageId(Long messageId);

    @Query(value = "SELECT count(*) FROM comment c WHERE c.message_id = ?1", nativeQuery = true)
    Integer findMAxOrderNum(@Param("msgid") Long message_id);

}