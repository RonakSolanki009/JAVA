package com.test.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.test.user.exception.ResourceNotFoundException;
import com.test.user.model.UserDetails;
import com.test.user.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public UserDetails createUser(UserDetails user) {
        UserDetails savedUser = userRepository.save(user);
        kafkaTemplate.send("user-topic", "User Created: " + savedUser.getName());
        return savedUser;
    }

    public UserDetails updateUser(Long id, UserDetails userDetails) {
        UserDetails user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        UserDetails updatedUser = userRepository.save(user);
        kafkaTemplate.send("user-topic", "User Updated: " + updatedUser.getId());
        return updatedUser;
    }

    public void deleteUser(Long id) {
        UserDetails user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
        kafkaTemplate.send("user-topic", "User Deleted: " + user.getId());
    }
}

