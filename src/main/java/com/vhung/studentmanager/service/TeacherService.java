package com.vhung.studentmanager.service;

import com.vhung.studentmanager.dto.request.TeacherRequestDTO;
import com.vhung.studentmanager.dto.response.PageResponse;
import com.vhung.studentmanager.dto.response.TeacherResponseDTO;
import com.vhung.studentmanager.entity.Departments;
import com.vhung.studentmanager.entity.Teacher;
import com.vhung.studentmanager.entity.User;
import com.vhung.studentmanager.entity.enums.Role;
import com.vhung.studentmanager.exception.AppException;
import com.vhung.studentmanager.repository.DepartmentRepository;
import com.vhung.studentmanager.repository.TeacherRepository;
import com.vhung.studentmanager.repository.UserRepository;
import com.vhung.studentmanager.specification.TeacherSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Encoder;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherResponseDTO getTeacherById(Long id, Authentication authentication) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy giảng viên với Id: " + id));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isSelf = teacher.getUser().getUserName().equals(authentication.getName());

        if (!isAdmin && !isSelf) {
            throw new AppException(HttpStatus.FORBIDDEN, "Không có quyền xem thông tin giảng viên khác");
        }
        return TeacherResponseDTO.fromEntity(teacher);
    }

    public PageResponse<TeacherResponseDTO> getTeachers(
            String name, Long departmentId, String status, Pageable pageable) {

        Specification<Teacher> spec = Specification
                .where(TeacherSpecification.hasName(name))
                .and(TeacherSpecification.hasDepartmentId(departmentId));

        if ("active".equalsIgnoreCase(status)) {
            spec = spec.and(TeacherSpecification.isNotDeleted());
        } else if ("deleted".equalsIgnoreCase(status)) {
            spec = spec.and(TeacherSpecification.isDeleted());
        } else if (!"all".equalsIgnoreCase(status)) {
            throw new AppException(HttpStatus.BAD_REQUEST,
                    "Trạng thái lọc không hợp lệ: " + status);
        }

        Page<TeacherResponseDTO> dtoPage =
                teacherRepository.findAll(spec, pageable).map(TeacherResponseDTO::fromEntity);
        return PageResponse.from(dtoPage);
    }

    public TeacherResponseDTO getCurrentTeacherByUserName(String username) {
        Teacher teacher = teacherRepository.findByUser_UserName(username)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giảng viên với username: " + username));
        return TeacherResponseDTO.fromEntity(teacher);
    }
    @Transactional
    public TeacherResponseDTO create(TeacherRequestDTO request){
        validateUniqueForCreate(request);
        Departments departments = departmentRepository.findByIdAndIsDeletedFalse(request.getIdDepartment())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy khoa"));
        //Tao user
        User user = User.builder()
                .userName(request.getUserName())
                .role(Role.TEACHER)
                .password(passwordEncoder.encode(request.getPassword()))
                .isDeleted(false)
                .build();
        User userSave = userRepository.save(user);



        Teacher teacher = Teacher.builder()
                .user(userSave)
                .teacherCode(request.getTeacherCode())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .isDeleted(false)
                .department(departments)
                .build();
        Teacher teacherSave = teacherRepository.save(teacher);

        return TeacherResponseDTO.fromEntity(teacherSave);
    }
    @Transactional
    public TeacherResponseDTO update(Long id, TeacherRequestDTO request) {
        Teacher teacher = teacherRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy giảng viên đang hoạt động"));

        User user = teacher.getUser();

        if (teacherRepository.existsByTeacherCodeAndIdNot(request.getTeacherCode(), id)) {
            throw new AppException(HttpStatus.CONFLICT, "Đã tồn tại mã giáo viên");
        }
        if (teacherRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new AppException(HttpStatus.CONFLICT, "Đã tồn tại email");
        }
        if (teacherRepository.existsByPhoneNumberContainingIgnoreCaseAndIdNot(request.getPhoneNumber(), id)) {
            throw new AppException(HttpStatus.CONFLICT, "Đã tồn tại số điện thoại");
        }
        if (userRepository.existsUserByUserNameAndIdNot(request.getUserName(), user.getId())) {
            throw new AppException(HttpStatus.CONFLICT, "Đã tồn tại userName");
        }

        Departments department = departmentRepository
                .findByIdAndIsDeletedFalse(request.getIdDepartment())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy khoa"));

        teacher.setTeacherCode(request.getTeacherCode());
        teacher.setFullName(request.getFullName());
        teacher.setEmail(request.getEmail());
        teacher.setPhoneNumber(request.getPhoneNumber());
        teacher.setDepartment(department);

        user.setUserName(request.getUserName());


        userRepository.save(user);
        return TeacherResponseDTO.fromEntity(teacherRepository.save(teacher));
    }

    // THÊM: soft-delete đồng thời khóa User đăng nhập.
    @Transactional
    public void delete(Long id) {
        Teacher teacher = teacherRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy giảng viên đang hoạt động"));

        teacher.setIsDeleted(true);
        if (teacher.getUser() != null) {
            teacher.getUser().setIsDeleted(true);
            userRepository.save(teacher.getUser());
        }
        teacherRepository.save(teacher);
    }

    // THÊM: restore đồng thời mở lại User đăng nhập.
    @Transactional
    public TeacherResponseDTO restore(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy giảng viên"));

        if (!Boolean.TRUE.equals(teacher.getIsDeleted())) {
            throw new AppException(HttpStatus.BAD_REQUEST,
                    "Giảng viên này đang hoạt động");
        }

        teacher.setIsDeleted(false);
        if (teacher.getUser() != null) {
            teacher.getUser().setIsDeleted(false);
            userRepository.save(teacher.getUser());
        }
        return TeacherResponseDTO.fromEntity(teacherRepository.save(teacher));
    }

    // THÊM: validate riêng cho create.
    private void validateUniqueForCreate(TeacherRequestDTO request) {
        if (userRepository.existsUserByUserName(request.getUserName())) {
            throw new AppException(HttpStatus.CONFLICT, "Đã tồn tại userName");
        }
        if (teacherRepository.existsByTeacherCode(request.getTeacherCode())) {
            throw new AppException(HttpStatus.CONFLICT, "Đã tồn tại mã giáo viên");
        }
        if (teacherRepository.existsByEmail(request.getEmail())) {
            throw new AppException(HttpStatus.CONFLICT, "Đã tồn tại email");
        }
        if (teacherRepository.existsByPhoneNumberContainingIgnoreCase(request.getPhoneNumber())) {
            throw new AppException(HttpStatus.CONFLICT, "Đã tồn tại số điện thoại");
        }
    }
}