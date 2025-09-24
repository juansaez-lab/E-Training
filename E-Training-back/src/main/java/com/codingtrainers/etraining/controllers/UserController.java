package com.codingtrainers.etraining.controllers;

import com.codingtrainers.etraining.dto.input.UserRequestDTO;
import com.codingtrainers.etraining.dto.output.UserResponseDTO;
import com.codingtrainers.etraining.entities.Role;
import com.codingtrainers.etraining.entities.User;
import com.codingtrainers.etraining.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDTO userRequestDTO) {

        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setSurname(userRequestDTO.getSurname());
        user.setEmail(userRequestDTO.getEmail());
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(userRequestDTO.getPassword());
        user.setBirthday(userRequestDTO.getBirthday());
        user.setDni(userRequestDTO.getDni());
        user.setRole(Role.PUPIL);
        user.setActive(true);

        userService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");

    }

    @PostMapping("/")
    public ResponseEntity<Void> create(@RequestBody User user) {
        try {
            userService.create(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        try {
            List<UserResponseDTO> users = userService.getAll();
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<UserResponseDTO>> getInactiveUsers() {
        try {
            List<UserResponseDTO> users = userService.getInactiveUsers();
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable("id") Long id) {
        try {
            UserResponseDTO user = userService.getById(id);
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/")
    public ResponseEntity update(@RequestBody User user){
        userService.update(user);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable("id") Long id) {
        try {
            userService.activateUser(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> findByUsername(@PathVariable("username") String username) {
        try {
            return userService.findByUsername(username)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
