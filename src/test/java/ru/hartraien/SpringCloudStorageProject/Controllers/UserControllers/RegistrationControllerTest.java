package ru.hartraien.SpringCloudStorageProject.Controllers.UserControllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
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
import ru.hartraien.SpringCloudStorageProject.Validators.UserValidator;

@WebMvcTest
@ContextConfiguration(classes = { BeanConfig.class,WebSecurityConfig.class, RegistrationController.class })
@Import(TestConfig.class)
class RegistrationControllerTest
{

    @MockBean
    private UserValidator userValidator;
    @MockBean
    private UserService userService;
    @MockBean
    private SecurityService securityService;

    @Autowired
    private MockMvc mockMvc;

    @WithAnonymousUser
    @Test
    void getPageAnonymous() throws Exception
    {
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/register" ) )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().string( Matchers.containsString( "<input type=\"submit\" value=\"Register\">" ) ) );
    }

    @WithMockUser(username = "spring")
    @Test
    void getPageAuthenticated() throws Exception
    {
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/register" ) )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().is3xxRedirection() );
    }

    @WithAnonymousUser
    @Test
    void registerValid() throws Exception
    {
        this.mockMvc.perform( MockMvcRequestBuilders.post( "/register" )
                        .param( "fusername", "User" )
                        .param( "fpassword", "Password" )
                        .with( SecurityMockMvcRequestPostProcessors.csrf() )
                )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().is3xxRedirection() );
    }
}