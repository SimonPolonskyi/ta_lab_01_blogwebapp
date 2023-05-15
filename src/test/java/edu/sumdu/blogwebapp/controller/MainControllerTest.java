package edu.sumdu.blogwebapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create_users.sql", "/create_msg.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clear_db_after_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

class MainControllerTest {

    @Autowired
    private MainController mainController;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void greetingTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("first msg")))
                .andExpect(content().string(containsString("Home")))
                .andExpect(content().string(containsString("Add Message")))
                .andExpect(content().string(containsString("Sign In")));
    }

    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "user1")
    public void mainTestFilter() throws Exception {
        this.mockMvc.perform(get("/main").param("filter", "tag 2"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(1))
                .andExpect(xpath("//*[@id='message-list']/div[@msg-id='2']").exists())
                .andExpect(xpath("//*[@id='message-list']/div[@msg-id='1']").doesNotExist())
                .andExpect(xpath("//*[@id='message-list']/div[@msg-id='2']/div/span").string("second"));
    }

    @Test
    @WithUserDetails(value = "user1")
    public void mainTestEmptyF() throws Exception {
        this.mockMvc.perform(get("/main").param("filter", ""))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(2))
                .andExpect(xpath("//*[@id='message-list']/div[@msg-id='2']").exists())
                .andExpect(xpath("//*[@id='message-list']/div[@msg-id='1']").exists());
    }
    @Test
    @WithUserDetails(value = "user1")
    public void addTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/main")
                .file("file", "file".getBytes())
                .param("text", "test_msg")
                .param("tag", "tag 3")
                .with(csrf());

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(3))
                .andExpect(xpath("//*[@id='message-list']/div[@msg-id='51']").exists())
                .andExpect(xpath("//*[@id='message-list']/div[@msg-id='51']/div/span").string("test_msg"))
                .andExpect(xpath("//*[@id='message-list']/div[@msg-id='51']/div/div/div/i").string("tag 3"));
    }

    @Test
    @WithUserDetails(value = "user1")
    public void addErrorTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/main")
                .file("file", "file".getBytes())
                .param("text", "")
                .param("tag", "tag 4")
                .with(csrf());

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(2))
                .andExpect(xpath("//*[@id=\"collapseExample\"]/div/form/div[1]/div").string("\n" +
                        "                            Please fill the message\n" +
                        "                        "));
    }

}