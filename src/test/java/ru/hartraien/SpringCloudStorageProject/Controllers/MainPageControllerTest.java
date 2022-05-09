package ru.hartraien.SpringCloudStorageProject.Controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hartraien.SpringCloudStorageProject.Configs.BeanConfig;
import ru.hartraien.SpringCloudStorageProject.Configs.WebSecurityConfig;
import ru.hartraien.SpringCloudStorageProject.ConfigsForTest.TestConfig;

@WebMvcTest
@ContextConfiguration(classes = { BeanConfig.class, WebSecurityConfig.class, MainPageController.class })
@Import(TestConfig.class)
class MainPageControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(value = "spring")
    @Test
    void getPageWithUser() throws Exception
    {
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/" ) )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().string( Matchers.containsString( "Your account page" ) ) )
                .andExpect( MockMvcResultMatchers.content().string( Matchers.not( Matchers.containsString( "List of existing users" ) ) ) );
    }

    @WithMockUser(username = "admin", authorities = { "Role_Admin" })
    @Test
    void getPageWithAdminUser() throws Exception
    {
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/" ) )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().string( Matchers.containsString( "List of existing users" ) ) );
    }

    @WithAnonymousUser
    @Test
    void getPageWithAnonymousUser() throws Exception
    {
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/" ) )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().string( Matchers.containsString( "Login" ) ) )
                .andExpect( MockMvcResultMatchers.content().string( Matchers.not( Matchers.containsString( "Your account page" ) ) ) )
                .andExpect( MockMvcResultMatchers.content().string( Matchers.not( Matchers.containsString( "List of existing users" ) ) ) );
    }
}