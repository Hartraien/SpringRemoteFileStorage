package ru.hartraien.SpringCloudStorageProject.Controllers.FileControllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.security.test.context.support.WithMockUser;
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

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@WebMvcTest
@ContextConfiguration(classes = { WebSecurityConfig.class, FileDownloadController.class })
@Import(TestConfig.class)
class FileDownloadControllerTest
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
    void getFile() throws Exception
    {
        UserEntity user = new UserEntity();
        DirectoryEntity directory = new DirectoryEntity();
        directory.setDirname( "directory" );
        user.setDir( directory );

        Mockito.when( userService.findByUsername( Mockito.anyString() ) ).thenReturn( user );

        String fileText = "text of file\n";

        Resource file = Mockito.mock( Resource.class );
        Mockito.when( file.getInputStream() ).thenReturn( new ByteArrayInputStream( fileText.getBytes( StandardCharsets.UTF_8 ) ) );
        Mockito.when( file.getFilename() ).thenReturn( "filename" );

        Mockito.when( storageService.getFile( Mockito.anyString(), Mockito.anyString() ) ).thenReturn( file );

        var result = this.mockMvc.perform( MockMvcRequestBuilders.get( "/download/file.txt" ) )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().contentType( "application/json" ) )
                .andReturn();

        Assertions.assertTrue( result.getResponse().getContentAsString().contains( fileText ) );
    }
}