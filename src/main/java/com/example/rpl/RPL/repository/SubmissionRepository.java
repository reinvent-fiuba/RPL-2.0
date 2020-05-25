package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<ActivitySubmission, Long> {

    List<ActivitySubmission> findAllByUserAndActivityIn(User user, List<Activity> activities);

    List<ActivitySubmission> findAllByUserAndActivity_Id(User user, Long activityId);

    @Query("select s " +
        "from ActivitySubmission s " +
        "inner join s.activity a " +
        "where s.activity.id = ?1 and a.course.id = ?2")
    List<ActivitySubmission> findAllByUserIdAndCourseId(Long userId, Long courseId);
}