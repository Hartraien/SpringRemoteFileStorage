package ru.hartraien.SpringRemoteFileStorage.Entities;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "t_user_entities")
public class UserEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userentity_seq")
    private Long id;

    @NotNull
    @NotBlank(message = "Username should not be empty")
    @Column(unique = true)
    private String username;
    @NotNull
    @NotBlank(message = "Password should not be empty")
    private String password;

    @NotNull
    @NotBlank(message = "Email should not be empty")
    @Email(message = "This email is not of valid format: [smth]@[smth].[smth]")
    private String email;

    private String resetPasswordToken;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "User_Roles", joinColumns = @JoinColumn(name = "username"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles;

    @OneToOne(cascade = CascadeType.ALL)
    private DirectoryEntity dir;

    public UserEntity()
    {
        roles = new HashSet<>();
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles( Set<Role> roles )
    {
        this.roles = roles;
    }

    public void addRole( Role role )
    {
        roles.add( role );
    }

    public DirectoryEntity getDir()
    {
        return dir;
    }

    public void setDir( DirectoryEntity dir )
    {
        this.dir = dir;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;
        UserEntity that = (UserEntity) o;
        return username.equals( that.username ) && password.equals( that.password );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( username, password );
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getResetPasswordToken()
    {
        return resetPasswordToken;
    }

    public void setResetPasswordToken( String resetPasswordToken )
    {
        this.resetPasswordToken = resetPasswordToken;
    }
}
