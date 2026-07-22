package com.vhung.studentmanager.service;

import com.vhung.studentmanager.dto.request.StudentRequestDTO;
import com.vhung.studentmanager.dto.request.UpdateStudentRequest;
import com.vhung.studentmanager.dto.response.DepartmentResponseDTO;
import com.vhung.studentmanager.dto.response.PageResponse;
import com.vhung.studentmanager.dto.response.StudentResponseDTO;
import com.vhung.studentmanager.entity.*;
import com.vhung.studentmanager.exception.AppException;
import com.vhung.studentmanager.repository.DepartmentRepository;
import com.vhung.studentmanager.repository.StudentReposistory;
import com.vhung.studentmanager.repository.UserReposistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final DepartmentRepository departmentRepository;
    private final UserReposistory userReposistory;
    private final StudentReposistory studentReposistory;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public StudentResponseDTO create(StudentRequestDTO request){
        //kieemr tra khoa tồn tại
        Departments departments = departmentRepository.findByIdAndIsDeletedFalse(request.getDepartmentId()).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy khoa"));
        //kiểm tra tên người dùng trùng lặp
        if(userReposistory.existsUserByUserName(request.getUsername())){
            throw new AppException(HttpStatus.CONFLICT, "Đã tồn tai user name");
        }
        //kiểm tra trùng mã sinh viên
        if(studentReposistory.existsStudentByStudentCode(request.getStudentCode())){
            throw new AppException(HttpStatus.CONFLICT, "Mã sinh vien đã tồn tại");
        }
        //kiểm tra trùng email
        if(studentReposistory.existsStudentByEmail(request.getEmail())){
            throw new AppException(HttpStatus.CONFLICT, "Email đã tồn tại");
        }
        Gender gender;
        try {
            gender = Gender.valueOf(request.getGender());
        }
        catch (IllegalArgumentException e){
            throw new AppException(HttpStatus.BAD_REQUEST, "Giới tính không hợp lệ");
        }

        //lưu user
        User user = User.builder()
                .userName(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .isDeleted(false)
                .build();

        User userSave = userReposistory.save(user);

        Student student = Student.builder()
                .user(userSave)
                .departments(departments)
                .studentCode(request.getStudentCode())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDob())
                .gender(gender)
                .enrollmentYear(request.getEnrollmentYear())
                .className(request.getClassName())
                .gpa(BigDecimal.ZERO)
                .isDeleted(false)
                .build();

        Student studentSave = studentReposistory.save(student);

        return toDTO(studentSave);
    }

    public StudentResponseDTO update(Long id, UpdateStudentRequest dataUpdate){
        Student student = studentReposistory.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy sinh viên"));

        student.setFullName(dataUpdate.getFullName());
        student.setEmail(dataUpdate.getEmail());
        student.setPhone(dataUpdate.getPhone());
        student.setClassName(dataUpdate.getClassName());

        Student studentSave = studentReposistory.save(student);

        return toDTO(studentSave);

    }

    public StudentResponseDTO get(Long id){
        Student student = studentReposistory.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy sinh viên"));

        return toDTO(student);
    }

    public PageResponse<StudentResponseDTO> getAll(int page, int size, String name, Long departmentId){

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students;

        boolean hasName = name != null && !name.isBlank();

        boolean hasDept = departmentId != null;

        if (hasName && hasDept) {
            students = studentReposistory
                    .findAllByIsDeletedFalseAndFullNameContainingIgnoreCaseAndDepartmentId(
                            name, departmentId, pageable);
        } else if (hasName) {
            students = studentReposistory
                    .findAllByIsDeletedFalseAndFullNameContainingIgnoreCase(name, pageable);
        } else if (hasDept) {
            students = studentReposistory
                    .findAllByIsDeletedFalseAndDepartmentId(departmentId, pageable);
        } else {
            students = studentReposistory.findAllByIsDeletedFalse(pageable);
        }

        Page<StudentResponseDTO> dtoPage = students.map(this::toDTO);

        return PageResponse.from(dtoPage);

    }

    private StudentResponseDTO toDTO(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .fullName(student.getFullName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .dob(student.getDateOfBirth())
                .gender(student.getGender())
                .enrollmentYear(student.getEnrollmentYear())
                .className(student.getClassName())
                .gpa(student.getGpa())
                .department(DepartmentResponseDTO.builder()
                        .id(student.getDepartments().getId())
                        .departmentCode(student.getDepartments().getDepartmentCode())
                        .name(student.getDepartments().getName())
                        .build())
                .build();
    }


}
