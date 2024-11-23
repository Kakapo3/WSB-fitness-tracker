package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingDTO;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNoUserDTO;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import com.capgemini.wsb.fitnesstracker.user.internal.UserMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
class TrainingController {

    private final UserMapper userMapper;
    @PersistenceContext
    private EntityManager entityManager;

    private final TrainingServiceImpl trainingService;
    private final TrainingMapper trainingMapper;
    private final UserService userService;

    public boolean isManaged(User user) {
        return entityManager.contains(user);
    }

    @GetMapping
    public List<TrainingDTO> getAllTrainings() {
        return trainingService.findAllTrainings()
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @GetMapping("/{userId}")
    public List<TrainingDTO> getAllTrainingsForUser(@PathVariable Long userId) {
        return trainingService.getAllTrainingsForUser(userId)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @GetMapping("/finished/{date}")
    public List<TrainingDTO> getAllTrainingsFinishedAfterTime(@PathVariable String date) throws ParseException {
        String[] formats = {
                "yyyy-MM-dd'T'HH:mm:ss.SSS+00:00", // Full date-time with timezone
                "yyyy-MM-dd"                   // Just the date
        };

        Date endDate = null;
        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                endDate = sdf.parse(date);
            } catch (ParseException ignored) {}
        }

        if (endDate == null) {
            throw new ParseException(date, 0);
        }

        return trainingService.getTrainingsByEndTime(endDate)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @GetMapping("/type/{activity}")
    public List<TrainingDTO> getAllTrainingsByActivityType(@PathVariable String activity) {
        return trainingService.getAllTrainingsByActivityType(ActivityType.valueOf(activity.toUpperCase()))
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<TrainingDTO> addTraining(@RequestBody TrainingNoUserDTO trainingDtoNoUser) {
        User user = userService.getUserById(trainingDtoNoUser.getUserId());
        if (user == null) { throw new EntityNotFoundException("User with ID " + trainingDtoNoUser.getUserId() + " not found."); }
        TrainingDTO trainingDto = trainingMapper.toDto(trainingDtoNoUser, user);
        Training training = trainingMapper.toEntity(trainingDto);
        Training savedTraining = trainingService.createTraining(training);
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingMapper.toDto(savedTraining));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingDTO> updateTraining(@PathVariable Long id, @RequestBody TrainingNoUserDTO trainingDtoNoUser) {
        User user = null;
        if (trainingDtoNoUser.getUserId() != null) {
            user = userService.getUserById(trainingDtoNoUser.getUserId());
        }
        TrainingDTO trainingDto = trainingMapper.toDto(trainingDtoNoUser, user);
        Training training = trainingMapper.toEntity(trainingDto);
        Training updatedTraining = trainingService.updateTraining(id, training);
        return ResponseEntity.status(HttpStatus.OK).body(trainingMapper.toDto(updatedTraining));
    }

    @GetMapping("/activityType")
    public ResponseEntity<List<TrainingDTO>> getAllTrainingByActivityType(@RequestParam ActivityType activityType) {
        List<TrainingDTO> trainingDTOS = trainingService.getAllTrainingsByActivityType(activityType).stream().map(trainingMapper::toDto).toList();
        return ResponseEntity.ok(trainingDTOS);
    }

}
