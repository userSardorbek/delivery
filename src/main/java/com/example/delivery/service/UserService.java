package com.example.delivery.service;

import com.example.delivery.entity.Role;
import com.example.delivery.entity.RoleNames;
import com.example.delivery.entity.User;
import com.example.delivery.payload.LogInDto;
import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.payload.UserDto;
import com.example.delivery.repository.RoleRepository;
import com.example.delivery.repository.UserRepository;
import com.example.delivery.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    RoleRepository roleRepository;

    public ResponseMessage addUser(String name, String username, String password) {
        boolean exists = userRepository.existsByUsername(username);
        if (exists)
            return new ResponseMessage("Bunday user mavjud", false);
        User user = new User(name, username, passwordEncoder.encode(password));
        user.setRole(roleRepository.findByRoleName(RoleNames.USER));
        User save = userRepository.save(user);
        return new ResponseMessage("User saqlandi", true, save);
    }

    public ResponseMessage logIn(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                   username, password));
            User user = (User)authentication.getPrincipal();
            String token = jwtProvider.generateToken(username, user.getRole());

            return new ResponseMessage("Token", true, token);
        } catch (BadCredentialsException e) {
            return new ResponseMessage("Parol yoki username xato", false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optional = userRepository.findByUsername(username);
        if (optional.isEmpty())
            throw new UsernameNotFoundException(username + " not found");
        return optional.get();
    }
}
