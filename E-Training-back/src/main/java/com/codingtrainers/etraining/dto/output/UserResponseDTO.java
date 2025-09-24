package com.codingtrainers.etraining.dto.output;

import com.codingtrainers.etraining.entities.Role;
import com.codingtrainers.etraining.entities.User;

import java.time.LocalDate;

public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String surname;
    private Role role;
    private String dni;
    private String username;
    private LocalDate birthday;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.surname = user.getSurname();
        this.role = user.getRole();
        this.dni = user.getDni();
        this.username = user.getUsername();
        this.birthday = user.getBirthday();
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
