package com.vhung.studentmanager.service;

import com.vhung.studentmanager.dto.request.UserRequestDTO;
import com.vhung.studentmanager.dto.response.UserResponseDTO;
import com.vhung.studentmanager.entity.Role;
import com.vhung.studentmanager.entity.User;
import com.vhung.studentmanager.exception.AppException;
import com.vhung.studentmanager.repository.UserReposistory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserReposistory userReposistory;
    private final PasswordEncoder passwordEncoder;
    //Thêm user
    public UserResponseDTO create(UserRequestDTO request){
        if(!request.getRole().equals("ADMIN") && !request.getRole().equals("TEACHER") && !request.getRole().equals("STUDENT")){
            throw new AppException(HttpStatus.BAD_REQUEST, "Vai trò không hợp lệ");
        }
        if(request.getPassWord().length() < 8 ) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Mật khẩu ít nhất 8 kí tự");
        }
        if(userReposistory.existsUserByUserName(request.getUserName())){
            throw new AppException(HttpStatus.CONFLICT, "Đã tồn tại tên người dùng");
        }
        User user = new User();

        user.setUserName(request.getUserName());
        user.setRole(Role.valueOf(request.getRole()));
        user.setPassword(passwordEncoder.encode(request.getPassWord()));
        user.setIsDeleted(false);

        User userSave = userReposistory.save(user);

        return toDTO(userSave);
    }

    private UserResponseDTO toDTO(User user) {
        UserResponseDTO data = UserResponseDTO.builder()
                .id(user.getId())
                .role(user.getRole())
                .userName(user.getUserName())
                .isActive(user.getIsDeleted())
                .createdAt(user.getCreatedAt())
                .build();

        return data;
    }

}
