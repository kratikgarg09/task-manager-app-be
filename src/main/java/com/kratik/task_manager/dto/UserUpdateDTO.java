package com.kratik.task_manager.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String newPassword;
    private String name;
    private String mobileNumber;
    private String emailId;
}

