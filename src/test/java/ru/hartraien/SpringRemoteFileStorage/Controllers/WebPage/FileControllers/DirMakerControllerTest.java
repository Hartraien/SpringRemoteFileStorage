package ru.hartraien.SpringRemoteFileStorage.Controllers.WebPage.FileControllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hartraien.SpringRemoteFileStorage.Configs.BeanConfig;
import ru.hartraien.SpringRemoteFileStorage.Configs.WebSecurityConfig;
import ru.hartraien.SpringRemoteFileStorage.ConfigsForTest.TestConfig;
import ru.hartraien.SpringRemoteFileStorage.Entities.DirectoryEntity;
import ru.hartraien.SpringRemoteFileStorage.Entities.UserEntity;
import ru.hartraien.SpringRemoteFileStorage.Services.DirServicePackage.DirService;
import ru.hartraien.SpringRemoteFileStorage.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringRemoteFileStorage.Services.UserServicePackage.UserService;

@WebMvcTest
@ContextConfiguration(classes = { BeanConfig.class, WebSecurityConfig.class, DirMakerController.class })
@Import(TestConfig.class)
class DirMakerControllerTest
{

    @MockBean
    private UserService userService;

    @MockBean
    private DirService dirService;

    @MockBean
    private StorageService storageService;

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "spring", authorities = { "Role_User" })
    @Test
    void makeDir() throws Exception
    {
        UserEntity user = new UserEntity();
        DirectoryEntity directory = new DirectoryEntity();
        directory.setDirname( "directory" );
        user.setDir( directory );

        Mockito.when( userService.findByUsername( Mockito.anyString() ) ).thenReturn( user );

        this.mockMvc.perform( MockMvcRequestBuilders.post( "/makedir" )
                        .param( "name", "name" )
                        .param( "path", "path" )
                        .with( SecurityMockMvcRequestPostProcessors.csrf() )
                )
                .andExpect( MockMvcResultMatchers.status().is3xxRedirection() );
    }
}