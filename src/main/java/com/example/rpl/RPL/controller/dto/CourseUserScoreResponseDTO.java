package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.CourseUserScoreInterface;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseUserScoreResponseDTO {

    private Long id;

    private String name;

    private String surname;

    private String studentId;

    private String username;

    private String email;

    private String imgUri;

    private Long score;

    private Long activitiesCount;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    public static CourseUserScoreResponseDTO fromEntity(
        CourseUserScoreInterface courseUserScoreInterface) {
//        User user = courseUserScore.getCourseUser().getUser();
        return CourseUserScoreResponseDTO.builder()
//            .id(user.getId())
            .name(courseUserScoreInterface.getName())
            .surname(courseUserScoreInterface.getSurname())
//            .studentId(user.getStudentId())
//            .username(user.getUsername())
            .imgUri(courseUserScoreInterface.getImgUri())
//            .email(user.getEmail())
            .score(courseUserScoreInterface.getScore())
            .activitiesCount(courseUserScoreInterface.getActivitiesCount())
//            .dateCreated(user.getDateCreated())
//            .lastUpdated(user.getLastUpdated())
            .build();
    }
}
