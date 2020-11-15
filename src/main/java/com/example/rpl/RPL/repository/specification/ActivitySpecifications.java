package com.example.rpl.RPL.repository.specification;

import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public final class ActivitySpecifications {

    public static Specification<Activity> courseIdIs(Long courseId) {
        return (root, query, builder) -> builder.equal(
                root.get("activity").<String>get("course").<String>get("id"),
                courseId);
    }

    public static Specification<Activity> categoryIdIs(Long categoryId) {
        return (root, query, builder) -> builder.equal(
                root.get("activity").<String>get("activityCategory").<String>get("id"),
                categoryId);
    }
}