package com.course.eugen.api;

import com.course.eugen.domain.Train;
import com.course.eugen.domain.User;
import com.course.eugen.dto.CreateTrainDTO;
import com.course.eugen.dto.MarkUserDTO;
import com.course.eugen.service.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.error.Mark;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/train")
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;

    @PostMapping("/create")
    public Train createTrain(@RequestBody CreateTrainDTO createTrainDTO) {
        return trainService.createTraining(createTrainDTO);
    }

    @PatchMapping("/mark")
    public void markUser(@RequestBody MarkUserDTO markUserDTO){
        trainService.markUser(markUserDTO);
    }

    @GetMapping("/visitors/{trainId}")
    public List<User> getVisitors(@PathVariable BigInteger trainId){
        return trainService.getVisitors(trainId);
    }
}
