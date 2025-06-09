package com.kratik.task_manager.repository;

import com.kratik.task_manager.model.Priority;
import com.kratik.task_manager.model.TasksEntity;
import com.kratik.task_manager.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    List<TasksEntity> findByReminderTimeBetweenAndReminderSent(LocalDateTime start, LocalDateTime end,boolean sent);

    @Query("SELECT t FROM TasksEntity t WHERE t.user.id = :userId AND t.dueDate < :now AND t.status = 'PENDING'")
    List<TasksEntity> findMissedTasks(@Param("userId") Long userId, @Param("now") LocalDate now);

    @Query("SELECT t FROM TasksEntity t WHERE t.user.id = :userId AND t.dueDate = :start")
    List<TasksEntity> findTasksDueToday(@Param("userId") Long userId, @Param("start") LocalDate start);

    @Query("SELECT t FROM TasksEntity t WHERE t.user.id = :userId AND t.dueDate = :start")
    List<TasksEntity> findTasksDueTomorrow(@Param("userId") Long userId, @Param("start") LocalDate start);

}
