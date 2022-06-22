# Облачное хранилище файлов

## Описание

Веб приложение на Spring Boot, позволяющее пользователю зарегистрироваться и хранить свои файлы на сервере. Для организации файлов
пользователи могут создавать директории, аналогично файловой системе. У каждого пользователя своя root directory, а
потому пользователи не видят и не могут влиять на директории друг друга.

Приложение контейнеризовано в виде docker-контейнера.

## Сборка и Запуск

Сборка приложения осуществляется с помощью Maven командой

```
./mvnw clean package
```

Docker-образ приложения описан в [DockerFile](DockerFile) и сборка осуществляется командой

```
docker build .
```

Docker-контейнер приложения и базы данных, используемый приложением поднимается командой

```
docker compose up
```

### Использованные Технологии

- Java
- Spring Boot
    - Core
    - MVC
    - Data
    - Security
    - Validation
    - Mail (Для восстановления пароля)
- Thymeleaf
- Passay
- PostgreSQL
- Тестирование
    - Junit5
    - Mockito
    - H2 Database

### Дополнительные настройки

В приложении предусмотрена система восстановления пароля: по запросу пользователя ему на почту приходит письмо с ссылкой на форму замены пароля на новый.
Для того, чтобы система работала нужно в файле [credentials.properties](src/main/resources/credentials.properties) следующим свойствам задать правильные значения

```
spring.mail.username=test@gmail.com (адрес с которого будут отправляться письма для восстановления пароля)
spring.mail.password=password (пароль от почты для отправки писем)
```

По умолчанию система восстановления пароля использует сервис gmail рассылки писем, для смены нужно изменить свойства `spring.mail.host` и `spring.mail.port` в файле [application.properties](src/main/resources/application.properties) 

В том же файле [credentials.properties](src/main/resources/credentials.properties) свойства начинающиеся с `admin.` задают логин, пароль и email администратора

```
admin.username=admin
admin.password=1234
admin.email=test@test.com
```

Параметры подключения к СУБД указаны в переменных окружения в [docker-compose.yml](docker-compose.yml), а именно
```
spring.datasource.url
spring.datasource.username
spring.datasource.password
spring.datasource.driverClassName
spring.jpa.database-platform
spring.jpa.hibernate.ddl-auto
```