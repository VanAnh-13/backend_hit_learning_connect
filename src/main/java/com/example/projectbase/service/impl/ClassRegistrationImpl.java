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
import com.example.projectbase.exception.extended.ForbiddenException;
import com.example.projectbase.exception.extended.NotFoundException;
import com.example.projectbase.repository.ClassRegistrationRepository;
import com.example.projectbase.repository.ClassRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.ClassRegistrationService;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Arrays.stream;

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

        User user = userRepository.findById(userId).orElseThrow(()->new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND));
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
        ClassRegistration registration = classRegistrationRepository.findById(request.getRegistrationId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ClassRegistration.REGISTRATION_NOT_FOUND));
        registration.setStatus(request.isApproved() ? SubmissionStatus.ACCEPTED : SubmissionStatus.WRONG_ANSWER);
        registration.setPending(false);
        classRegistrationRepository.save(registration);
    }


    @Override
    public void deleteRegistration(Long id) {
       if(!classRegistrationRepository.existsById(id)){
           throw new NotFoundException(ErrorMessage.ClassRegistration.REGISTRATION_NOT_FOUND);
       }
       classRegistrationRepository.deleteById(id);
    }

    @Override
    public void cancelRegistration(Long userId, Long classId) {
         User user= userRepository.findById(userId).
                 orElseThrow(()-> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND));

         ClassRoom classRoom= classRepository.findById(classId).
                 orElseThrow(()-> new NotFoundException(ErrorMessage.ClassRegistration.CLASS_NOT_FOUND));

         ClassRegistration classRegistration= classRegistrationRepository.findByClassEntityAndStudent(classRoom, user).
                 orElseThrow(()-> new NotFoundException(ErrorMessage.ClassRegistration.REGISTRATION_NOT_FOUND));
         classRegistrationRepository.delete(classRegistration);
    }

    @Override
    public Page<ClassRegistrationResponse> getRegistrationsByUser(Long userId, Pageable pageable, UserPrincipal principal) {
        if (!principal.getId().equals(userId) && !principal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN_VIEW_USER_REGISTRATION);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND));

        Page<ClassRegistration> registrations = classRegistrationRepository.findByStudent(user, pageable);

        return registrations.map(this::toReponse);
    }

    @Override
    public Page<ClassRegistrationResponse> getAllRegistrations(Pageable pageable) {
        return classRegistrationRepository.findAll(pageable)
                .map(this::toReponse);
    }

    @Override
    public Page<ClassRegistrationResponse> filterRegistrations(FilterRegistrationRequest request, Pageable pageable) {
        return classRegistrationRepository
                .filterRegistrations(request.getClassId(), request.getStudentEmail(), pageable)
                .map(this::toReponse);
    }

        public ClassRegistrationResponse toReponse(ClassRegistration reg){
            return ClassRegistrationResponse.builder()
                    .registrationId(reg.getRegistrationId())
                    .classTitle(reg.getClassEntity().getTitle())
                    .registeredAt(reg.getRegisteredAt())
                    .pending(reg.isPending())
                    .studentEmail(reg.getStudent().getEmail())
                    .build();
        }
}
