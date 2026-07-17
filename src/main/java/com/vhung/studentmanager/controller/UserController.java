package com.vhung.studentmanager.controller;

import com.vhung.studentmanager.dto.request.UpdateUserNameRequest;
import com.vhung.studentmanager.dto.request.UserRequestDTO;
import com.vhung.studentmanager.dto.response.ApiResponse;
import com.vhung.studentmanager.dto.response.UserResponseDTO;
import com.vhung.studentmanager.entity.User;
import com.vhung.studentmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> create(@RequestBody UserRequestDTO request){
        UserResponseDTO data = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(data));
    }

    //lấy dữ liệu 1 user bằng id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> get(@PathVariable Long id){
        UserResponseDTO data = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUserName(@PathVariable Long id, @RequestBody UpdateUserNameRequest userNameRequest){
        UserResponseDTO data = userService.updateUserName(id, userNameRequest.getUserName());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(data));
    }
}
