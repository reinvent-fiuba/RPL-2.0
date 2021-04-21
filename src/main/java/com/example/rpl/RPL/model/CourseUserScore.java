package com.example.rpl.RPL.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseUserScore implements CourseUserScoreInterface {

    String name;

    String surname;

    String imgUri;

    Long score;

    Long activitiesCount;
}
