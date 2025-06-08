package com.kratik.task_manager.services;

import com.kratik.task_manager.model.TasksEntity;
import com.kratik.task_manager.model.UserEntity;
import com.kratik.task_manager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderService {

    private final TaskRepository taskRepository;

    @Scheduled(fixedRate = 60000)
    public void checkReminders() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        List<TasksEntity> dueReminders = taskRepository.findByReminderTimeAndReminderSentFalse(now);

        for (TasksEntity task : dueReminders) {
            UserEntity user = task.getUser(); // This makes it user-specific

            // 🔔 You can trigger: email, push, or logging
            log.info("🔔 Reminder for user {}: '{}' is due at {}", user.getEmailId(), task.getTitle(),
                    task.getReminderTime());

            // Mark it as sent
            task.setReminderSent(true);
            taskRepository.save(task);
        }
    }
}