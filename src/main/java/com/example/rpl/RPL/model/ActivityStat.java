package com.example.rpl.RPL.model;

import lombok.Getter;

import java.util.List;

@Getter
public class ActivityStat {

    private Long started;

    private Long notStarted;

    private Long solved;

    private Long obtainedPoints;

    private Long totalPoints;

    public ActivityStat(Long started, Long notStarted, Long solved, Long obtainedPoints, Long totalPoints) {
        this.started = started;
        this.notStarted = notStarted;
        this.solved = solved;
        this.obtainedPoints = obtainedPoints;
        this.totalPoints = totalPoints;
    }
}
