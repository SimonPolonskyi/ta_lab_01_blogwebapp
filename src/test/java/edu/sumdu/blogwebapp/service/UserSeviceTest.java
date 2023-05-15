package edu.sumdu.blogwebapp.service;

import edu.sumdu.blogwebapp.entity.PendingUserChanges;
import edu.sumdu.blogwebapp.entity.User;
import edu.sumdu.blogwebapp.enums.ChangedParameters;
import edu.sumdu.blogwebapp.enums.MailType;
import edu.sumdu.blogwebapp.enums.Role;
import edu.sumdu.blogwebapp.repository.UserRepository;
import edu.sumdu.blogwebapp.utils.RandomPasswordGenerator;
import edu.sumdu.blogwebapp.utils.ServerInfo;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
class UserSeviceTest {

    @Spy
    @InjectMocks
    private UserSevice userSevice;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    ServerInfo serverInfo;
    
    @MockBean
    PendingUserChangesService pendingUserChangesService;

    @MockBean
    RandomPasswordGenerator randomPasswordGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void loadUserByUsername() {
        String testUsername = "testUser";
        User expectedUser = new User();
        expectedUser.setUsername(testUsername);

        when(userRepository.findByUsername(anyString())).thenReturn(expectedUser);

        UserDetails actualUser = userSevice.loadUserByUsername(testUsername);

        verify(userRepository, times(1)).findByUsername(testUsername);
        assertNotNull(actualUser);
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
    }

