package com.vhung.studentmanager.service;

import com.vhung.studentmanager.dto.request.SemesterRequestDTO;
import com.vhung.studentmanager.dto.response.SemesterResponseDTO;
import com.vhung.studentmanager.entity.Semesters;
import com.vhung.studentmanager.exception.AppException;
import com.vhung.studentmanager.repository.SemesterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemesterService {
    private final SemesterRepository semesterRepository;

    @Transactional
    public SemesterResponseDTO create(SemesterRequestDTO request){

        if(semesterRepository.existsBySemesterCode(request.getSemesterCode())){
            throw new AppException(HttpStatus.CONFLICT, "Mã học kì đã tồn tại");
        }

        Semesters semester = new Semesters();
        semester.setSemesterCode(request.getSemesterCode());
        semester.setName(request.getName());
        semester.setStartDate(request.getStartDate());
        semester.setEndDate(request.getEndDate());
        semester.setRegStartDate(request.getRegStartDate());
        semester.setRegEndDate(request.getRegEndDate());

        if (request.getSetAsCurrent()) {

            semesterRepository.findByIsActiveIsTrue()
                    .ifPresent(old -> {
                        old.setIsActive(false);
                        semesterRepository.save(old);
                    });
            semester.setIsActive(true);
        } else {
            semester.setIsActive(false);
        }

        return toDTO(semesterRepository.save(semester));
    }

    @Transactional
    public SemesterResponseDTO setCurrent(Long id){
        Semesters target = semesterRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy học kỳ"));

        semesterRepository.findByIsActiveIsTrue()
                .filter(old -> !old.getId().equals(id))   // tránh tắt rồi bật lại chính nó
                .ifPresent(old -> {
                    old.setIsActive(false);
                    semesterRepository.save(old);
                });

        target.setIsActive(true);
        return toDTO(semesterRepository.save(target));
    }

    private SemesterResponseDTO toDTO(Semesters semesters){
        return new SemesterResponseDTO(
                semesters.getId(),
                semesters.getSemesterCode(),
                semesters.getName(),
                semesters.getStartDate(),
                semesters.getEndDate(),
                semesters.getRegStartDate(),
                semesters.getRegEndDate(),
                semesters.getIsActive());
    }
}
