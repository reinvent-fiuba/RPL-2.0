package com.example.rpl.RPL.model;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class SubmissionStats {

    private List<SubmissionStat> submissionStats;

     // This metadata could differ depending on way submissions are grouped,
    // For example, for SubmissionsStats by Activity the metadata will
    // contain the information associated to each SubmissionStat in the
    // submissionStats list.
     private List<Map<String, String>> metadata;

    public SubmissionStats(List<SubmissionStat> submissionStats, List<Map<String, String>> metadata) {
        this.submissionStats = submissionStats;
        this.metadata = metadata;
    }
}
