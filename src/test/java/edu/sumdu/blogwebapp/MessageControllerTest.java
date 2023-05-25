package edu.sumdu.blogwebapp;

import edu.sumdu.blogwebapp.controller.MessageController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MessageController controller;

    @Test
    public  void  contextTest() throws Exception{
        assertThat(controller).isNotNull();
    }
}
