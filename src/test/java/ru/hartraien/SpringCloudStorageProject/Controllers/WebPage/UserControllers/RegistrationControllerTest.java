package ru.hartraien.SpringCloudStorageProject.Controllers.WebPage.UserControllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hartraien.SpringCloudStorageProject.Configs.BeanConfig;
import ru.hartraien.SpringCloudStorageProject.Configs.WebSecurityConfig;
import ru.hartraien.SpringCloudStorageProject.ConfigsForTest.TestConfig;
import ru.hartraien.SpringCloudStorageProject.Services.SecurityServicePackage.SecurityService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;
import ru.hartraien.SpringCloudStorageProject.Validators.PasswordConstraintValidator;

import java.util.List;
import java.util.stream.Collectors;

@WebMvcTest
@ContextConfiguration(classes = {BeanConfig.class, WebSecurityConfig.class, RegistrationController.class})
@Import(TestConfig.class)
class RegistrationControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private SecurityService securityService;

    @Autowired
    private MockMvc mockMvc;

    @WithAnonymousUser
    @Test
    void getPageAnonymous() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("<input type=\"submit\" value=\"Register\">")));
    }

    @WithMockUser(username = "spring")
    @Test
    void getPageAuthenticated() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @WithAnonymousUser
    @Test
    void registerValid() throws Exception {
        PasswordConstraintValidator validator = new PasswordConstraintValidator();
        PasswordGenerator generator = new PasswordGenerator();
        List<CharacterRule> rules = validator.getRules().stream().filter(rule -> rule instanceof CharacterRule).map(rule -> (CharacterRule) rule).collect(Collectors.toList());

        String password = generator.generatePassword(10, rules);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", "User")
                        .param("password", password)
                        .param("email", "test@test.com")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @WithAnonymousUser
    @Test
    void registerInvalid() throws Exception {

        String password = "password";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", "User")
                        .param("password", password)
                        .param("email", "test@test.com")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}