package com.example.rpl.RPL.specification;

import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.ActivitySubmission;
import com.example.rpl.RPL.model.ActivitySubmission_;
import com.example.rpl.RPL.model.Activity_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

public class ActivitySpecifications {
    public static Specification<Activity> byCourseId(long courseId) {
        return (root, query, criteriaBuilder) -> {
            Predicate coursePredicate = criteriaBuilder.equal(root.get(Activity_.course).get("id"), courseId);
            return criteriaBuilder.and(coursePredicate);
        };
    }
}