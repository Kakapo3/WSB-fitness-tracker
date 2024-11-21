package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingDTO;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNoUserDTO;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {

    UserMapper userMapper;



    @Autowired
    private TrainingMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    TrainingDTO toDto(Training training) {
        return new TrainingDTO(
                training.getId(),
                userMapper.toDto(training.getUser()),
                training.getStartTime(),
                training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed()
                );
    }

    Training toEntity(TrainingDTO trainingDTO) {
        return new Training(
                userMapper.toEntity(trainingDTO.getUser()),
                trainingDTO.getStartTime(),
                trainingDTO.getEndTime(),
                trainingDTO.getActivityType(),
                trainingDTO.getDistance(),
                trainingDTO.getAverageSpeed()
        );
    }

    TrainingDTO toDtoWithUser(TrainingNoUserDTO training, User user) {
        return new TrainingDTO(
                training.getId(),
                userMapper.toDto(user),
                training.getStartTime(),
                training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed()
        );
    }
}
