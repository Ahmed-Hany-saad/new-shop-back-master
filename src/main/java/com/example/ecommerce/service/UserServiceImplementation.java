package com.example.ecommerce.service;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.exception.UserException;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    private UserRepository userRepository;
    private JwtProvider jwtProvider;
    public UserServiceImplementation(UserRepository userRepository ,JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }
    @Override
    public User findUserByld(Long userld) throws UserException {
        Optional<User> user = userRepository.findById(userld);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserException("User not found with id " + userld);
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email= jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UserException("User not found with email " + email);
        }
        return user;
    }
}
