package com.course.eugen.service;

import com.course.eugen.config.AppBusinessException;
import com.course.eugen.domain.Group;
import com.course.eugen.domain.RoleModel;
import com.course.eugen.domain.Train;
import com.course.eugen.domain.User;
import com.course.eugen.domain.enums.RolesEnum;
import com.course.eugen.dto.AssignUserToTrainDTO;
import com.course.eugen.dto.ChangeUserRoleDTO;
import com.course.eugen.dto.CreateUserDTO;
import com.course.eugen.dto.GroupDTO;
import com.course.eugen.repository.GroupRepository;
import com.course.eugen.repository.RoleModelRepository;
import com.course.eugen.repository.TrainRepository;
import com.course.eugen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@SuppressWarnings("ALL")
@Slf4j
@Service(value = "UserService")
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RoleModelRepository roleModelRepository;
    private final TrainRepository trainRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BigInteger createUser(@Validated CreateUserDTO createUserDTO) {
        var user = User.builder()
                .firstName(createUserDTO.getFirstName())
                .lastName(createUserDTO.getLastName())
                .mail(createUserDTO.getMail())
                .password(createUserDTO.getPassword())
                .globalRole(RolesEnum.PLAIN_USER)
                .build();
        return userRepository.save(user).getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void changeRole(@Validated ChangeUserRoleDTO changeUserRoleDTO) {
        userRepository.findById(changeUserRoleDTO.getUserId()).ifPresentOrElse(user -> {
            groupRepository.findById(changeUserRoleDTO.getGroupId()).ifPresentOrElse(group -> {
                if (group.getUsers().contains(user)) {
                    roleModelRepository.findByUserAndGroup(user, group).ifPresentOrElse(roleModel -> {
                        roleModel.setRole(changeUserRoleDTO.getRole());
                        log.info("Successfully changed user {} role to {} in group {}", user.getId(), changeUserRoleDTO.getRole(), group.getId());
                    }, () -> {
                        var role = RoleModel.builder()
                                .role(changeUserRoleDTO.getRole())
                                .group(group)
                                .user(user)
                                .build();
                        roleModelRepository.save(role);
                        log.info("Successfully created user {} role: {} in group {}", user.getId(), changeUserRoleDTO.getRole(), group.getId());
                    });
                }
            }, () -> {
                log.error("Group with id {} not found in registry!", changeUserRoleDTO.getGroupId());
                throw new EntityNotFoundException(format("Group with id %s not found in registry!", changeUserRoleDTO.getGroupId()));
            });
        }, () -> {
            log.error("User with id {} not found in registry!", changeUserRoleDTO.getUserId());
            throw new EntityNotFoundException(format("User with id %s not found in registry!", changeUserRoleDTO.getUserId()));
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void assignUserToTrain(@Validated AssignUserToTrainDTO assignUserToTrainDTO) {
        trainRepository.findById(assignUserToTrainDTO.getTrainId()).ifPresentOrElse(train -> {
            checkTrainingTime(train.getBeginTime());
            userRepository.findById(assignUserToTrainDTO.getUserId()).ifPresentOrElse(user -> {
                var group = train.getGroup();
                var students = group.getUsers();
                if (assignUserToTrainDTO.getRole() != null) {
                    changeRole(ChangeUserRoleDTO.builder()
                            .groupId(group.getId())
                            .role(assignUserToTrainDTO.getRole())
                            .userId(assignUserToTrainDTO.getUserId())
                            .build());
                }
                students.add(user);
                train.setGroup(group);
                trainRepository.save(train);
                log.info("User {} added to training {} ", assignUserToTrainDTO.getUserId(), assignUserToTrainDTO.getTrainId());
            }, () -> {
                log.error("User with id {} not found in registry!", assignUserToTrainDTO.getUserId());
                throw new EntityNotFoundException(format("User with id %s not found in registry!", assignUserToTrainDTO.getUserId()));
            });
        }, () -> {
            log.error("Training with id {} not found in registry!", assignUserToTrainDTO.getTrainId());
            throw new EntityNotFoundException(format("Training with id %s not found in registry!", assignUserToTrainDTO.getTrainId()));
        });
    }

    public List<GroupDTO> getGroups(BigInteger userId) {
        var userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            var groups = groupRepository.findByUsersContains(userOpt.get());
            return groups.stream().map(group -> {
                var roleOpt = roleModelRepository.findByUserAndGroup(userOpt.get(), group);
                if (roleOpt.isPresent()) {
                    return GroupDTO.builder()
                            .id(group.getId())
                            .name(group.getName())
                            .createdBy(group.getCreatedBy())
                            .updated(group.getUpdated())
                            .role(roleOpt.get().getRole())
                            .build();
                } else {
                    throw new EntityNotFoundException(
                            format("Internal server error! No role found for user %s and group %s !", userId, group.getId()));
                }
            }).collect(Collectors.toList());
        }
        log.error("User with id {} not found in registry!", userId);
        throw new EntityNotFoundException(format("User with id %s not found in registry!", userId));
    }


    public List<Train> getTrains(BigInteger userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Group> userGroups = groupRepository.findAll().stream().filter(group -> group.getUsers().contains(user.get()))
                    .collect(Collectors.toList());
            List<Train> trains = trainRepository.findAll().stream().filter(train -> userGroups.contains(train.getGroup()))
                    .collect(Collectors.toList());
            return trains;
        } else {
            log.error("User with id {} not found in registry!", userId);
            throw new EntityNotFoundException(format("User with id %s not found in registry!", userId));
        }
    }

    private void checkTrainingTime(Timestamp trainBeginningTime) {
        var now = LocalDateTime.now();
        var local = trainBeginningTime.toLocalDateTime();
        long diff = Duration.between(local, now).toMinutes();
        if (diff > 45) {
            throw new AppBusinessException("Unable to join training. Time between now and beginning is less then 45 minutes!");
        }
    }
}
