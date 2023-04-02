package edu.sumdu.blogwebapp.service;

import edu.sumdu.blogwebapp.entity.Comment;
import edu.sumdu.blogwebapp.entity.Message;
import edu.sumdu.blogwebapp.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findByMessage(Message message) {
        return commentRepository.findByMessage(message);
    }

    public Long findMAxOrderNum(Long message_id){
        return commentRepository.findMAxOrderNum(message_id);
    };

     public Comment save(Comment comment){
       return commentRepository.save(comment);
    };
}