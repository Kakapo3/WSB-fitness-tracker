package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingDTO;
import com.capgemini.wsb.fitnesstracker.user.internal.UserMapper;
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
}
