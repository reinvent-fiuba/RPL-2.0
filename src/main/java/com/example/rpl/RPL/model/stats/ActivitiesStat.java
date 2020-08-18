package com.example.rpl.RPL.model.stats;

import lombok.Getter;

import java.util.List;

@Getter
public class ActivitiesStat {

    private Long started;

    private Long notStarted;

    private Long solved;

    private Long obtainedPoints;

    private Long totalPoints;

    public ActivitiesStat(Long started, Long notStarted, Long solved, Long pendingPoints, Long obtainedPoints) {
        this.started = started;
        this.notStarted = notStarted;
        this.solved = solved;
        this.obtainedPoints = obtainedPoints;
        this.totalPoints = pendingPoints + obtainedPoints;
    }
}
