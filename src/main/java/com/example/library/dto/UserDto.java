package com.example.library.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserDto {

    @NotEmpty(message = "Логин не может быть пустым")
    @Size(min = 3, max = 20, message = "От 3 до 20 символов")
    public String username;

    @NotEmpty(message = "Имя не может быть пустым")
    public String name;

    @NotEmpty(message = "Фамилия не может быть пустой")
    public String surname;

    @NotEmpty(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 20, message = "От 8 до 20 символов")
    public String password;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
