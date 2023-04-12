package edu.sumdu.blogwebapp.service;

import edu.sumdu.blogwebapp.entity.PendingUserChanges;
import edu.sumdu.blogwebapp.entity.User;
import edu.sumdu.blogwebapp.enums.ChangedParameters;
import edu.sumdu.blogwebapp.repository.PendingUserChangesRepository;
import edu.sumdu.blogwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PendingUserChangesService {

    @Autowired
    private PendingUserChangesRepository pendingUserChangesRepository;

    @Autowired
    private UserRepository userRepository;


    public PendingUserChanges  save(PendingUserChanges pendingUserChanges){
        return pendingUserChangesRepository.save(pendingUserChanges);

    }

    public void  delete(PendingUserChanges pendingUserChanges){
         pendingUserChangesRepository.delete(pendingUserChanges);
    }

   public PendingUserChanges  findPendingUserChangesByUser(User user){
        return pendingUserChangesRepository.findPendingUserChangesByUser(user);
   };

    //public PendingUserChanges fin

    public PendingUserChanges  findPendingUserChangesByConfirmationCode(String code){
        return pendingUserChangesRepository.findPendingUserChangesByConfirmationCode(code);
    };
    public boolean changeExists(User user, ChangedParameters parameter){
        int count = pendingUserChangesRepository.countByUserIdAndParameter(user.getId(), parameter);
        if (count>0) {
            return true;
        } else {
            return false;
        }
    }

    public Optional<PendingUserChanges> findPendingUserChange(Long userId, ChangedParameters parameter) {
        return pendingUserChangesRepository.findByIdUserIdAndIdParameter(userId, parameter);
    }
    public boolean applyChange(String code) {

        PendingUserChanges pendingUserChanges = pendingUserChangesRepository.findPendingUserChangesByConfirmationCode(code);
        if (pendingUserChanges == null) {
            return false;
        }

        User user = userRepository.findUserById(pendingUserChanges.getUser().getId());
        if (user == null) {
            return false;
        }


        switch (pendingUserChanges.getId().getParameter()){
            case MAIL:
                user.setEmail(pendingUserChanges.getNewValue());
                userRepository.save(user);
                pendingUserChangesRepository.delete(pendingUserChanges);
                break;
            case PASSWORD:
                user.setPassword(pendingUserChanges.getNewValue());
                userRepository.save(user);
                pendingUserChangesRepository.delete(pendingUserChanges);
                break;
            default:
                return false;
        }
        return true;
    }
}
