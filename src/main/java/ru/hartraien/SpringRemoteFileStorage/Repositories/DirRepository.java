package ru.hartraien.SpringRemoteFileStorage.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.hartraien.SpringRemoteFileStorage.Entities.DirectoryEntity;

@Transactional(propagation = Propagation.MANDATORY)
public interface DirRepository extends JpaRepository<DirectoryEntity, Long>
{
    DirectoryEntity findByDirname( String name );
}
