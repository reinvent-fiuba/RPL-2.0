package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.Course;
import com.example.rpl.RPL.model.CourseSemester;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class CourseDTO {

    private Long id;

    private String name;

    private String universityCourseId;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    public static CourseDTO fromEntity(Course course) {
        return CourseDTO.builder()
            .id(course.getId())
            .name(course.getName())
            .universityCourseId(course.getUniversityCourseId())
            .dateCreated(course.getDateCreated())
            .lastUpdated(course.getLastUpdated())
            .build();
    }
}
