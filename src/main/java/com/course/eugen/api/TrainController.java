package com.course.eugen.api;

import com.course.eugen.domain.Train;
import com.course.eugen.dto.CreateTrainDTO;
import com.course.eugen.service.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/train")
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;

    @PostMapping("/create")
    public Train createTrain(@RequestBody CreateTrainDTO createTrainDTO) {
        return trainService.createTraining(createTrainDTO);
    }

}
