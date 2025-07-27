package com.kratik.task_manager.model;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Locale;

public class TaskSpecification {

    public static Specification<TasksEntity> filterTasks(
            UserEntity user,
            String title,
            String status,
            String category,
            String tag,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (user != null) {
                predicate = cb.and(predicate, cb.equal(root.get("user"), user));
            }

            if (title != null && !title.isBlank()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (status != null && !status.isBlank()) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), TaskStatus.valueOf(status.toUpperCase(Locale.ROOT))));
            }

            if (category != null && !category.isBlank()) {
                predicate = cb.and(predicate, cb.equal(root.get("category").get("name"), category));
            }

            if (tag != null && !tag.isBlank()) {
                Join<TasksEntity, TagsEntity> tagJoin = root.join("tags", JoinType.LEFT);
                predicate = cb.and(predicate, cb.equal(tagJoin.get("name"), tag));
                query.distinct(true); // to avoid duplicates
            }

            if (fromDate != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("dueDate"), fromDate));
            }

            if (toDate != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("dueDate"), toDate));
            }

            return predicate;
        };
    }
}