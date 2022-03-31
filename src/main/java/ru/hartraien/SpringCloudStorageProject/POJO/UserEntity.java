package ru.hartraien.SpringCloudStorageProject.POJO;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="user_entity")
public class UserEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    public UserEntity()
    {
    }

    public UserEntity( String username, String password )
    {
        this.username = username;
        this.password = password;
    }

    public UserEntity( String username, String password, Role role )
    {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public Role getRole()
    {
        return role;
    }

    public void setRole( Role role )
    {
        this.role = role;
    }

    @Override
    public String toString()
    {
        return "UserEntity{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role.toString() +
                '}';
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        UserEntity that = (UserEntity) o;
        return username.equals( that.username );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( username );
    }
}
