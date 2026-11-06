package org.rewear.service;

import org.mindrot.jbcrypt.BCrypt;
import org.rewear.model.User;
import org.rewear.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || plainPassword == null) return false;
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            // Handle Werkzeug scrypt or pbkdf2 or legacy hashes if any
            return plainPassword.equals(hashedPassword);
        }
    }

    @Transactional
    public User registerUser(String name, String email, String plainPassword, String phone, String location, String bio) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("An account with this email address already exists.");
        }
        String hashed = hashPassword(plainPassword);
        User user = new User(name, email, hashed, phone, location, bio, false);
        return userRepository.save(user);
    }

    public Optional<User> authenticate(String email, String plainPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isActive() && verifyPassword(plainPassword, user.getPasswordHash())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public boolean toggleActive(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!user.isAdmin()) {
                user.setActive(!user.isActive());
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
