package com.example.rpl.RPL.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseUserScore implements CourseUserScoreInterface {

//    CourseUser courseUser;

    String name;

    String surname;

    String imgUri;

    Long score;

    Long activitiesCount;

//    public CourseUserScore(CourseUser courseUser, Long score, Long activitiesCount) {
//    public CourseUserScore(String name, String surname, Long score, Long activitiesCount) {
//        this.name = name;
//        this.surname = surname;
//        this.score = score;
//        this.activitiesCount = activitiesCount;
//    }
}
