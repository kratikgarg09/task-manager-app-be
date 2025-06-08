package com.kratik.task_manager.services;

import com.kratik.task_manager.model.TasksEntity;
import com.kratik.task_manager.model.UserEntity;
import com.kratik.task_manager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderService {

    private final TaskRepository taskRepository;
    private final EmailService emailService;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = now.minusMinutes(1); // avoid missing in case of small delays
        LocalDateTime windowEnd = now.plusMinutes(2);
        List<TasksEntity> dueReminders = taskRepository.findByReminderTimeBetweenAndReminderSent(windowStart,
                windowEnd,false);

        log.info("⏰ Checking tasks between {} and {}", windowStart, windowEnd);
//        log.info("📬 Sending reminder for task ID {}", dueReminders.getFirst().getId());
        for (TasksEntity task : dueReminders) {
            UserEntity user = task.getUser();
            String subject = "🔔 Task Reminder: " + task.getTitle();
            String body = "Hi " + user.getName() + ",\n\n"
                    + "This is a reminder for your task:\n\n"
                    + "📌 Title: " + task.getTitle() + "\n"
                    + "📅 Due: " + task.getDueDate() + "\n\n"
                    + "Stay productive!\nYour Task Manager";

            emailService.sendReminderEmail(user.getEmailId(), subject, body);
            log.info("mail sent");

            task.setReminderSent(true);
            taskRepository.save(task);
        }
    }
}