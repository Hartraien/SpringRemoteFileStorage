package ru.hartraien.SpringCloudStorageProject.Entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "t_directory_entities")
public class DirectoryEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "direntity_seq")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String dirname;

    @OneToOne(mappedBy = "dir")
    private UserEntity owner;

    public DirectoryEntity()
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

    public String getDirname()
    {
        return dirname;
    }

    public void setDirname( String dirname )
    {
        this.dirname = dirname;
    }

    public UserEntity getOwner()
    {
        return owner;
    }

    public void setOwner( UserEntity owner )
    {
        this.owner = owner;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;
        DirectoryEntity that = (DirectoryEntity) o;
        return dirname.equals( that.dirname );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( dirname );
    }
}
