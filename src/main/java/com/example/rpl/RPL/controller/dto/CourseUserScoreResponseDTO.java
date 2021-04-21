package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.CourseUserScoreInterface;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseUserScoreResponseDTO {

    private String name;

    private String surname;

    private String imgUri;

    private Long score;

    private Long activitiesCount;

    public static CourseUserScoreResponseDTO fromEntity(
        CourseUserScoreInterface courseUserScoreInterface) {
        return CourseUserScoreResponseDTO.builder()
            .name(courseUserScoreInterface.getName())
            .surname(courseUserScoreInterface.getSurname())
            .imgUri(courseUserScoreInterface.getImgUri())
            .score(courseUserScoreInterface.getScore())
            .activitiesCount(courseUserScoreInterface.getActivitiesCount())
            .build();
    }
}
