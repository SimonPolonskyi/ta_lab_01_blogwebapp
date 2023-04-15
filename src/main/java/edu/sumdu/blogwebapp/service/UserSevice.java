package edu.sumdu.blogwebapp.service;

import edu.sumdu.blogwebapp.entity.PendingUserChanges;
import edu.sumdu.blogwebapp.entity.PendingUserChangesId;
import edu.sumdu.blogwebapp.entity.User;
import edu.sumdu.blogwebapp.enums.ChangedParameters;
import edu.sumdu.blogwebapp.enums.MailType;
import edu.sumdu.blogwebapp.enums.Role;
import edu.sumdu.blogwebapp.repository.PendingUserChangesRepository;
import edu.sumdu.blogwebapp.repository.UserRepository;
import edu.sumdu.blogwebapp.utils.RandomPasswordGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import edu.sumdu.blogwebapp.utils.ServerInfo;



@Service
public class UserSevice implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;




    //@Autowired
    //private PendingUserChangesRepository pendingUserChangesRepository;

    @Autowired
    private  PendingUserChangesService pendingUserChangesService;


    @Autowired
    ServerInfo serverInfo;




    @Override
    public UserDetails  loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public boolean addUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        save(user);

        sendMessage(user,MailType.ACTIVATE);

        return true;
    }


    public void sendMessage(User user, MailType mailType) {
        //ServerInfo serverInfo = new ServerInfo();

        switch(mailType) {
            case ACTIVATE:
                if (!StringUtils.isEmpty(user.getEmail())) {
                    String message = String.format(
                            "Hello, %s! \n" +
                                    "Welcome to TA Lab Blog. For activate you account, navigate to the following link: http://%s/activate/%s",
                            user.getUsername(),
                            serverInfo.getHost(),
                            user.getActivationCode()
                    );

                    mailSender.send(user.getEmail(), "Activation code", message);
                }
                break;
            case RESET:
                if (!StringUtils.isEmpty(user.getEmail())) {

                    RandomPasswordGenerator randomPasswordGenerator = new RandomPasswordGenerator();
                    String newPassword = randomPasswordGenerator.generateCommonLangPassword();
                    resetPassword(user, newPassword);


                    String message = "";
                    Optional<PendingUserChanges> pendingUserChange = pendingUserChangesService.findPendingUserChange(user.getId(), ChangedParameters.PASSWORD);
                    if (pendingUserChange.isPresent()){
                         message = String.format(
                                "Hello, %s! \n" +
                                        "You new password: \n%s\nTo confirm this change following the link: http://%s/changeconfirm/%s",
                                user.getUsername(),
                                newPassword,
                                serverInfo.getHost(),
                                pendingUserChange.get().getConfirmationCode()
                        );
                    }else{
                        message = String.format(
                                "Hello, %s! \n" +
                                        "An error occurred while trying to reset your password. Please try again", user.getUsername());
                    }



               /*     System.out.println("----------------------------------------------------------------------------------------");
                    System.out.println(message);
                    System.out.println("----------------------------------------------------------------------------------------");
                */
                     mailSender.send(user.getEmail(), "Reset password", message);

                }
                break;

            case CHANGE_MAIL:
                if (!StringUtils.isEmpty(user.getEmail())) {

                    String message = "";
                    Optional<PendingUserChanges> pendingUserChange = pendingUserChangesService.findPendingUserChange(user.getId(), ChangedParameters.MAIL);
                    if (pendingUserChange.isPresent()){
                        message = String.format(
                                "Hello, %s! \n" +
                                        "Mail address was changed on %s. To confirm this change following the link: http://%s/changeconfirm/%s",
                                user.getUsername(),
                                pendingUserChange.get().getNewValue(),
                                serverInfo.getHost(),
                                pendingUserChange.get().getConfirmationCode()
                        );
                    }else{
                        message = String.format(
                                "Hello, %s! \n" +
                                        "An error occurred while trying to change the email address. Please try again", user.getUsername());
                    }


               /*     System.out.println("----------------------------------------------------------------------------------------");
                    System.out.println(message);
                    System.out.println("----------------------------------------------------------------------------------------");
                */
                    mailSender.send(user.getEmail(), "Change email", message);
                }
                break;
            default:
                break;
        }

    }


    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);

        save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        save(user);
    }

    @Transactional
    public void updateProfile(User user, String password, String email, String firstName, String lastName) {
        String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email)||(userEmail == null && email !=null));

        if (isEmailChanged) {

            Optional<PendingUserChanges> existingPendingUserChange = pendingUserChangesService.findPendingUserChange(user.getId(), ChangedParameters.MAIL);
            if (existingPendingUserChange.isPresent()){
                pendingUserChangesService.delete(existingPendingUserChange.get());
            }
            PendingUserChangesId pendingUserChangesId = new PendingUserChangesId();
            pendingUserChangesId.setUserId(user.getId());
            pendingUserChangesId.setParameter(ChangedParameters.MAIL);
            PendingUserChanges pendingUserChanges = new PendingUserChanges(user, pendingUserChangesId,email, UUID.randomUUID().toString());

            pendingUserChangesService.save(pendingUserChanges);

            sendMessage(user,MailType.CHANGE_MAIL);
        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if ((firstName != null && !firstName.equals(user.getFirstName())) ||
                (user.getFirstName() != null && !user.getFirstName().equals(firstName))) {
            user.setFirstName(firstName);
        }

        if ((lastName != null && !lastName.equals(user.getLastName())) ||
                (user.getLastName() != null && !user.getLastName().equals(lastName))) {
            user.setLastName(lastName);
        }
         save(user);

    }

    public void resetPassword(User user, String password){

        Optional<PendingUserChanges> existingPendingUserChange = pendingUserChangesService.findPendingUserChange(user.getId(), ChangedParameters.PASSWORD);
        if (existingPendingUserChange.isPresent()){
            pendingUserChangesService.delete(existingPendingUserChange.get());
        }
        PendingUserChangesId pendingUserChangesId = new PendingUserChangesId();
        pendingUserChangesId.setUserId(user.getId());
        pendingUserChangesId.setParameter(ChangedParameters.PASSWORD);
        PendingUserChanges pendingUserChanges = new PendingUserChanges(user, pendingUserChangesId,passwordEncoder.encode(password), UUID.randomUUID().toString());
        pendingUserChangesService.save(pendingUserChanges);

    }
     public User save(User user){

        //fix for @NotBlank + @Transient
        // user.setPassword2(user.getPassword());
         return userRepository.save(user);
     }



}
