package com.vhung.studentmanager.service;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.vhung.studentmanager.dto.request.UserRequestDTO;
import com.vhung.studentmanager.dto.response.PageResponse;
import com.vhung.studentmanager.dto.response.UserResponseDTO;
import com.vhung.studentmanager.entity.Role;
import com.vhung.studentmanager.entity.User;
import com.vhung.studentmanager.exception.AppException;

import com.vhung.studentmanager.repository.UserReposistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        if(request.getPassword().length() < 8 ) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Mật khẩu ít nhất 8 kí tự");
        }
        if(userReposistory.existsUserByUserName(request.getUserName())){
            throw new AppException(HttpStatus.CONFLICT, "Đã tồn tại tên người dùng");
        }
        User user = new User();

        user.setUserName(request.getUserName());
        user.setRole(Role.valueOf(request.getRole()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsDeleted(false);

        User userSave = userReposistory.save(user);

        return toDTO(userSave);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userReposistory.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy User"));
        UserResponseDTO result = toDTO(user);
        return result;
    }

    //cập nhật username
    public UserResponseDTO updateUserName(Long id, String userNameUpdate){
        User user = userReposistory.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản"));
        if(userReposistory.existsUserByUserNameAndIdNot(userNameUpdate, id)){
            throw new AppException(HttpStatus.CONFLICT, "Tên người dùng đã tôn tại");
        }

        user.setUserName(userNameUpdate);

        return toDTO(userReposistory.save(user));
    }

    //lấy danh sách tất cả user
    public PageResponse<UserResponseDTO> getAll(String role, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        Page<User> users;
        if(role != null && !role.isBlank()){
            Role roleEnum;
            try{
                roleEnum = Role.valueOf(role);

            } catch (IllegalArgumentException e) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Vai trò lọc không hợp lệ");
            }
            users = userReposistory.findAllByIsDeletedFalseAndRole(roleEnum, pageable);
        }
        else {
            users = userReposistory.findAllByIsDeletedFalse(pageable);
        }
        Page<UserResponseDTO> dtoPage = users.map(this::toDTO);
        
        return PageResponse.from(dtoPage);
    }

    //convert DTO
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
