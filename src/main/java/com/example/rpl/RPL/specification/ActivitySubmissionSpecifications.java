package com.example.rpl.RPL.specification;

import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.ActivitySubmission_;
import com.example.rpl.RPL.model.Activity_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class ActivitySubmissionSpecifications {
    public static Specification<ActivitySubmission> byUserIdAndCourseId(long userId, long courseId) {
        return (root, query, criteriaBuilder) -> {
            Predicate userPredicate = criteriaBuilder.equal(root.get(ActivitySubmission_.user).get("id"), userId);
            Join<ActivitySubmission, Activity> activitySubmissionJoin = root.join(ActivitySubmission_.activity);
            Predicate coursePredicate = criteriaBuilder.equal(activitySubmissionJoin.get(Activity_.course).get("id"), courseId);
            return criteriaBuilder.and(coursePredicate, userPredicate);
        };
    }
}