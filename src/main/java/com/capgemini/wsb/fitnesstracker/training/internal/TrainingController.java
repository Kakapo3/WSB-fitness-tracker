package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.TrainingDTO;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNoUserDTO;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletRequest;
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

    private final TrainingServiceImpl trainingService;
    private final TrainingMapper trainingMapper;
    private final UserService userService;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+00:00");
        Date endDate = sdf.parse(date);
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
        User user = userService.getUserById(trainingDtoNoUser.getUser_id());
        TrainingDTO trainingDto = trainingMapper.toDtoWithUser(trainingDtoNoUser, user);
        System.out.println("Created " + trainingDto.getActivityType() + " for user " + trainingDto.getUser().firstName());
        System.out.println(trainingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingMapper.toDto(trainingService.createTraining(trainingMapper.toEntity(trainingDto))));
    }

}
