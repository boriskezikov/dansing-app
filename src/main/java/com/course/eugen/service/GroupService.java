package com.course.eugen.service;

import com.course.eugen.config.AppBusinessException;
import com.course.eugen.domain.Group;
import com.course.eugen.domain.RoleModel;
import com.course.eugen.domain.User;
import com.course.eugen.domain.enums.RolesEnum;
import com.course.eugen.dto.CreateGroupDTO;
import com.course.eugen.repository.GroupRepository;
import com.course.eugen.repository.RoleModelRepository;
import com.course.eugen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;
import java.util.HashSet;

import static java.lang.String.format;

@Service(value = "GroupService")
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    public final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final RoleModelRepository roleModelRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createGroup(@Validated CreateGroupDTO createGroupDTO) {
        if (groupRepository.existsByName(createGroupDTO.getName())) {
            throw new AppBusinessException(format("Group with name %s already exists", createGroupDTO.getName()));
        }
        userRepository.findById(createGroupDTO.getCreatorId()).ifPresentOrElse(user -> {
            if (user.getGlobalRole() != RolesEnum.ADMIN) {
                throw new AppBusinessException(format("Incorrect access rules! User %s is not in admin list!", user.getId()));
            }
            var group = groupRepository.save(Group.builder()
                    .name(createGroupDTO.getName())
                    .createdBy(createGroupDTO.getCreatorId())
                    .build());
            log.info("Group {} created by admin!", group.getId());
            var role = RoleModel.builder()
                    .user(user)
                    .role(RolesEnum.TEACHER)
                    .group(group)
                    .build();
            roleModelRepository.save(role);
            var users = new HashSet<User>();
            users.add(user);
            group.setUsers(users);
            groupRepository.save(group);
            log.info("Role {} created for user {} in group {}", role.getRole(), role.getUser().getId(), group.getId());
        }, () -> {
            log.error("User with id {} not found in registry!", createGroupDTO.getCreatorId());
            throw new EntityNotFoundException(String.format("User with id %s not found in registry!", createGroupDTO.getCreatorId()));
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void joinUserToGroup(BigInteger groupId, BigInteger userId) {
        groupRepository.findById(groupId).ifPresentOrElse(group ->
                userRepository.findById(userId).ifPresentOrElse(user -> {
                    if (group.getUsers().contains(user)) {
                        throw new AppBusinessException(format("User {%s} already joined to group {%s}", user.getId(), group.getId()));
                    }
                    group.getUsers().add(user);
                    groupRepository.save(group);
                    log.info("Successfully joined user {} to group {}", user.getId(), group.getId());
                    var role = RoleModel.builder()
                            .group(group)
                            .user(user)
                            .role(RolesEnum.STUDENT)
                            .build();
                    roleModelRepository.save(role);
                    log.info("Successfully granted role {} to user {}", role.getRole(), user.getId());

                }, () -> {
                    log.error("User with id {} not found in registry!", userId);
                    throw new EntityNotFoundException(format("User with id %s not found in registry!", userId));
                }), () -> {
            log.error("Group with id {} not found in registry!", groupId);
            throw new EntityNotFoundException(format("Group with id %s not found in registry!", groupId));
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void leaveGroup(BigInteger userId, BigInteger groupId) {
        groupRepository.findById(groupId).ifPresentOrElse(group ->
                userRepository.findById(userId).ifPresentOrElse(user -> {
                    if (!group.getUsers().contains(user)) {
                        throw new AppBusinessException(format("User {%s} is not assigned to group {%s}!", user.getId(), group.getId()));
                    }
                    group.getUsers().remove(user);
                    groupRepository.save(group);
                    log.info("Successfully removed user {} from group {}", user.getId(), group.getId());
                    roleModelRepository.findByUserAndGroup(user, group).ifPresent(roleModelRepository::delete);
                }, () -> {
                    log.error("User with id {} not found in registry!", userId);
                    throw new EntityNotFoundException(format("User with id %s not found in registry!", userId));
                }), () -> {
            log.error("Group with id {} not found in registry!", groupId);
            throw new EntityNotFoundException(format("Group with id %s not found in registry!", groupId));
        });
    }
}
