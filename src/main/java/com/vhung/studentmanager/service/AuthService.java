package com.vhung.studentmanager.service;

import com.vhung.studentmanager.dto.request.LoginRequestDTO;
import com.vhung.studentmanager.dto.response.LoginResponseDTO;
import com.vhung.studentmanager.entity.Admins;
import com.vhung.studentmanager.entity.Student;
import com.vhung.studentmanager.entity.Teacher;
import com.vhung.studentmanager.entity.User;
import com.vhung.studentmanager.exception.AppException;
import com.vhung.studentmanager.repository.AdminReposistory;
import com.vhung.studentmanager.repository.StudentRepository;
import com.vhung.studentmanager.repository.TeacherRepository;
import com.vhung.studentmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminReposistory adminReposistory;
    private final EmailService emailService;
    private final OtpStore otpStore;

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

    public void sendResetOtp(String identifier){

        User user = resolveUserByIdentifier(identifier);

        String otpCode = generateOtp();
        //Lưu mã
        otpStore.save(user.getUserName(), otpCode);

        String email = resolveEmail(user);
        //Gửi mã
        emailService.sendOtpEmail(email, otpCode);
    }

    //Kiểm tra xem mã đúng không, còn hạn không
    public void verifyOtp(String identifier, String inputCode){
        String userName = resolveUserByIdentifier(identifier).getUserName();
        OtpStore.OtpEntry entry = otpStore.get(userName);
        validateOtpEntry(entry, inputCode, identifier);
    }

    private User resolveUserByIdentifier(String identifier){
        //Tìm theo username
        Optional<User> byUserName = userRepository.findByUserName(identifier);
        if(byUserName.isPresent()){
            return byUserName.get();
        }

        //Tìm theo email Student
        Optional<Student> byEmailStudent = studentRepository.findByEmail(identifier);
        if(byEmailStudent.isPresent()){
            return byEmailStudent.get().getUser();
        }

        //tìm theo email Teacher
        Optional<Teacher> byEmailTeacher = teacherRepository.findByEmail(identifier);
        if(byEmailTeacher.isPresent()){
            return byEmailTeacher.get().getUser();
        }
        //tìm theo email admin
        Optional<Admins> byEmailAdmin = adminReposistory.findByEmail(identifier);
        if(byEmailAdmin.isPresent()){
            return byEmailAdmin.get().getUser();
        }

        throw new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy user");
    }

    private String resolveEmail(User user) {
        
        return switch (user.getRole()) {
            case STUDENT -> studentRepository.findByUserId(user.getId())
                    .map(Student::getEmail)
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy email"));
            case TEACHER -> teacherRepository.findByUserId(user.getId())
                    .map(Teacher::getEmail)
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy email"));
            case ADMIN -> adminReposistory.findByUserId(user.getId())
                    .map(Admins::getEmail)
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy email"));
        };
    }

    @Transactional
    public void resetPassword(String identifier, String otpCode, String newPassword){
        User user = resolveUserByIdentifier(identifier);

        //Lấy otp
        OtpStore.OtpEntry entry = otpStore.get(user.getUserName());
        //thực hiện kiểm tra, nếu có lỗi thì dừng
        validateOtpEntry(entry, otpCode, user.getUserName());

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpStore.remove(user.getUserName());

    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));   // 6 chữ số ngẫu nhiên
    }

    private void validateOtpEntry(OtpStore.OtpEntry otpEntry, String inputCode, String userName){
        if(otpEntry == null){
            throw new AppException(HttpStatus.BAD_REQUEST, "Không tìm thấy yêu cầu OTP, vui lòng gửi lai");
        }
        //Hết hạn thì xóa otp
        if(LocalDateTime.now().isAfter(otpEntry.getExpiresAt())){
            otpStore.remove(userName);
            throw new AppException(HttpStatus.BAD_REQUEST, "Mã OTP đã hết hạn, vui lòng gửi lại");
        }
        if (!otpEntry.getOtpCode().equals(inputCode)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Mã OTP không đúng");
        }
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
