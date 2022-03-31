package ru.hartraien.SpringCloudStorageProject.DAOs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hartraien.SpringCloudStorageProject.CRUDInterfaces.UserRepository;
import ru.hartraien.SpringCloudStorageProject.POJO.Role;
import ru.hartraien.SpringCloudStorageProject.POJO.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDAO
{
    private final UserRepository userRepository;

    @Autowired
    public UserDAO( UserRepository userRepository )
    {
        this.userRepository = userRepository;
    }


    public String checkUser( String fusername, String fpassword )
    {
        if(this.checkIfUserExists( fusername,fpassword ))
            return "There is such user!";
        else
            return "There is no such user";
    }

    private boolean checkIfUserExists(String fusername, String fpassword)
    {
        final UserEntity tempUser = new UserEntity(fusername, fpassword);
        final UserEntity user = userRepository.findUserByUsername( fusername );
        return tempUser.equals( user );
    }

    public String registerUser( String fusername, String fpassword, Role role )
    {
        if(this.checkIfUserExists( fusername,fpassword ))
            return "Such user already exists";
        else
        {
            UserEntity user = new UserEntity( fusername, fpassword, role );
            final var saveduser = userRepository.save( user );
            if(user.equals( saveduser ))
                return "Succesfully saved new user";
            else
                return "Could not save user!";
        }
    }

    public List<UserEntity> getAllUsers()
    {
        var iterColl =  userRepository.findAll();
        List<UserEntity> result = new ArrayList<>();
        iterColl.forEach( result::add );
        return result;
    }
}
