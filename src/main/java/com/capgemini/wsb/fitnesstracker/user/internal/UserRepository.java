package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Query searching users by email address. It matches by exact match.
     *
     * @param email email of the user to search
     * @return {@link Optional} containing found user or {@link Optional#empty()} if none matched
     */
    default List<User> findByEmail(String email) {
        return findAll().stream()
                        .filter(user -> Objects.equals(user.getEmail(), email)).toList();
    }

    default List<User> findByEmailLike(String email) {
        return findAll().stream()
                .filter(user -> user.getEmail().toLowerCase().contains(email.toLowerCase())).toList();
    }

    List<User> getUsersByBirthdateLessThan(LocalDate birthdate);

    default User updateUser(Long id, User user) {
        User userFromDB = findById(id).orElse(null);
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
            save(userFromDB);
        } else {
            throw new UserNotFoundException(id);
        }
        return userFromDB;
    }
}
