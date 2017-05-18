package test;

import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControllerTest extends TestBase {
    private static final Logger Log = LoggerFactory.getLogger(ControllerTest.class);

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        User user = new User();
        user.setBirthDate(Date.valueOf("1995-01-05"));
        user.setUserId("15622743170");
        user.setPassword("123456");
        user.setName("wyf");
        user.setGender("male");
        user.setLocation("至九");
        userService.createUserWithRawPassword(user);
    }

    @Test
    public void testUserLogin() throws Exception {
        mockMvc.perform(post("/user/login")
                .param("userId", "1562274317")
                .param("password", "123456"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/user/login")
                .param("userId", "15622743170")
                .param("password", "12345"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/user/login")
                .param("userId", "15622743175")
                .param("password", "123456"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/user/login")
                .param("userId", "15622743170")
                .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.userId").value("15622743170"));
    }

    @Test
    public void testUserInfo() throws Exception {
        mockMvc.perform(post("/user/info")
                .param("userId", "1562274317")
                .param("password", "123456"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/user/info")
                .param("userId", "15622743170")
                .param("password", "12345"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/user/info")
                .param("userId", "15622743175")
                .param("password", "12345"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/user/info")
                .param("userId", "15622743170")
                .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.userId").value("15622743170"))
                .andExpect(jsonPath("$.name").value("wyf"))
                .andExpect(jsonPath("$.gender").value("male"))
                .andExpect(jsonPath("$.birthDate").value("1995-01-05"))
                .andExpect(jsonPath("$.location").value("至九"));
    }

    @Test
    public void testUserModifyInfo() throws Exception {
        mockMvc.perform(post("/user/modifyInfo")
                .param("userId", "1562274317")
                .param("password", "123456")
                .param("name", "李四")
                .param("gender", "female")
                .param("birthDate", "1996-01-05")
                .param("location", "至十"))
                .andExpect(status().is(403));
        mockMvc.perform(post("/user/modifyInfo")
                .param("userId", "15622743170")
                .param("password", "12345")
                .param("name", "李四")
                .param("gender", "female")
                .param("birthDate", "1996-01-05")
                .param("location", "至十"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/user/modifyInfo")
                .param("userId", "15622743175")
                .param("password", "123456")
                .param("name", "李四")
                .param("gender", "female")
                .param("birthDate", "1996-01-05")
                .param("location", "至十"))
                .andExpect(status().is(404));
        mockMvc.perform(post("/user/modifyInfo")
                .param("userId", "15622743170")
                .param("password", "123456")
                .param("name", "李四")
                .param("gender", "female")
                .param("birthDate", "1996-01-05")
                .param("location", "至十"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.userId").value("15622743170"));
        mockMvc.perform(post("/user/info")
                .param("userId", "15622743170")
                .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.userId").value("15622743170"))
                .andExpect(jsonPath("$.name").value("李四"))
                .andExpect(jsonPath("$.gender").value("female"))
                .andExpect(jsonPath("$.birthDate").value("1996-01-05"))
                .andExpect(jsonPath("$.location").value("至十"));
    }
}
