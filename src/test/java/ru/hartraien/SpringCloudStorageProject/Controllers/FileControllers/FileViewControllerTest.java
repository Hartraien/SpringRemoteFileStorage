package ru.hartraien.SpringCloudStorageProject.Controllers.FileControllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hartraien.SpringCloudStorageProject.Configs.BeanConfig;
import ru.hartraien.SpringCloudStorageProject.Configs.WebSecurityConfig;
import ru.hartraien.SpringCloudStorageProject.ConfigsForTest.TestConfig;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

import java.util.ArrayList;


@WebMvcTest
@ContextConfiguration(classes = { BeanConfig.class,WebSecurityConfig.class, FileViewController.class })
@Import(TestConfig.class)
class FileViewControllerTest
{

    @MockBean
    private UserService userService;

    @MockBean
    private DirService dirService;

    @MockBean
    private StorageService storageService;


    @Autowired
    private MockMvc mockMvc;

    @WithAnonymousUser
    @Test
    void getPageAnon() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders.get( "/viewfiles" ) )
                .andExpect( MockMvcResultMatchers.status().is3xxRedirection() );
    }

    @WithMockUser(value = "spring", authorities = { "Role_User" })
    @Test
    void getPageAuth() throws Exception
    {
        UserEntity user = new UserEntity();
        DirectoryEntity directory = new DirectoryEntity();
        directory.setDirname( "directory" );
        user.setDir( directory );

        Mockito.when( userService.findByUsername( Mockito.anyString() ) ).thenReturn( user );
        Mockito.when( storageService.getAllFilesInDir( Mockito.anyString(), Mockito.anyString() ) ).thenReturn( new ArrayList<>() );

        mockMvc.perform( MockMvcRequestBuilders.get( "/viewfiles" ) )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().string( Matchers.containsString( "<h1 class=\"text-center\">File storage of <span>spring</span></h1>" ) ) );
    }
}