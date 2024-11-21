package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingRepository;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public User createUser(final User user) {
        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> getUserByEmail(final String email) {
        return userRepository.findAllByEmail(email);
    }

    @Override
    public List<User> getUserByEmailLike(String email) {
        return userRepository.findByEmailLike(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Deleting User {}", userId);
        List<Training> trainings = trainingRepository.findAllByUserId(userId);
        trainingRepository.deleteAll(trainings);
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> getOlderUsers(LocalDate date) {
        return userRepository.getUsersByBirthdateLessThan(date);
    }

    @Override
    public User updateUser(Long id, User user) {
        User userFromDB = userRepository.findById(id).orElse(null);
        if (userFromDB != null) {
            if (user.getFirstName() != null) {
                userFromDB.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null) {
                userFromDB.setLastName(user.getLastName());
            }
            if (user.getEmail() != null) {
                userFromDB.setEmail(user.getEmail());
            }
            if (user.getBirthdate() != null) {
                userFromDB.setBirthdate(user.getBirthdate());
            }
            userRepository.save(userFromDB);
        } else {
            throw new UserNotFoundException(id);
        }
        return userFromDB;
    }
}