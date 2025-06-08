package com.kratik.task_manager.utility;

import com.kratik.task_manager.dto.TaskResponseDTO;
import com.kratik.task_manager.dto.UserDTO;
import com.kratik.task_manager.model.TasksEntity;
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

    public TaskResponseDTO getTaskResponseDto (TasksEntity tasks,UserEntity user){
        return new TaskResponseDTO(
                tasks.getId(),
                tasks.getTitle(),
                tasks.getDescription(),
                tasks.getDueDate(),
                tasks.isCompleted(),
               getUserDtoByUser(user)
        );
    }

}
