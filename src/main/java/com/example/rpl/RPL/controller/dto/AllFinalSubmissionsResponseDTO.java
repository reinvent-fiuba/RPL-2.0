package com.example.rpl.RPL.controller.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AllFinalSubmissionsResponseDTO {

    List<Long> submissionFileIds;

}
