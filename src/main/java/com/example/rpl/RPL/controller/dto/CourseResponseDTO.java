package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.Course;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseResponseDTO {

    private Long id;

    private String name;

    private String university;

    private String universityCourseId;

    private String description;

    private Boolean active;

    private String semester;

    private ZonedDateTime semesterStartDate;

    private ZonedDateTime semesterEndDate;

    private String imgUri;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    public static CourseResponseDTO fromEntity(Course course) {
        return CourseResponseDTO.builder()
            .id(course.getId())
            .name(course.getName())
            .university(course.getUniversity())
            .universityCourseId(course.getUniversityCourseId())
            .description(course.getDescription())
            .active(course.getActive())
            .semester(course.getSemester())
            .semesterStartDate(course.getSemesterStartDate())
            .semesterEndDate(course.getSemesterEndDate())
            .imgUri(course.getImgUri())
            .dateCreated(course.getDateCreated())
            .lastUpdated(course.getLastUpdated())
            .build();
    }
}
