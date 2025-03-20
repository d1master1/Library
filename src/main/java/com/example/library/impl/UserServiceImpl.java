package com.example.library.impl;

import com.example.library.dto.UserDto;
import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.repo.UserRepo;
import com.example.library.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepo userRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void save(UserDto userdto) {
        User user = new User();
        user.setUsername(userdto.getUsername());
        user.setPassword(encoder.encode(userdto.getPassword()));
        user.setName(userdto.getName() + " " + userdto.getSurname());
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);
    }

    @Override
    public User findUser(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return userRepo.existsByUsername(username);
    }

    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        this.userRepo.deleteById(id);
    }

    @Override
    public void editUser(User user) {
        User editedUser = userRepo.findById(user.getId()).orElse(null);
        if(editedUser == null) {
            throw new RuntimeException("Пользователь не найден");
        }
        editedUser.setUsername(user.getUsername());
        editedUser.setPassword(encoder.encode(user.getPassword()));
        editedUser.setName(user.getName() + " " + user.getSurname());
        editedUser.setRoles(user.getRoles());
        userRepo.save(editedUser);
    }
}
