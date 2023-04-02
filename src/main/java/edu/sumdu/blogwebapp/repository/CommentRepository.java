package edu.sumdu.blogwebapp.repository;

import edu.sumdu.blogwebapp.entity.Comment;
import edu.sumdu.blogwebapp.entity.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import edu.sumdu.blogwebapp.entity.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByMessage(Message message);

    @Query(value = "SELECT COALESCE(max(c.order_num),0) FROM comment c WHERE c.message_id = ?1", nativeQuery = true)
    Long findMAxOrderNum(@Param("msgid") Long message_id);
}