    @Test
    void loadUserByUsername_UserNotExists() {
        String testUsername = "testUser";

        when(userRepository.findByUsername(anyString())).thenReturn(null);

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userSevice.loadUserByUsername(testUsername);
        });

        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void findByUsername() {
        String testUsername = "testUser";
        User expectedUser = new User();
        expectedUser.setUsername(testUsername);

        when(userRepository.findByUsername(anyString())).thenReturn(expectedUser);

        User actualUser = userSevice.findByUsername(testUsername);

        verify(userRepository, times(1)).findByUsername(testUsername);
        assertNotNull(actualUser);
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());

    }

    @Test
    void addUser() {
        User user = new User();

        user.setEmail("test@test.com");

        boolean isUserCreated = userSevice.addUser(user);

        Assert.assertTrue(isUserCreated);
        Assert.assertNotNull(user.getActivationCode());
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        verify(userRepository, times(1)).save(user);
        verify(mailSender, times(1))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        anyString(),
                        anyString()
                );
    }

    @Test
    public void addUserFailTest() {
        User user = new User();

        user.setUsername("John");

        Mockito.doReturn(new User())
                .when(userRepository)
                .findByUsername("John");

        boolean isUserCreated = userSevice.addUser(user);

        Assert.assertFalse(isUserCreated);

        verify(userRepository, times(0)).save(ArgumentMatchers.any(User.class));
        verify(mailSender, times(0))
                .send(
                        anyString(),
                        anyString(),
                        anyString()
                );
    }
    @Test
    void sendMessage_Activate() {
        User testUser = new User();
        testUser.setEmail("test@test.com");
        testUser.setUsername("testUser");
        testUser.setActivationCode("123456");

        when(serverInfo.getHost()).thenReturn("localhost");

        userSevice.sendMessage(testUser, MailType.ACTIVATE);

        verify(mailSender, times(1)).send(
                eq(testUser.getEmail()),
                eq("Activation code"),
                anyString()
        );
    }

    @Test
    void sendMessage_Reset_PasswordChangeExists() {
        User testUser = new User();
        testUser.setEmail("test@test.com");
        testUser.setUsername("testUser");
        testUser.setId(1L);

        String newPassword = "newPassword";

        PendingUserChanges pendingUserChanges = new PendingUserChanges();
        pendingUserChanges.setConfirmationCode("confirmationCode");
        pendingUserChanges.setUser(testUser);

        when(serverInfo.getHost()).thenReturn("localhost");

        when(randomPasswordGenerator.generateCommonLangPassword()).thenReturn(newPassword);
        when(pendingUserChangesService.findPendingUserChange(testUser.getId(), ChangedParameters.PASSWORD)).thenReturn(Optional.of(pendingUserChanges));

        userSevice.sendMessage(testUser, MailType.RESET);

        verify(mailSender, times(1)).send(
                eq(testUser.getEmail()),
                eq("Reset password"),
                contains("http://localhost/changeconfirm/confirmationCode")
        );
        verify(pendingUserChangesService, times(2)).findPendingUserChange(testUser.getId(), ChangedParameters.PASSWORD);
    }

    @Test
    void sendMessage_Reset_PasswordChangeNotExists() {
        User testUser = new User();
        testUser.setEmail("test@test.com");
        testUser.setUsername("testUser");
        testUser.setId(1L);

        String newPassword = "newPassword";

        when(serverInfo.getHost()).thenReturn("localhost");
        when(randomPasswordGenerator.generateCommonLangPassword()).thenReturn(newPassword);
        when(pendingUserChangesService.findPendingUserChange(testUser.getId(), ChangedParameters.PASSWORD)).thenReturn(Optional.empty());

        userSevice.sendMessage(testUser, MailType.RESET);

        verify(mailSender, times(1)).send(
                eq(testUser.getEmail()),
                eq("Reset password"),
                contains("An error occurred while trying to reset your password. Please try again")
        );
        verify(pendingUserChangesService, times(2)).findPendingUserChange(testUser.getId(), ChangedParameters.PASSWORD);
    }

    @Test
    void sendMessage_Reset_MailChangeExists() {
        User testUser = new User();
        testUser.setEmail("test@test.com");
        testUser.setUsername("testUser");
        testUser.setId(1L);



        PendingUserChanges pendingUserChanges = new PendingUserChanges();
        pendingUserChanges.setConfirmationCode("confirmationCode");
        pendingUserChanges.setUser(testUser);

        when(serverInfo.getHost()).thenReturn("localhost");

        when(pendingUserChangesService.findPendingUserChange(testUser.getId(), ChangedParameters.MAIL)).thenReturn(Optional.of(pendingUserChanges));

        userSevice.sendMessage(testUser, MailType.CHANGE_MAIL);

        verify(mailSender, times(1)).send(
                eq(testUser.getEmail()),
                eq("Change email"),
                contains("http://localhost/changeconfirm/confirmationCode")
        );
        verify(pendingUserChangesService, times(1)).findPendingUserChange(testUser.getId(), ChangedParameters.MAIL);
    }

    @Test
    void sendMessage_Reset_MailChangeNotExists() {
        User testUser = new User();
        testUser.setEmail("test@test.com");
        testUser.setUsername("testUser");
        testUser.setId(1L);


        when(serverInfo.getHost()).thenReturn("localhost");
        when(pendingUserChangesService.findPendingUserChange(testUser.getId(), ChangedParameters.MAIL)).thenReturn(Optional.empty());

        userSevice.sendMessage(testUser, MailType.CHANGE_MAIL);

        verify(mailSender, times(1)).send(
                eq(testUser.getEmail()),
                eq("Change email"),
                contains("An error occurred while trying to change the email address. Please try again")
        );
        verify(pendingUserChangesService, times(1)).findPendingUserChange(testUser.getId(), ChangedParameters.MAIL);
    }
    @Test
    void activateUser_UserExists() {
        String activationCode = "12345";
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setActivationCode(activationCode);

        when(userRepository.findByActivationCode(anyString())).thenReturn(testUser);

        boolean result = userSevice.activateUser(activationCode);

        verify(userSevice, times(1)).save(testUser);
        assertTrue(result);
        assertNull(testUser.getActivationCode());
    }

    @Test
    void activateUser_UserNotExists() {
        String activationCode = "12345";

        when(userRepository.findByActivationCode(anyString())).thenReturn(null);

        boolean result = userSevice.activateUser(activationCode);

        verify(userSevice, times(0)).save(any(User.class));
        assertFalse(result);
    }

    @Test
    void findAll() {
        User user1 = new User();
        User user2 = new User();
        List<User> expectedUsers = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userSevice.findAll();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void saveUser() {
        User testUser = new User();
        testUser.setUsername("oldUsername");
        testUser.setRoles(new HashSet<>());

        String newUsername = "newUsername";
        Map<String, String> form = new HashMap<>();
        form.put("ADMIN", "");

        userSevice.saveUser(testUser, newUsername, form);

        assertEquals(newUsername, testUser.getUsername());
        assertTrue(testUser.getRoles().contains(Role.ADMIN));
        verify(userSevice, times(1)).save(testUser);
    }

    @Test
    void updateProfile() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("oldEmail@test.com");
        testUser.setFirstName("OldFirstName");
        testUser.setLastName("OldLastName");
        testUser.setPassword(passwordEncoder.encode("oldPassword"));

        String newEmail = "newEmail@test.com";
        String newFirstName = "NewFirstName";
        String newLastName = "NewLastName";
        String newPassword = "newPassword";

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userSevice.updateProfile(testUser, newPassword, newEmail, newFirstName, newLastName);

        verify(pendingUserChangesService, times(2)).findPendingUserChange(anyLong(), any());
        verify(userSevice, times(1)).save(any(User.class));
        verify(userSevice, times(1)).sendMessage(any(User.class), any());

        assertEquals("encodedPassword", testUser.getPassword());
        assertEquals(newFirstName, testUser.getFirstName());
        assertEquals(newLastName, testUser.getLastName());
    }

    @Test
    void updateProfile_MailChangeExists() {
        User testUser = new User();
        testUser.setEmail("oldEmail@test.com");
        testUser.setFirstName("OldFirstName");
        testUser.setLastName("OldLastName");
        testUser.setId(1L);
        testUser.setPassword(passwordEncoder.encode("oldPassword"));

        String newEmail = "newEmail@test.com";
        String newFirstName = "OldFirstName";
        String newLastName = "OldLastName";
        String newPassword = "";


        PendingUserChanges pendingUserChanges = new PendingUserChanges();
        pendingUserChanges.setConfirmationCode("confirmationCode");
        pendingUserChanges.setUser(testUser);


        when(pendingUserChangesService.findPendingUserChange(testUser.getId(), ChangedParameters.MAIL)).thenReturn(Optional.of(pendingUserChanges));

        userSevice.updateProfile(testUser, newPassword, newEmail, newFirstName, newLastName);

        verify(pendingUserChangesService, times(2)).findPendingUserChange(anyLong(), any());
        verify(userSevice, times(1)).save(any(User.class));
        verify(userSevice, times(1)).sendMessage(any(User.class), any());


        assertEquals(newFirstName, testUser.getFirstName());
        assertEquals(newLastName, testUser.getLastName());
    }
    @Test
    void resetPassword() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setPassword(passwordEncoder.encode("oldPassword"));

        String newPassword = "newPassword";

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userSevice.resetPassword(testUser, newPassword);

        verify(pendingUserChangesService, times(1)).findPendingUserChange(anyLong(), any());
        verify(pendingUserChangesService, times(1)).save(any(PendingUserChanges.class));

    }

    @Test
    void save_Test() {
        User testUser = new User();
        testUser.setUsername("testUser");

        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userSevice.save(testUser);

        verify(userRepository, times(1)).save(testUser);
        assertNotNull(savedUser);
        assertEquals(testUser.getUsername(), savedUser.getUsername());
    }

}