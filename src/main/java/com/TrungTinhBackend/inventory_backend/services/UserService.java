package com.TrungTinhBackend.inventory_backend.services;

import com.TrungTinhBackend.inventory_backend.dto.LoginRequest;
import com.TrungTinhBackend.inventory_backend.dto.RegisterRequest;
import com.TrungTinhBackend.inventory_backend.dto.Response;
import com.TrungTinhBackend.inventory_backend.dto.UserDTO;
import com.TrungTinhBackend.inventory_backend.models.User;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUser();
    User getCurrentLoggedInUser();
    Response getUserById(Long id);
    Response updateUser(Long id, UserDTO userDTO);
    Response deleteUser(Long id);
    Response getUserTransactions(Long id);

}
