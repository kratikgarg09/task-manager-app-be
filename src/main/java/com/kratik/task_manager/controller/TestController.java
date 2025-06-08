package com.kratik.task_manager.controller;

import com.kratik.task_manager.services.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final ReminderService reminderService;

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, authenticated user!";
    }

    @PostMapping("/reminder")
    public void sentReminder(){
        reminderService.checkReminders();
    }
}
