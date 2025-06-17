package com.example.authCrud.services;

import com.example.authCrud.DTO.UserDTO;
import com.example.authCrud.DTO.UserResponseDTO;
import com.example.authCrud.entities.User;
import com.example.authCrud.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO registerUser(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(user);

        return new UserResponseDTO(saved.getId(), saved.getName(), saved.getEmail());
    }
}
