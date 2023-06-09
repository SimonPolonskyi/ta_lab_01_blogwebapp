package edu.sumdu.blogwebapp.controller;

import edu.sumdu.blogwebapp.entity.Message;
import edu.sumdu.blogwebapp.entity.User;
import edu.sumdu.blogwebapp.service.StringValidationService;
import edu.sumdu.blogwebapp.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private MessageService messageService;

    @Value("${upload.path}")
    private String uploadPath;

    private final StringValidationService stringValidationService;


    @Autowired
    public MainController(StringValidationService stringValidationService) {
        this.stringValidationService = stringValidationService;
    }

    @GetMapping("/")
    public String greeting(Model model) {
        Iterable<Message> messages = messageService.getAllMessagesWithCommentCount();
        model.addAttribute("messages", messages);
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages = messageService.getAllMessagesWithCommentCount();

        filter = stringValidationService.escapeHtml(filter);

        if (filter != null && !filter.isEmpty()) {
            messages = messageService.findByTag(filter);
        } else {
            messages = messageService.getAllMessagesWithCommentCount();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        message.setAuthor(user);

        message.setText(stringValidationService.escapeHtml(message.getText()));
        message.setTag(stringValidationService.escapeHtml(message.getTag()));


        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();

                file.transferTo(new File(uploadPath + "/" + resultFilename));

                message.setFilename(resultFilename);
            }

            model.addAttribute("message", null);

            messageService.save(message);
        }

        Iterable<Message> messages = messageService.getAllMessagesWithCommentCount();

        model.addAttribute("messages", messages);

        return "main";
    }
}

