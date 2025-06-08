package com.kratik.task_manager.repository;

import com.kratik.task_manager.model.Priority;
import com.kratik.task_manager.model.TasksEntity;
import com.kratik.task_manager.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<TasksEntity, Long> {
    List<TasksEntity> findByUser(UserEntity user);
    List<TasksEntity> findByUserAndCompleted(UserEntity user, boolean completed);

    List<TasksEntity> findByUserAndDueDate(UserEntity user, LocalDate dueDate);

    List<TasksEntity> findByUserAndDueDateAfter(UserEntity user, LocalDate date);

    List<TasksEntity> findByUserAndTitleContainingIgnoreCase(UserEntity user, String keyword);

    List<TasksEntity> findByUserAndPriority(UserEntity user, Priority priority);

    List<TasksEntity> findByReminderTime(LocalDateTime time);

    List<TasksEntity> findByReminderTimeAndReminderSentFalse(LocalDateTime time);
}
