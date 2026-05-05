package com.ordermgmt.users.infrastructure.adapter.out.persistance;

import com.ordermgmt.users.domain.model.User;
import com.ordermgmt.users.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {
    private final UserMongoRepository userMongoRepository;

    @Override
    public User save(User user) {
        UserDocument doc = toDocument(user);
        UserDocument saved = userMongoRepository.save(doc);
        return toDomain(saved);
    }



    @Override
    public Optional<User> findByUsername(String username) {
        return userMongoRepository.findByUsername(username)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userMongoRepository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userMongoRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userMongoRepository.existsByEmail(email);
    }

    private UserDocument toDocument(User user) {
        return UserDocument.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .build();
    }

    private User toDomain(UserDocument doc) {
        return User.builder()
                .id(doc.getId())
                .username(doc.getUsername())
                .email(doc.getEmail())
                .password(doc.getPassword())
                .role(doc.getRole())
                .enabled(doc.isEnabled())
                .build();
    }
}
