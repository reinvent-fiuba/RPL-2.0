package com.example.rpl.RPL.model.stats;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class SubmissionsStats {

    private List<SubmissionsStat> submissionsStats;

    // This metadata could differ depending on the way submissions are grouped,
    // For example, for SubmissionsStats by Activity the metadata will
    // contain the information associated to each SubmissionStat in the
    // submissionStats list.
     private List<Map<String, String>> metadata;

    public SubmissionsStats(List<SubmissionsStat> submissionsStats, List<Map<String, String>> metadata) {
        this.submissionsStats = submissionsStats;
        this.metadata = metadata;
    }
}
