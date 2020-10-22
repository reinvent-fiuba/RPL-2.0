package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.ActivitySubmissionComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivitySubmissionCommentRepository extends
    JpaRepository<ActivitySubmissionComment, Long> {

    List<ActivitySubmissionComment> findAllByActivitySubmission(ActivitySubmission submission);
}