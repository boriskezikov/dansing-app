package com.course.eugen.api;

import com.course.eugen.dto.CreateGroupDTO;
import com.course.eugen.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupsController {

    private final GroupService groupService;

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


}
