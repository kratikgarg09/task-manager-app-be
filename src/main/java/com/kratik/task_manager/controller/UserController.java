package com.kratik.task_manager.controller;

import com.kratik.task_manager.dto.UserDTO;
import com.kratik.task_manager.dto.UserUpdateDTO;
import com.kratik.task_manager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public UserDTO getProfile(){
        return userService.getCurrentUSerProfile();
    }

    @PutMapping("/update/profile")
    public UserDTO updateProfile(@RequestBody UserUpdateDTO userUpdateDTO){
        return userService.updateUserProfile(userUpdateDTO);
    }

}
