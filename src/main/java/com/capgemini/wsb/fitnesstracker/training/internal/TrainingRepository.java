package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findAllByUserId(Long id);
    List<Training> findAllByEndTimeIsAfter(Date date);
    List<Training> getAllTrainingsByActivityType(ActivityType activity);
}
