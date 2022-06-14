package ru.hartraien.SpringCloudStorageProject.Controllers.WebPage.UserControllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
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
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

import java.util.ArrayList;

@WebMvcTest
@ContextConfiguration(classes = { BeanConfig.class, WebSecurityConfig.class, UserListController.class })
@Import(TestConfig.class)
class UserListControllerTest
{

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @WithAnonymousUser
    @Test
    void getPartialList_asAnon() throws Exception
    {
        this.mockMvc.perform( MockMvcRequestBuilders.get( "/userlist" ) )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().is3xxRedirection() );
    }

    @WithMockUser(username = "spring", authorities = { "Role_User" })
    @Test
    void getPartialList_asUser() throws Exception
    {
        Mockito.when( userService.getAllUsersPaging( Mockito.any() ) ).thenReturn( new PageImpl<>( new ArrayList<>() ) );

        this.mockMvc.perform( MockMvcRequestBuilders.get( "/userlist" ) )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().is3xxRedirection() );
    }

    @WithMockUser(username = "spring", authorities = { "Role_User", "Role_Admin" })
    @Test
    void getPartialList_asAdmin() throws Exception
    {
        Mockito.when( userService.getAllUsersPaging( Mockito.any() ) ).thenReturn( new PageImpl<>( new ArrayList<>() ) );

        this.mockMvc.perform( MockMvcRequestBuilders.get( "/userlist" ) )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().string( Matchers.containsString(
                        "<div class=\"col-auto\">\n" +
                                "                    <label for=\"sizeNew\">Users per page</label>\n" +
                                "                </div>"
                ) ) );
    }
}