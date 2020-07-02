package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.User;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<ActivitySubmission, Long> {

    List<ActivitySubmission> findAllByActivityIn(List<Activity> activities);

    List<ActivitySubmission> findAllByActivityInAndUser_Id(List<Activity> activities, Long userId);

    List<ActivitySubmission> findAllByActivityInAndUser_IdAndDateCreatedBetween(List<Activity> activities,
                                                                                Long userId,
                                                                                ZonedDateTime dateCreatedStart,
                                                                                ZonedDateTime dateCreatedEnd);

    List<ActivitySubmission> findAllByUserAndActivityIn(User user, List<Activity> activities);



    List<ActivitySubmission> findAllByUserAndActivity_Id(User user, Long activityId);

    List<ActivitySubmission> findAllByActivity_Course_Id(Long courseId);

    List<ActivitySubmission> findAllByActivity_Course_IdAndUser_Id(Long courseId, Long userId);

    List<ActivitySubmission> findAllByActivity_Course_IdAndUser_IdAndDateCreatedBetween(Long courseId,
                                                                                        Long userId,
                                                                                        ZonedDateTime dateCreatedStart,
                                                                                        ZonedDateTime dateCreatedEnd);

    @Query("select s " +
        "from ActivitySubmission s " +
        "inner join s.activity a " +
        "where s.user.id = ?1 and a.course.id = ?2")
    List<ActivitySubmission> findAllByUserIdAndCourseId(Long userId, Long courseId);
}