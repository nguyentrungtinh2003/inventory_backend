package com.TrungTinhBackend.inventory_backend.services.impl;

import com.TrungTinhBackend.inventory_backend.dto.*;
import com.TrungTinhBackend.inventory_backend.enums.UserRole;
import com.TrungTinhBackend.inventory_backend.exceptions.InvalidCredentialsException;
import com.TrungTinhBackend.inventory_backend.exceptions.NotFoundException;
import com.TrungTinhBackend.inventory_backend.models.User;
import com.TrungTinhBackend.inventory_backend.repositories.UserRepository;
import com.TrungTinhBackend.inventory_backend.security.JwtUtils;
import com.TrungTinhBackend.inventory_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;


    @Override
    public Response registerUser(RegisterRequest registerRequest) {
        UserRole role = UserRole.MANAGER;
        if(registerRequest.getRole() != null) {
            role = registerRequest.getRole();
        }

        User userToSave = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();
        userRepository.save(userToSave);

        return Response.builder()
                .status(200)
                .message("User register success !")
                .data(userToSave)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->new NotFoundException("Email not found !"));
        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())) {
            throw new InvalidCredentialsException("Password does not match");
        }
        String token = jwtUtils.generateToken(user.getEmail());

        return Response.builder()
                .status(200)
                .message("User login success !")
                .token(token)
                .data(user)
                .role(user.getRole())
                .expirationTime("6 months")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public Response getAllUser() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        users.forEach(user -> user.setTransactions(null));
        List<UserDTO> userDTOS = modelMapper.map(users,new TypeToken<List<UserDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("Get all user success !")
                .data(userDTOS)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        user.setTransactions(null);

        return user;
    }

    @Override
    public Response getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found "));
        UserDTO userDTO = modelMapper.map(user,UserDTO.class);
        userDTO.setTransactions(null);
        return Response.builder()
                .status(200)
                .message("Get user by id success !")
                .data(userDTO)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public Response updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found "));
        if(userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if(userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if(userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if(userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        }
        if(userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userRepository.save(user);

        return Response.builder()
                .status(200)
                .message("Update user success !")
                .data(user)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public Response deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found "));
        userRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Delete user success !")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public Response getUserTransactions(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found "));
        UserDTO userDTO = modelMapper.map(user,UserDTO.class);
        userDTO.getTransactions().forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setSupplier(null);
        });
        return Response.builder()
                .status(200)
                .message("Get transaction user success !")
                .data(userDTO)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
