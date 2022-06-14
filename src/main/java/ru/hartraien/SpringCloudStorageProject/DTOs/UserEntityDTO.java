package ru.hartraien.SpringCloudStorageProject.DTOs;


import org.springframework.validation.annotation.Validated;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
public class UserEntityDTO
{
    @NotNull
    @NotBlank(message = "Username should not be empty")
    private String username;
    @NotNull
    @NotBlank(message = "Password should not be empty")
    private String password;
    @NotNull
    @NotBlank(message = "Email should not be empty")
    @Email
    private String email;

    public UserEntityDTO()
    {
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public UserEntity toUserEntity()
    {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername( username );
        userEntity.setPassword( password );
        userEntity.setEmail( email );
        return userEntity;
    }
}
