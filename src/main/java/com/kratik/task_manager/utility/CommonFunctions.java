package com.kratik.task_manager.utility;

import com.kratik.task_manager.dto.TaskResponseDTO;
import com.kratik.task_manager.dto.UserDTO;
import com.kratik.task_manager.model.TasksEntity;
import com.kratik.task_manager.model.UserEntity;
import com.kratik.task_manager.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class CommonFunctions {

    private final UserRepository userRepository;

    public CommonFunctions(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("UserEntity not found"));
    }
    public UserDTO getUserDtoByUser(){
        UserEntity user = getCurrentUser();
          return new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getName(),
                    user.getEmailId(),
                    user.getMobileNumber()
          );
    }

    public TaskResponseDTO getTaskResponseDto (TasksEntity tasks){
        return new TaskResponseDTO(
                tasks.getId(),
                tasks.getTitle(),
                tasks.getDescription(),
                tasks.getDueDate(),
                tasks.getPriority(),
                tasks.isCompleted(),
                tasks.getReminderTime(),
                tasks.getStatus(),
                getUserDtoByUser()
        );
    }

}
