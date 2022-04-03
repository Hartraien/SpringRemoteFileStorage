package ru.hartraien.SpringCloudStorageProject.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hartraien.SpringCloudStorageProject.Entities.DirectoryEntity;

public interface DirRepository extends JpaRepository<DirectoryEntity, Long>
{
    DirectoryEntity findByDirname( String name );
}
