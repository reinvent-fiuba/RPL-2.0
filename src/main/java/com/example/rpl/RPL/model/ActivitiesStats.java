package com.example.rpl.RPL.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class ActivitiesStats {

    private int total;

    private Map<String, Long> countByStatus;

    private Map<String, Long> score;

    public ActivitiesStats(int total, Map<String, Long> countByStatus, Map<String, Long> score) {
        this.total = total;
        this.countByStatus = countByStatus;
        this.score = score;
    }
}
