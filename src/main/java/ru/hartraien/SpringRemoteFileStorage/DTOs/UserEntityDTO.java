package ru.hartraien.SpringRemoteFileStorage.DTOs;


import org.springframework.validation.annotation.Validated;
import ru.hartraien.SpringRemoteFileStorage.Entities.UserEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
public class UserEntityDTO {
    @NotNull
    @NotBlank(message = "Username should not be empty")
    private String username;
    @ValidPassword
    private String password;
    @NotNull
    @NotBlank(message = "Email should not be empty")
    @Email(message = "This email is not of valid format: [smth]@[smth].[smth]")
    private String email;

    public UserEntityDTO() {
    }

    public UserEntityDTO(UserEntity entity) {
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        this.email = entity.getEmail();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserEntity toUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setEmail(email);
        return userEntity;
    }
}
