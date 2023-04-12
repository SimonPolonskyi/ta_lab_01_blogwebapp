package edu.sumdu.blogwebapp.controller;

import edu.sumdu.blogwebapp.entity.User;
import edu.sumdu.blogwebapp.enums.MailType;
import edu.sumdu.blogwebapp.service.UserSevice;
import edu.sumdu.blogwebapp.utils.RandomPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/resetpass")
public class ResetPassController {
    @Autowired
    private UserSevice userService;

    @GetMapping
    public String showResetForm(Model model) {
        model.addAttribute("isRequestPosted", false);

        return "resetpass";
    }

    @PostMapping
    public String resetPass(Model model, @RequestParam String username){

        System.out.println("-----------------------------------------------");
        System.out.println("username "+username);

        if (username==null){
            model.addAttribute("usernameError", "The field cannot be empty");
            model.addAttribute("isRequestPosted", true);
        }else {
            model.addAttribute("isRequestPosted", true);
            User user = userService.findByUsername(username);
            if (user!=null){

                userService.sendMessage(user, MailType.RESET);
            }
        }
        return "resetpass";
    }
}
