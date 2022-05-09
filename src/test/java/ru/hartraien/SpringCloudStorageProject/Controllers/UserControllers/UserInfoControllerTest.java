package ru.hartraien.SpringCloudStorageProject.Controllers.UserControllers;

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
@ContextConfiguration(classes = { BeanConfig.class, WebSecurityConfig.class, UserInfoController.class })
@Import(TestConfig.class)
class UserInfoControllerTest
{


    @Autowired
    private MockMvc mockMvc;

    @WithAnonymousUser
    @Test
    void getPageAnon() throws Exception
    {
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/userinfo" ) )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().is3xxRedirection() );
    }

    @WithMockUser(username = "spring", authorities = { "Role_User" })
    @Test
    void getPageAuth() throws Exception
    {
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/userinfo" ) )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().string( Matchers.containsString( "<button class=\"btn btn-danger\" type=\"submit\">Delete all my files</button>" ) ) );
    }
}