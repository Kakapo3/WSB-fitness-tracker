package com.capgemini.wsb.fitnesstracker.user.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface (API) for modifying operations on {@link User} entities through the API.
 * Implementing classes are responsible for executing changes within a database transaction, whether by continuing an existing transaction or creating a new one if required.
 */
public interface UserService {

    User getUserById(Long id);

    User createUser(User user);

    void deleteUser(Long userId);

    List<User> getOlderUsers(LocalDate date);

    User updateUser(Long userId, User user);

}
