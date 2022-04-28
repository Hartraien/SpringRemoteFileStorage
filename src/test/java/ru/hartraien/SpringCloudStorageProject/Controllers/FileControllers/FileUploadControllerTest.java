package ru.hartraien.SpringCloudStorageProject.Controllers.FileControllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hartraien.SpringCloudStorageProject.Configs.WebSecurityConfig;
import ru.hartraien.SpringCloudStorageProject.ConfigsForTest.TestConfig;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.DirServicePackage.DirService;
import ru.hartraien.SpringCloudStorageProject.Services.StorageServicePackage.StorageService;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

import java.nio.charset.StandardCharsets;

@WebMvcTest
@ContextConfiguration(classes = { WebSecurityConfig.class, FileUploadController.class })
@Import(TestConfig.class)
class FileUploadControllerTest
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
    void uploadFile() throws Exception
    {
        UserEntity user = new UserEntity();
        DirectoryEntity directory = new DirectoryEntity();
        directory.setDirname( "directory" );
        user.setDir( directory );

        Mockito.when( userService.findByUsername( Mockito.anyString() ) ).thenReturn( user );

        MockMultipartFile file = new MockMultipartFile( "file", "file.txt", MediaType.TEXT_PLAIN_VALUE, "content".getBytes( StandardCharsets.UTF_8 ) );

        this.mockMvc.perform( MockMvcRequestBuilders
                        .multipart( "/uploadpage" )
                        .file( file )
                        .param( "path", "" )
                        .with( SecurityMockMvcRequestPostProcessors.csrf() )
                )
                .andExpect( MockMvcResultMatchers.status().is3xxRedirection() );
    }
}