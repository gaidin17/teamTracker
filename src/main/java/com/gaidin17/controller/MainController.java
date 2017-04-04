package com.gaidin17.controller;


import com.gaidin17.domain.User;
import com.gaidin17.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Gaidin on 11.02.2017.
 */
@RestController
public class MainController {
    private String testQuery = "pid=18&lat=1233124.21&lon=1443224.99&time=32489723";
    private UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/position")
    public List<User> getAndUpdatePositions(@RequestParam("pid") int pid, @RequestParam("lat") float lat, @RequestParam("lon") float lon, @RequestParam("time") long time) {
        if (lat != 0 && lon != 0) {
            userService.updateUserposition(pid, lat, lon, time);
        }
        List<User> users = userService.getUsersExceptId(pid);
        return users;
    }

    @RequestMapping("/auth")
    public int initialiseUser(@RequestParam("name") String name, @RequestParam("deviceId") String deviceId) {
        User user = userService.getUserByDeviceId(deviceId);
        if (user == null) {
            if (userService.isUserNameExist(name)) {
                throw new TheSameUserNameException(name);
            }
            user = userService.createUser(name, deviceId);
        }
        return user.getUserId();
    }

    @RequestMapping("/getAll")
    public List<User> initialiseUser() {
        return userService.getAllUsers();
    }

    @RequestMapping("/secretRemoveUser")
    public boolean removeUser(@RequestParam("pid") int pid) {
        return userService.removeUserById(pid);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    class TheSameUserNameException extends RuntimeException {
        public TheSameUserNameException(String name) {
            super("User with name '" + name + "' is already exist");
        }
    }
}
