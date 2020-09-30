package com.course.eugen.api;

import com.course.eugen.domain.Train;
import com.course.eugen.dto.AssignUserToTrainDTO;
import com.course.eugen.dto.ChangeUserRoleDTO;
import com.course.eugen.dto.CreateUserDTO;
import com.course.eugen.dto.GroupDTO;
import com.course.eugen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public BigInteger createUser(@RequestBody CreateUserDTO createUserDTO) {
        return userService.createUser(createUserDTO);
    }

    @PostMapping("/change/role")
    @ResponseStatus(HttpStatus.OK)
    public void changeUserRole(@RequestBody ChangeUserRoleDTO changeUserRoleDTO) {
        userService.changeRole(changeUserRoleDTO);
    }

    @PostMapping("/join/to/train")
    @ResponseStatus(HttpStatus.OK)
    public void joinToTrain(@RequestBody AssignUserToTrainDTO assignUserToTrainDTO) {
        userService.assignUserToTrain(assignUserToTrainDTO);
    }

    @GetMapping("/{userId}/groups")
    public List<GroupDTO> loadGroups(@PathVariable BigInteger userId) {
        return userService.getGroups(userId);
    }

    @GetMapping("/{userId}/trains")
    public List<Train> loadTrains(@PathVariable BigInteger userId) {
        return userService.getTrains(userId);
    }
}
