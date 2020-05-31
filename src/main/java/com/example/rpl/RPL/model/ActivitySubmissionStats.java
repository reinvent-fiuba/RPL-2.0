package com.example.rpl.RPL.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Map;

import static java.time.ZonedDateTime.now;

@Getter
public class ActivitySubmissionStats {

    private int total;

    private Map<SubmissionStatus, Long> countByStatus;

    public ActivitySubmissionStats(int total, Map<SubmissionStatus, Long> countByStatus) {
        this.total = total;
        this.countByStatus = countByStatus;
    }
}
