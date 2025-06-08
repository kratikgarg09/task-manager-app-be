package com.kratik.task_manager.services;

import com.kratik.task_manager.dto.UserDTO;
import com.kratik.task_manager.dto.UserUpdateDTO;
import com.kratik.task_manager.model.UserEntity;
import com.kratik.task_manager.repository.UserRepository;
import com.kratik.task_manager.utility.CommonFunctions;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CommonFunctions commonFunctions;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserDTO getCurrentUSerProfile(){
        return commonFunctions.getUserDtoByUser();
    }

    public UserDTO updateUserProfile(UserUpdateDTO dto){
        UserEntity user = commonFunctions.getCurrentUser();
        if(dto.getMobileNumber() != null) user.setMobileNumber(dto.getMobileNumber());
        if (dto.getName()!=null && !dto.getName().isBlank()) user.setName(dto.getName());
        if (dto.getNewPassword()!=null && !dto.getNewPassword().isBlank()) user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        if (dto.getEmailId()!=null && !dto.getEmailId().isBlank()) user.setEmailId(dto.getEmailId());
        userRepository.save(user);
        return commonFunctions.getUserDtoByUser();
    }

}

