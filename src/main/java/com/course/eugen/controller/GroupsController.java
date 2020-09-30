package com.course.eugen.controller;

import com.course.eugen.domain.Group;
import com.course.eugen.dto.CreateGroupDTO;
import com.course.eugen.repository.GroupRepository;
import com.course.eugen.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupsController {

    private final GroupService groupService;
    private final GroupRepository groupRepository;

    @PostMapping("/create")
    public void createGroup(@RequestBody CreateGroupDTO createGroupDTO) {
        groupService.createGroup(createGroupDTO);
    }

    @PostMapping("/join/{userId}/to/group/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    public void joinToGroup(@PathVariable BigInteger groupId, @PathVariable BigInteger userId) {
        groupService.joinUserToGroup(groupId, userId);
    }

    @DeleteMapping("/remove/{userId}/from/group/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFromGroup(@PathVariable BigInteger groupId, @PathVariable BigInteger userId) {
        groupService.leaveGroup(userId, groupId);
    }

    @GetMapping("/{groupId}")
    public Group getById(@PathVariable BigInteger groupId){
        return groupRepository.findById(groupId).orElseThrow(EntityNotFoundException::new);
    }

}
