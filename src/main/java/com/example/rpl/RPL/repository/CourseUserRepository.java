package com.example.rpl.RPL.repository;

import com.example.rpl.RPL.model.CourseUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseUserRepository extends JpaRepository<CourseUser, Long> {

    Optional <CourseUser> findById(Long courseUserId);

    Optional<CourseUser> findByCourse_IdAndUser_Id(Long courseId, Long userId);

    Long deleteByCourse_IdAndUser_Id(Long courseId, Long userId);

    Optional<CourseUser> findByCourse_IdAndRole_IdAndUser_Id(Long courseId, Long roleId, Long userId);

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
}
