package com.codingtrainers.etraining.services;


import com.codingtrainers.etraining.dto.output.UserResponseDTO;
import com.codingtrainers.etraining.entities.Role;
import com.codingtrainers.etraining.entities.User;
import com.codingtrainers.etraining.repositories.UserRepository;
import com.codingtrainers.etraining.utils.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserResponseDTO> getAll() {
        return userRepository.findAllByActiveTrueExceptSuper()
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getById(Long id) {
        User user = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        return new UserResponseDTO(user);
    }
    public void update(User user) {
        user.setActive(true);
        if (user.getRole() == null) {
            user.setRole(Role.PUPIL);
        }
        userRepository.save(user);
    }

    public Optional<UserResponseDTO> findByUsername(String username) {
        return userRepository.findByUsernameAndActiveTrue(username)
                .map(UserResponseDTO::new);
    }

    public void create(User user) {
        String password = user.getPassword();
        user.setActive(true);
        password = HashUtils.sha256(password);
        user.setPassword(password);
         userRepository.save(user);
    }

    public void deleteUser (Long id) {
        User user = userRepository.findByIdAndActiveTrue(id).orElseThrow(()-> new RuntimeException("User Not Found"));
        user.setActive(false);
        userRepository.save(user);
    }
    public void activateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User Not Found"));
        user.setActive(true);
        userRepository.save(user);
    }
    public List<UserResponseDTO> getInactiveUsers() {
        return userRepository.findAllByActiveFalse()
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }
    public User findUserByUsernameAndPassword(String username, String password) throws Exception {
        return userRepository.userLogin(username, password).stream().findFirst().orElseThrow(() ->
                new Exception("User not found by username and password")
        );
    }


}
