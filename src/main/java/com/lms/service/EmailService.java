package com.lms.service;

import com.lms.model.User;

public interface EmailService {
    void sendWelcomeEmail(User user);
    void sendPasswordResetEmail(User user, String resetToken);
}