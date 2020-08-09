package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.Language;
import com.example.rpl.RPL.model.SubmissionStatus;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserActivityResponseDTO {

    private Long id;

    private Long courseId;

    private Long categoryId;

    private String categoryName;

    private String categoryDescription;

    private String name;

    private String description;

    private Language language;

    private Boolean isIOTested;

    private Boolean active;

    private Boolean deleted;

    private Long points;

    private Long fileId;

    private String submissionStatus;

    private ZonedDateTime lastSubmissionDate;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    private static UserActivityResponseDTO fromEntity(Activity activity,
        String submissionStatus, ZonedDateTime lastSubmissionDate) {
        return UserActivityResponseDTO.builder()
            .id(activity.getId())
            .courseId(activity.getCourse().getId())
            .categoryId(activity.getActivityCategory().getId())
            .categoryName(activity.getActivityCategory().getName())
            .categoryDescription(activity.getActivityCategory().getDescription())
            .name(activity.getName())
            .description(activity.getDescription())
            .language(activity.getLanguage())
            .isIOTested(activity.getIsIOTested())
            .active(activity.getActive())
            .deleted(activity.getDeleted())
            .points(activity.getPoints())
            .fileId(activity.getStartingFiles().getId())
            .dateCreated(activity.getDateCreated())
            .lastUpdated(activity.getLastUpdated())
            .submissionStatus(submissionStatus)
            .lastSubmissionDate(lastSubmissionDate)
            .build();
    }

    /**
     * Agreggate activity status with latest submission information.
     */
    public static UserActivityResponseDTO fromEntityWithStatus(Activity activity,
        Map<Activity, List<ActivitySubmission>> submissions) {
        List<ActivitySubmission> s = submissions.getOrDefault(activity, new ArrayList<>());

        if (s.isEmpty()) {
            return fromEntity(activity, "", null);
        }

        ZonedDateTime lastSubmission = s.stream().max(Comparator.comparing(ActivitySubmission::getLastUpdated)).get().getLastUpdated();

        Set<SubmissionStatus> statuses = s.stream().map(ActivitySubmission::getStatus).collect(Collectors.toSet());

        SubmissionStatus bestStatus = statuses.stream().max(Comparator.comparingInt(value -> value.ordinal())).get();

        return fromEntity(activity, bestStatus.name(), lastSubmission);


    }
}
