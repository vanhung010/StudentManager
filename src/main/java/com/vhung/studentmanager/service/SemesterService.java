package com.vhung.studentmanager.service;

import com.vhung.studentmanager.dto.request.SemesterRequestDTO;
import com.vhung.studentmanager.dto.response.PageResponse;
import com.vhung.studentmanager.dto.response.SemesterResponseDTO;
import com.vhung.studentmanager.entity.Semesters;
import com.vhung.studentmanager.exception.AppException;
import com.vhung.studentmanager.repository.SemesterRepository;
import com.vhung.studentmanager.specification.SemesterSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    //Tắt mọi ca khác
        semesterRepository.findByIsActiveIsTrue()
                .filter(old -> !old.getId().equals(id))   // tránh tắt rồi bật lại chính nó
                .ifPresent(old -> {
                    old.setIsActive(false);
                    semesterRepository.save(old);
                });

        target.setIsActive(true);
        return toDTO(semesterRepository.save(target));
    }

    @Transactional
    public void delete(Long id) {
        Semesters semester = semesterRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy học kỳ"));
        if (Boolean.TRUE.equals(semester.getIsActive())) {
            throw new AppException(HttpStatus.CONFLICT, "Không thể xóa học kỳ đang hoạt động");
        }
        semesterRepository.delete(semester);
    }

    public PageResponse<SemesterResponseDTO> getAll(String status,
                                                     String keyword,
                                                     Pageable pageable) {
        Specification<Semesters> specification = Specification
                .where(SemesterSpecification.hasStatus(status))
                .and(SemesterSpecification.hasKeyword(keyword));
        Page<SemesterResponseDTO> page = semesterRepository
                .findAll(specification, pageable)
                .map(this::toDTO);

        return PageResponse.from(page);
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
