package ru.dcp.gamedev.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.repository.JpaUserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JpaUserRepository userRepository;

    public User getOrCreate(int userId, String name, String username, String surname) {

        updateUser(userId, name, username, surname);

        return get(userId)
                .orElseGet(() -> userRepository.save(new User(userId, name, username, surname)));
    }

    public Optional<User> get(int userId) {
        return userRepository.getByUserId(userId);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    private void updateUser (int userId, String name, String username, String surname) {
        Optional<User> userToUpdate = get(userId);

        if (userToUpdate.isPresent()) {
            User updatedUser = userToUpdate.get();
            updatedUser.setName(name);
            updatedUser.setSurname(surname);
            updatedUser.setUsername(username);

            userRepository.save(updatedUser);
        }
    }

}
