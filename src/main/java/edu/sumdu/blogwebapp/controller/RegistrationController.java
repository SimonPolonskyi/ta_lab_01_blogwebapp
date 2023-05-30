package edu.sumdu.blogwebapp.controller;

import edu.sumdu.blogwebapp.entity.User;
import edu.sumdu.blogwebapp.service.StringValidationService;
import edu.sumdu.blogwebapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    private final StringValidationService stringValidationService;


    @Autowired
    public RegistrationController(StringValidationService stringValidationService) {
        this.stringValidationService = stringValidationService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user, BindingResult bindingResult, Model model) {

        boolean paramsHasEror = false;

        user.setUsername(stringValidationService.escapeHtml(user.getUsername()));
        user.setFirstName(stringValidationService.escapeHtml(user.getFirstName()));
        user.setLastName(stringValidationService.escapeHtml(user.getLastName()));



        if (user.getPassword2()== null ) {
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
            paramsHasEror=true;
            // return "registration";
        }


        if (user.getPassword() != null && !user.getPassword().equals(user.getPassword2())) {
            model.addAttribute("passwordError", "Passwords are different!");
            paramsHasEror=true;
           // return "registration";
        }

        if (!stringValidationService.isValidEmailAddress(user.getEmail())){
            user.setEmail(stringValidationService.escapeHtml(user.getEmail()));
            paramsHasEror=true;

        }

        if (paramsHasEror || bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);

            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }


}
