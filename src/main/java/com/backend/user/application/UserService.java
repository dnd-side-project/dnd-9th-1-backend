package com.backend.user.application;

import com.backend.user.domain.User;
import com.backend.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findUserOrRegister(User uncheckedUser) {
        Optional<User> user =  userRepository.findBySocialId(uncheckedUser.getSocialId());
        return user.orElseGet(() -> userRepository.save(uncheckedUser));
    }
}
