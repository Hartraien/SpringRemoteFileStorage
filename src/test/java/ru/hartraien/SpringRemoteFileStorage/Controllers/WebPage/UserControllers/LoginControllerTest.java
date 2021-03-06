package ru.hartraien.SpringRemoteFileStorage.Controllers.WebPage.UserControllers;

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
import ru.hartraien.SpringRemoteFileStorage.Configs.BeanConfig;
import ru.hartraien.SpringRemoteFileStorage.Configs.WebSecurityConfig;
import ru.hartraien.SpringRemoteFileStorage.ConfigsForTest.TestConfig;


@WebMvcTest
@ContextConfiguration(classes = { BeanConfig.class, WebSecurityConfig.class, LoginController.class })
@Import(TestConfig.class)
class LoginControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @WithAnonymousUser
    @Test
    void getPageAnonymous() throws Exception
    {
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/login" ) )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().string( Matchers.containsString( "Sign Up" ) ) )
                .andExpect( MockMvcResultMatchers.content().string( Matchers.not( Matchers.containsString( "Your files" ) ) ) );
    }

    @Test
    @WithMockUser(username = "spring")
    void getPageLoggedIn() throws Exception
    {
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/login" ) )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().is3xxRedirection() );
    }
}