package com.example.rpl.RPL.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class CourseUserScore {

    CourseUser courseUser;

    Long score;

    Long activitiesCount;

    public CourseUserScore(CourseUser courseUser, Long score, Long activitiesCount) {
        this.courseUser = courseUser;
        this.score = score;
        this.activitiesCount = activitiesCount;
    }
}
