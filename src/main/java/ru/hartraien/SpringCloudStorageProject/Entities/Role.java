package ru.hartraien.SpringCloudStorageProject.Entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "t_user_roles")
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;

    public Role()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Set<UserEntity> getUsers()
    {
        return users;
    }

    public void setUsers( Set<UserEntity> users )
    {
        this.users = users;
    }
}
