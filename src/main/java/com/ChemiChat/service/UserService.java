package com.ChemiChat.service;

import com.ChemiChat.exception.EmailAlreadyExistException;
import com.ChemiChat.exception.UserAlreadyExistException;
import com.ChemiChat.model.User;
import com.ChemiChat.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private String encryptPassword(String password){
        return new BCryptPasswordEncoder().encode(password);
    }

    public User findUserByUserName(String userName){
        return userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("Could not found a user with given name"));
    }

    public void registerNewUserAccount(User user) throws UserAlreadyExistException {
        if (userExists(user.getUsername())) {
            throw new UserAlreadyExistException("User with username: '"
                    + user.getUsername() + "' is already exist!");
        }

        if (emailExists(user.getEmail())) {
            throw new EmailAlreadyExistException("User with email: '"
                    + user.getEmail() + "' is already exist!");
        }

        user.setPassword(encryptPassword(user.getPassword()));

        userRepository.save(user);
    }

    public void deleteByUsername(String username){
        userRepository.deleteByUsername(username);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
