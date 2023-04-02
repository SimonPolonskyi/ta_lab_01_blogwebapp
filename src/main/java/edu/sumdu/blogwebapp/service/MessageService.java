package edu.sumdu.blogwebapp.service;

import edu.sumdu.blogwebapp.entity.Message;
import edu.sumdu.blogwebapp.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessagesWithCommentCount() {
        return messageRepository.findAll();
    }
    public Message findByMessageId(Long messageId){
        return messageRepository.findByMessageId(messageId);
    };

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> findByTag(String tag){
        return messageRepository.findByTag(tag);
    };
}