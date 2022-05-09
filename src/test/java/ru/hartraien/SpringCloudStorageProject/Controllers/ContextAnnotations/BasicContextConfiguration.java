package ru.hartraien.SpringCloudStorageProject.Controllers.ContextAnnotations;

import org.springframework.test.context.ContextConfiguration;
import ru.hartraien.SpringCloudStorageProject.Configs.BeanConfig;
import ru.hartraien.SpringCloudStorageProject.Configs.WebSecurityConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(classes = { BeanConfig.class, WebSecurityConfig.class})
public @interface BasicContextConfiguration
{

}
