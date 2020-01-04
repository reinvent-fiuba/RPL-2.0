package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.BadRequestException;
import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.RPLFile;
import com.example.rpl.RPL.model.SubmissionStatus;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.repository.ActivityRepository;
import com.example.rpl.RPL.repository.FileRepository;
import com.example.rpl.RPL.repository.SubmissionRepository;
import com.example.rpl.RPL.repository.UserRepository;
import com.example.rpl.RPL.security.UserPrincipal;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class SubmissionService {

    private ActivityRepository activityRepository;
    private UserRepository userRepository;
    private SubmissionRepository submissionRepository;
    private FileRepository fileRepository;

    @Autowired
    public SubmissionService(ActivityRepository activityRepository,
        UserRepository userRepository,
        SubmissionRepository submissionRepository,
        FileRepository fileRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
        this.fileRepository = fileRepository;
    }


    public ActivitySubmission getActivitySubmission(Long submissionId) {
        return submissionRepository.findById(submissionId).orElseThrow(
            () -> new NotFoundException("Activity submission not found",
                "activity_submission_not_found"));
    }


    @Transactional
    public ActivitySubmission create(UserPrincipal currentUser, Long courseId, Long activityId,
        String description, MultipartFile file) {

        Activity ac = activityRepository.findById(activityId).orElseThrow(
            () -> new NotFoundException("Activity not found",
                "activity_not_found"));

//        User user = userRepository.findById(currentUser.getId()).orElseThrow(
//            () -> new NotFoundException("User not found",
//                "user_not_found"));



        try {
            RPLFile f = new RPLFile(String.format("%d_%d_%d", courseId, activityId, currentUser.getId()), file.getContentType(), file.getBytes());

            f = fileRepository.save(f);

            ActivitySubmission as = new ActivitySubmission(ac, currentUser.getId(), f,
                SubmissionStatus.PENDING);

            as = submissionRepository.save(as);

            return as;

        } catch (IOException e) {
            log.error("ERROR OBTENIENDO LOS BYTES DE LA SUBMISSION");
            log.error(e.getMessage());
            throw new BadRequestException("Error obteniendo los bytes de la submission");
        }
    }
}
