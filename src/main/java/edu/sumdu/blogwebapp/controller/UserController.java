package edu.sumdu.blogwebapp.controller;

import edu.sumdu.blogwebapp.entity.User;
import edu.sumdu.blogwebapp.enums.Role;
import edu.sumdu.blogwebapp.service.StringValidationService;
import edu.sumdu.blogwebapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    private final StringValidationService stringValidationService;


    @Autowired
    public UserController(StringValidationService stringValidationService) {
        this.stringValidationService = stringValidationService;
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) {
       userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(@AuthenticationPrincipal UserDetails currentUserDetails, Model model) {
        User currentUser = userService.findByUsername(currentUserDetails.getUsername());
        model.addAttribute("user", currentUser);
        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(@AuthenticationPrincipal UserDetails currentUserDetails,
                                @Valid User updatedUser,
                                BindingResult result,
                                Model model,
                                @RequestParam String password,
                                @RequestParam String password2,
                                @RequestParam String email,
                                @RequestParam String firstName,
                                @RequestParam String lastName
    ) {
        User currentUser = userService.findByUsername(currentUserDetails.getUsername());

        updatedUser.setFirstName(stringValidationService.escapeHtml(updatedUser.getFirstName()));
        updatedUser.setLastName(stringValidationService.escapeHtml(updatedUser.getLastName()));
        boolean paramsHasEror = false;


        if (password!= null && password2== null ) {

            model.addAttribute("password2Error", "Password confirmation cannot be empty");
            paramsHasEror=true;
        }


        if (password!= null &&  !password.equals(password2)) {
            model.addAttribute("password2Error", "Passwords are different!");
            paramsHasEror=true;
        }

        if (email== null || !stringValidationService.isValidEmailAddress(email)) {
            model.addAttribute("emailError", "Email cannot be empty");
            email ="";
            paramsHasEror=true;
        }

        if (paramsHasEror ) {
            return "profile";
        }

        userService.updateProfile(currentUser, password, email, stringValidationService.escapeHtml(firstName), stringValidationService.escapeHtml(lastName));

        return "profile";
    }


}