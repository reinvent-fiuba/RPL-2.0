package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.model.CourseUserScoreInterface;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseUserRepository extends JpaRepository<CourseUser, Long> {

    Optional<CourseUser> findById(Long courseUserId);

    Optional<CourseUser> findByCourse_IdAndUser_Id(Long courseId, Long userId);

    Long deleteByCourse_IdAndUser_Id(Long courseId, Long userId);

    Optional<CourseUser> findByCourse_IdAndRole_IdAndUser_Id(Long courseId, Long roleId,
        Long userId);

    List<CourseUser> findByCourse_IdAndRole_Id(Long courseId, Long roleId);

    List<CourseUser> findByCourse_Id(Long courseId);

    List<CourseUser> findByUser_Id(Long userId);


    @Query("select case when count(cu)>0 then 'true' else 'false' end " +
        "from CourseUser cu " +
        "inner join cu.course c " +
        "inner join cu.user u " +
        "inner join cu.role r " +
        "where c.name = ?1 and c.universityCourseId = ?2 and c.semester = ?3 " +
        "and u.id = ?4 and r.name = 'admin'")
    Boolean existsByNameAndUniversityCourseIdAndSemesterAndAdmin(String name,
        String universityCourseId,
        String semester,
        Long userId);


    @Query(
        value =
            "SELECT course_students.name,\n"
                + "       course_students.surname,\n"
                + "       COALESCE(SUM(stats.points), 0) as score,\n"
                + "       COUNT(stats.activity_id) as activitiesCount\n"
                + "FROM (SELECT u.id, u.name, u.surname\n"
                + "      FROM users u\n"
                + "               LEFT OUTER JOIN course_users ON u.id = course_users.user_id\n"
                + "      WHERE course_users.course_id = :courseId \n"
                + "        AND course_users.role_id = :roleId) as course_students \n"
                + "    LEFT OUTER JOIN\n"
                + "         (SELECT acs.user_id,\n"
                + "                acs.activity_id,\n"
                + "                MAX(points) as points\n"
                + "         FROM activity_submissions acs\n"
                + "                  LEFT OUTER JOIN activities ac on acs.activity_id = ac.id\n"
                + "         WHERE ac.course_id = :courseId \n"
                + "           AND ac.active = true\n"
                + "           AND acs.status = 'success'\n"
                + "         GROUP BY user_id, activity_id) as stats "
                + "    ON course_students.id = stats.user_id\n"
                + "GROUP BY course_students.name, course_students.surname;",
        nativeQuery = true)
    List<CourseUserScoreInterface> getActivityStatsForCourseId(@Param("courseId") Long courseId,
        @Param("roleId") Long studentRoleId);
}
