package com.example.projectbase.service.impl;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.request.classes.ApproveOrRejectRequest;
import com.example.projectbase.domain.dto.request.classes.FilterRegistrationRequest;
import com.example.projectbase.domain.dto.request.classes.RegisterClassRequest;
import com.example.projectbase.domain.dto.response.classes.ClassRegistrationResponse;
import com.example.projectbase.domain.entity.ClassRegistration;
import com.example.projectbase.domain.entity.ClassRoom;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.model.SubmissionStatus;
import com.example.projectbase.exception.extended.NotFoundException;
import com.example.projectbase.repository.ClassRegistrationRepository;
import com.example.projectbase.repository.ClassRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.service.ClassRegistrationService;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class ClassRegistrationImpl implements ClassRegistrationService {

    private final ClassRegistrationRepository classRegistrationRepository;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;

    @Override
    public void register(Long userId, RegisterClassRequest request) throws BadRequestException {
        ClassRoom classRoom = classRepository.findById(request.getClassId()).
                orElseThrow(()-> new NotFoundException(ErrorMessage.ClassRegistration.CLASS_NOT_FOUND));

        User user = userRepository.findById(userId).orElseThrow(()->new NotFoundException("User not found"));
         if(classRegistrationRepository.existsByClassEntityAndStudent(classRoom, user)){
             throw new BadRequestException(ErrorMessage.ClassRegistration.ALREADY_REGISTERED);
         }

        ClassRegistration registration= ClassRegistration.builder()
                .classEntity(classRoom)
                .student(user)
                .status(SubmissionStatus.PENDING)
                .pending(true)
                .build();
         classRegistrationRepository.save(registration);
    }

    @Override
    public void approveOrReject(Long adminId, ApproveOrRejectRequest request) {

    }

    @Override
    public void deleteRegistration(Long id) {

    }

    @Override
    public void cancelRegistration(Long userId, Long classId) {

    }

    @Override
    public List<ClassRegistrationResponse> getRegistrationsByUser(Long userId) {
        return List.of();
    }

    @Override
    public List<ClassRegistrationResponse> getAllRegistrations() {
        return List.of();
    }

    @Override
    public List<ClassRegistrationResponse> filterRegistrations(FilterRegistrationRequest request) {
        return List.of();
    }
}
