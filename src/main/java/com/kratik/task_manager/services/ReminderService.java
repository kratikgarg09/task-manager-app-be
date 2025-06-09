package com.kratik.task_manager.services;

import com.kratik.task_manager.model.TasksEntity;
import com.kratik.task_manager.model.UserEntity;
import com.kratik.task_manager.repository.TaskRepository;
import com.kratik.task_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderService {

    private final TaskRepository taskRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

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

    @Scheduled(cron = "0 0 8 * * *")// Every day at 8:00 AM
    @Transactional
    public void sendMissedTaskWarnings() {
        LocalDate now = LocalDate.now();
        List<UserEntity> users = userRepository.findAll();

        for (UserEntity user : users) {
            List<TasksEntity> missedTasks = taskRepository.findMissedTasks(user.getId(), now);

            if (!missedTasks.isEmpty()) {
                StringBuilder emailBody = new StringBuilder();
                emailBody.append("Hi ").append(user.getName()).append(",\n\n");
                emailBody.append("⚠️ You've missed the deadline for these tasks:\n\n");

                for (TasksEntity task : missedTasks) {
                    emailBody.append("❗ ").append(task.getTitle())
                            .append(" — Due on ").append(task.getDueDate())
                            .append("\n");
                }

                emailBody.append("\nDon't forget to catch up! 💡\nYour Task Manager");

                emailService.sendReminderEmail(
                        user.getEmailId(),
                        "⚠️ Missed Task Warning",
                        emailBody.toString()
                );
            }
        }
    }

    @Scheduled(cron = "0 0 7 * * *") // Every day at 7:00 AM
    @Transactional
    public void sendDailyDigest() {
        LocalDate todayStart  = LocalDate.now();

        List<UserEntity> users = userRepository.findAll();

        for (UserEntity user : users) {
            List<TasksEntity> todaysTasks = taskRepository.findTasksDueToday(user.getId(), todayStart);

            if (!todaysTasks.isEmpty()) {
                StringBuilder emailBody = new StringBuilder();
                emailBody.append("Hi ").append(user.getName()).append(",\n\n");
                emailBody.append("🗓️ Here's your Task Summary for Today:\n\n");

                for (TasksEntity task : todaysTasks) {
                    emailBody.append("📌 ").append(task.getTitle())
                            .append(" — Due at ").append(task.getDueDate())
                            .append("\n");
                }

                emailBody.append("\nStay focused 💪\nYour Task Manager");

                emailService.sendReminderEmail(
                        user.getEmailId(),
                        "🗓️ Daily Task Summary",
                        emailBody.toString()
                );
            }
        }
    }


    @Scheduled(cron = "0 0 18 * * *")// Every day at 6:00 PM
    @Transactional
    public void sendTomorrowTaskSummary() {
        LocalDate tomorrowStart = LocalDate.now().plusDays(1);

        List<UserEntity> users = userRepository.findAll();

        for (UserEntity user : users) {
            List<TasksEntity> tasksTomorrow = taskRepository.findTasksDueTomorrow(user.getId(), tomorrowStart);

            if (!tasksTomorrow.isEmpty()) {
                StringBuilder emailBody = new StringBuilder();
                emailBody.append("Hi ").append(user.getName()).append(",\n\n");
                emailBody.append("📅 Here's a preview of your tasks for tomorrow:\n\n");

                for (TasksEntity task : tasksTomorrow) {
                    emailBody.append("📌 ").append(task.getTitle())
                            .append(" — Due at ").append(task.getDueDate())
                            .append("\n");
                }

                emailBody.append("\nPrepare well! 🚀\nYour Task Manager");

                emailService.sendReminderEmail(
                        user.getEmailId(),
                        "📅 Tasks Due Tomorrow",
                        emailBody.toString()
                );
            }
        }
    }
}