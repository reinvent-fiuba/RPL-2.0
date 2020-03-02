package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.IOTest;
import com.example.rpl.RPL.model.UnitTest;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActivityResponseDTO {

    private Long id;

    private Long courseId;

    private Long categoryId;

    private String categoryName;

    private String categoryDescription;

    private String name;

    private String description;

    private String language;

    private Boolean active;

    private String initialCode;

    private Long fileId;

    private String activityUnitTests;

    private List<IOTestResponseDTO> activityIOTests;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    public static ActivityResponseDTO fromEntity(Activity activity, UnitTest unitTest,
        List<IOTest> ioTests) {
        ActivityResponseDTOBuilder ab = ActivityResponseDTO.builder()
            .id(activity.getId())
            .courseId(activity.getCourse().getId())
            .categoryId(activity.getActivityCategory().getId())
            .categoryName(activity.getActivityCategory().getName())
            .categoryDescription(activity.getActivityCategory().getDescription())
            .name(activity.getName())
            .description(activity.getDescription())
            .language(activity.getLanguage().getName())
            .active(activity.getActive())
            .initialCode(activity.getInitialCode())
            .fileId(activity.getSupportingFile().getId())
            .dateCreated(activity.getDateCreated())
            .lastUpdated(activity.getLastUpdated());

        if (unitTest != null) {
            ab.activityUnitTests(new String(unitTest.getTestFile().getData()));
        }
        ab.activityIOTests(ioTests.stream().map(ioTest -> new IOTestResponseDTO(ioTest.getId(),
            ioTest.getTestIn(), ioTest.getTestOut())).collect(Collectors.toList()));

        return ab.build();
    }

    @Getter
    @AllArgsConstructor
    public static class IOTestResponseDTO {

        private Long id;
        private String in;
        private String out;

    }
}
