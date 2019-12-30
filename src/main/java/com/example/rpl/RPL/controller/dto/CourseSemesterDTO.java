package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.CourseSemester;
import lombok.Builder;
import lombok.Getter;

import java.net.URI;
import java.time.ZonedDateTime;

@Getter
@Builder
public class CourseSemesterDTO {

    private Long id;

    private String name;

    private String universityCourseId;

    private String description;

    private Boolean active;

    private String semester;

    private String imgUri;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    public static CourseSemesterDTO fromEntity(CourseSemester courseSemester) {
        return CourseSemesterDTO.builder()
            .id(courseSemester.getId())
            .name(courseSemester.getName())
            .universityCourseId(courseSemester.getUniversityCourseId())
            .description(courseSemester.getDescription())
            .active(courseSemester.getActive())
            .semester(courseSemester.getSemester())
            .imgUri(courseSemester.getImgUri())
            .dateCreated(courseSemester.getDateCreated())
            .lastUpdated(courseSemester.getLastUpdated())
            .build();
    }
}
