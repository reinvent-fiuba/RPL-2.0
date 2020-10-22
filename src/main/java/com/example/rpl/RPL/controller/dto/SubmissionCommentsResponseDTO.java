package com.example.rpl.RPL.controller.dto;

import com.example.rpl.RPL.model.ActivitySubmissionComment;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubmissionCommentsResponseDTO {

    private List<SubmissionCommentResponseDTO> comments;


    public static SubmissionCommentsResponseDTO fromEntity(
        List<ActivitySubmissionComment> submissionComments) {
        return SubmissionCommentsResponseDTO.builder().comments(submissionComments.stream().map(
            comment -> new SubmissionCommentResponseDTO(comment.getId(),
                comment.getAuthor().getName(), comment.getComment(), comment.getDateCreated()))
            .collect(Collectors.toList())).build();
    }

    @Getter
    @AllArgsConstructor
    private static class SubmissionCommentResponseDTO {

        private Long id;
        private String author;
        private String comment;
        private ZonedDateTime dateCreated;
    }
}
