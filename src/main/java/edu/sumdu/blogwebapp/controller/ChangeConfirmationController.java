package edu.sumdu.blogwebapp.controller;

import edu.sumdu.blogwebapp.entity.PendingUserChanges;
import edu.sumdu.blogwebapp.enums.ChangedParameters;
import edu.sumdu.blogwebapp.service.PendingUserChangesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import edu.sumdu.blogwebapp.entity.User;

@Controller
public class ChangeConfirmationController {

    @Autowired
    private PendingUserChangesService pendingUserChangesService;
    @GetMapping("/changeconfirm/{code}")
    public String activate(Model model, @PathVariable String code) {

        PendingUserChanges pendingUserChanges = pendingUserChangesService.findPendingUserChangesByConfirmationCode(code);
   /*     boolean isMailChange = false;
        String  newEmail = "";
        if (pendingUserChanges.getId().getParameter().equals(ChangedParameters.MAIL)){
            isMailChange = true;
            newEmail = pendingUserChanges.getNewValue();
        }
*/
        boolean isChanged= pendingUserChangesService.applyChange(code);

        if (isChanged) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully changed. Please relogin to complete changes");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Confirmation code is not found!");
        }

        return "changeconfirmation";
    }
}
