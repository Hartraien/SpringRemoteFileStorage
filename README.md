# Облачное хранилище файлов

## Описание

Веб приложение, позволяющее пользователю зарегистрироваться и хранить свои файлы на сервере. Для организации файлов
пользователи могут создавать директории, аналогично файловой системе. У каждого пользователя своя root directory, а
потому пользователи не видят и не могут влиять на директории друг друга.

Приложение контейнеризовано в виде docker-контейнера.

## Сборка и Запуск

Сборка приложения осуществляется с помощью Maven командой

```
./mvnw clean package
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

В файле [credentials.properties](src/main/resources/credentials.properties) следующие свойства отвечают за систему
восстановления пароля

```
spring.mail.username=test@gmail.com (адрес с которого будут отправляться письма для восстановления пароля)
spring.mail.password=password (пароль от почты для отправки писем)
```

А свойства начинающиеся с `admin.` задают логин, пароль и email администратора

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