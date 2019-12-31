package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.Course;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class CourseDTO {

    private Long id;

    private String name;

    private String universityCourseId;

    private String description;

    private Boolean active;

    private String semester;

    private String imgUri;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    public static CourseDTO fromEntity(Course course) {
        return CourseDTO.builder()
            .id(course.getId())
            .name(course.getName())
            .universityCourseId(course.getUniversityCourseId())
            .description(course.getDescription())
            .active(course.getActive())
            .semester(course.getSemester())
            .imgUri(course.getImgUri())
            .dateCreated(course.getDateCreated())
            .lastUpdated(course.getLastUpdated())
            .build();
    }
}
