package com.course.eugen.service;

import com.course.eugen.config.AppBusinessException;
import com.course.eugen.domain.Train;
import com.course.eugen.domain.enums.RolesEnum;
import com.course.eugen.dto.CreateTrainDTO;
import com.course.eugen.repository.GroupRepository;
import com.course.eugen.repository.RoleModelRepository;
import com.course.eugen.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainService {

    private final TrainRepository trainRepository;
    private final GroupRepository groupRepository;
    private final RoleModelRepository roleModelRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Train createTraining(@Validated CreateTrainDTO createTrainDTO) {
        checkCorrectBeginning(createTrainDTO.getBeginTime());
        var groupOpt = groupRepository.findById(createTrainDTO.getGroupId());
        if (groupOpt.isPresent()) {
            var users = groupOpt.get()
                    .getUsers()
                    .stream()
                    .filter(user -> user.getId().equals(createTrainDTO.getCreatorId()))
                    .collect(Collectors.toList());
            List<RolesEnum> roles = users.stream()
                    .map(user -> roleModelRepository.findByUserAndGroup(user, groupOpt.get()).get().getRole())
                    .collect(Collectors.toList());
            if (roles.contains(RolesEnum.TEACHER)) {
                var train = Train.builder()
                        .beginTime(createTrainDTO.getBeginTime())
                        .group(groupOpt.get())
                        .build();
                return trainRepository.save(train);
            } else {
                log.error("Incorrect access! User does'not have right to create new train!");
                throw new AppBusinessException("Incorrect access! User does'not have right to create new train!");
            }
        }
        log.error("Group with id {} not found in registry!", createTrainDTO.getGroupId());
        throw new EntityNotFoundException(format("Group with id %s not found in registry!", createTrainDTO.getGroupId()));
    }


    private void checkCorrectBeginning(Timestamp trainBeginning) {
        var now = LocalDateTime.now();
        var local = trainBeginning.toLocalDateTime();
        long diff = Duration.between(local, now).toHours();
        if (diff > 10) {
            throw new AppBusinessException("Unable to create training. Time between now and beginning is less then 10 hours!");
        }
    }
}
