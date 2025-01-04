package com.TrungTinhBackend.inventory_backend.security;

import com.TrungTinhBackend.inventory_backend.exceptions.NotFoundException;
import com.TrungTinhBackend.inventory_backend.models.User;
import com.TrungTinhBackend.inventory_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("User email not found"));
        return AuthUser.builder()
                .user(user)
                .build();
    }
}
