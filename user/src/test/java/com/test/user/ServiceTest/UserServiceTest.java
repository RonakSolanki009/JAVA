package com.test.user.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import com.test.user.model.UserDetails;
import com.test.user.repository.UserRepository;
import com.test.user.service.UserService;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private UserService userService;

    @Test
    public void testCreateUserSuccess() {
        UserDetails user = new UserDetails();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        when(userRepository.save(any(UserDetails.class))).thenReturn(user);
        UserDetails createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        verify(kafkaTemplate, times(1)).send("user-topic", "User Created: " + user.getId());
    }

    @Test
    public void testCreateUserFailure() {
        when(userRepository.save(any(UserDetails.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            userService.createUser(new UserDetails());
        });

        verify(kafkaTemplate, times(0)).send(anyString(), anyString());
    }
}