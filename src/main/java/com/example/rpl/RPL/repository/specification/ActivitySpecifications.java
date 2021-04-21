package com.example.rpl.RPL.repository.specification;

import com.example.rpl.RPL.model.Activity;
import org.springframework.data.jpa.domain.Specification;

public final class ActivitySpecifications {

    public static Specification<Activity> courseIdIs(Long courseId) {
        return (root, query, builder) -> builder.equal(
            root.get("course").<String>get("id"),
            courseId);
    }

    public static Specification<Activity> categoryIdIs(Long categoryId) {
        return (root, query, builder) -> builder.equal(
            root.get("activityCategory").<String>get("id"),
            categoryId);
    }

    public static Specification<Activity> notDeleted() {
        return (root, query, builder) -> builder.equal(
            root.get("deleted"), false);
    }

    public static Specification<Activity> isActive(Boolean isActive) {
        return (root, query, builder) -> builder.equal(
            root.get("active"), isActive);
    }
}