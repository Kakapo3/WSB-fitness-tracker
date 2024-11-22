package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

// TODO: Provide Impl
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingProvider {

    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<User> getTraining(final Long trainingId) {
        throw new UnsupportedOperationException("Not finished yet");
    }

    @Override
    public List<Training> findAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public List<Training> getAllTrainingsForUser(Long userId) {
        return trainingRepository.findAllByUserId(userId);
    }

    @Override
    public List<Training> getTrainingsByEndTime(Date endTime) {
        return trainingRepository.findAllByEndTimeIsAfter(endTime);
    }

    @Override
    public List<Training> getAllTrainingsByActivityType(ActivityType activity) {
        return trainingRepository.getAllTrainingsByActivityType(activity);
    }

    @Override
    public Training createTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    public Training updateTraining(Long id, Training training) {
        Training trainingFromDB = trainingRepository.findById(id).orElse(null);
        if (trainingFromDB != null) {
            if (training.getActivityType() != null) {
                trainingFromDB.setActivityType(training.getActivityType());
            }
            if (training.getUser() != null) {
                trainingFromDB.setUser(training.getUser());
            }
            if (training.getDistance() != 0.0) {
                trainingFromDB.setDistance(training.getDistance());
            }
            if (training.getStartTime() != null) {
                trainingFromDB.setStartTime(training.getStartTime());
            }
            if (training.getEndTime() != null) {
                trainingFromDB.setEndTime(training.getEndTime());
            }
            if (training.getAverageSpeed() != 0.0) {
                trainingFromDB.setAverageSpeed(training.getAverageSpeed());
            }
            trainingRepository.save(trainingFromDB);
        } else {
            throw new TrainingNotFoundException(id);
        }
        return trainingFromDB;
    }

}
