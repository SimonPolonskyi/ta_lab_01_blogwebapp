package edu.sumdu.blogwebapp.controller;

import edu.sumdu.blogwebapp.entity.Comment;
import edu.sumdu.blogwebapp.entity.Message;
import edu.sumdu.blogwebapp.entity.User;
import edu.sumdu.blogwebapp.repository.CommentRepository;
import edu.sumdu.blogwebapp.repository.MessageRepository;
import edu.sumdu.blogwebapp.service.CommentService;
import edu.sumdu.blogwebapp.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Controller
public class MessageController {


    @Autowired
    private MessageService messageService;

    @Autowired
    private CommentService commentService;

    @Value("${upload.path}")
    private String uploadPath;
/*
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    */

    @GetMapping("/message/{code}")
    public String message(Model model, @PathVariable Long code) {

        Message message = messageService.findByMessageId(code);
        if (message !=null){
            List<Comment>  comments = commentService.findByMessage(message);
            model.addAttribute("comments", comments);
        }
        model.addAttribute("message", message);
        return "message";
    }


    @PostMapping("/message/{code}")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Comment comment,
            BindingResult bindingResult,
            Model model,
            @PathVariable Long code
    ) throws IOException {



        Message message = messageService.findByMessageId(code);

        comment.setAuthor(user);
        comment.setMessage(message);
        comment.setOrder_num(commentService.findMAxOrderNum(message.getMessageId())+1);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            List<Comment> comments = commentService.findByMessage(message);
            model.addAttribute("comment", comment);
            model.addAttribute("comments", comments);
            model.addAttribute("message", message);

        }else {
            commentService.save(comment);
            List<Comment> comments = commentService.findByMessage(message);
            model.addAttribute("comment", null);
            model.addAttribute("comments", comments);
            model.addAttribute("message", message);

        }
        return "message";
    }


}
