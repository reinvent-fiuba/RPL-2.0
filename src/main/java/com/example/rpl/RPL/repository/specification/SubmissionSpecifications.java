package com.example.rpl.RPL.repository.specification;

import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.SubmissionStatus;
import com.example.rpl.RPL.model.User;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;

public final class SubmissionSpecifications {

    public static Specification<ActivitySubmission> courseIdIs(Long courseId) {
        return (root, query, builder) -> builder.equal(
                root.get("activity").<String>get("course").<String>get("id"),
                courseId);
    }

    public static Specification<ActivitySubmission> categoryIdIs(Long categoryId) {
        return (root, query, builder) -> builder.equal(
                root.get("activity").<String>get("activityCategory").<String>get("id"),
                categoryId);
    }

    public static Specification<ActivitySubmission> userIdIs(Long userId) {
        return (root, query, builder) -> builder.equal(
                root.get("user").<String>get("id"),
                userId);
    }

    public static Specification<ActivitySubmission> userIdsAre(List<User> users) {
        return (root, query, builder) -> {
            CriteriaBuilder.In<Object> in = builder.in(root.get("user"));
            users.forEach(user -> in.value(user));
            return in;
        };
    }

    public static Specification<ActivitySubmission> status(SubmissionStatus status) {
        return (root, query, builder) -> builder.equal(
            root.get("status"),
            status);
    }

    public static Specification<ActivitySubmission> activityIdIs(Long activityId) {
        return (root, query, builder) -> builder.equal(
                root.get("activity").<String>get("id"),
                activityId);
    }

    public static Specification<ActivitySubmission> dateCreatedIs(LocalDate date) {
        ZonedDateTime startOfDay = date.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault());
        return (root, query, builder) -> builder.between(
                root.get("dateCreated"),
                startOfDay,
                endOfDay);
    }

    public static Specification<ActivitySubmission> activitiesIn(List<Activity> activities) {
        return (root, query, builder) -> {
            CriteriaBuilder.In<Object> in = builder.in(root.get("activity"));
            activities.forEach(activity -> in.value(activity));
            return in;
        };
    }
}
