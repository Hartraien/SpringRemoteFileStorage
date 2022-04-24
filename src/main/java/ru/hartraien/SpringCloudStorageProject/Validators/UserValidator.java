package ru.hartraien.SpringCloudStorageProject.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.hartraien.SpringCloudStorageProject.Entities.UserEntity;
import ru.hartraien.SpringCloudStorageProject.Services.UserServicePackage.UserService;

/**
 * Validates user input
 */
@Component
public class UserValidator implements Validator
{

    private final UserService userService;

    @Autowired
    public UserValidator( UserService userService )
    {
        this.userService = userService;
    }

    @Override
    public boolean supports( Class<?> clazz )
    {
        return UserEntity.class.equals( clazz );
    }

    @Override
    public void validate( Object target, Errors errors )
    {
        UserEntity user = (UserEntity) target;
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "username", "NotEmpty" );
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "password", "NotEmpty" );
        if ( userService.findByUsername( user.getUsername() ) != null )
        {
            errors.rejectValue( "username", "Duplicate.userForm.username" );
        }

//        if(user.getName().length()<6 || user.getName().length()>32)
//            errors.rejectValue( "name", "Size.userForm.username" );
//
//        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
//            errors.rejectValue("password", "Size.userForm.password");
//        }
    }
}
