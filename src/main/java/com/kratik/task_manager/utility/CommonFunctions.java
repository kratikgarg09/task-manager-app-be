package com.kratik.task_manager.utility;

import com.kratik.task_manager.dto.UserDTO;
import com.kratik.task_manager.model.UserEntity;

public class CommonFunctions {
    public UserDTO getUserDtoByUser(UserEntity user){
          return new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getName(),
                    user.getEmailId(),
                    user.getMobileNumber()
          );
    }
}
