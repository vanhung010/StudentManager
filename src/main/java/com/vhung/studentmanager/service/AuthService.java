package com.vhung.studentmanager.service;

import com.vhung.studentmanager.dto.request.LoginRequestDTO;
import com.vhung.studentmanager.dto.response.LoginResponseDTO;
import com.vhung.studentmanager.entity.Student;
import com.vhung.studentmanager.entity.Teacher;
import com.vhung.studentmanager.entity.User;
import com.vhung.studentmanager.exception.AppException;
import com.vhung.studentmanager.repository.StudentRepository;
import com.vhung.studentmanager.repository.TeacherRepository;
import com.vhung.studentmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO request){
        User user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "Sai tài khoản hoặc mật khẩu"));

        if(user.getIsDeleted()){
            throw new AppException(HttpStatus.UNAUTHORIZED, "Tài khoanr đã bị vô hiệu hóa");
        }

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
           throw new AppException(HttpStatus.UNAUTHORIZED, "Sai tài khoản hoặc mật khẩu");
        }

        String fullName = resolveFullName(user);

        return LoginResponseDTO.builder()
                .id(user.getId())
                .username(user.getUserName())
                .role(user.getRole())
                .fullName(fullName)
                .build();

    }

    private String resolveFullName(User user) {
        switch (user.getRole()) {
            case STUDENT -> {
                return studentRepository.findById(user.getId()).map(Student::getFullName).get();
            }
            case TEACHER -> {
                return teacherRepository.findById(user.getId()).map(Teacher::getFullName).get();
            }
            default -> {
                return user.getUserName();
            }
        }
    }
}
