package com.example.rpl.RPL.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class ActivitiesStats {

    private int total;

    private Map<String, Long> countByStatus;

    public ActivitiesStats(int total, Map<String, Long> countByStatus) {
        this.total = total;
        this.countByStatus = countByStatus;
    }
}
