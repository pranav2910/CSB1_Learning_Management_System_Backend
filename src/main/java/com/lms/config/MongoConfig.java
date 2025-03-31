package com.lms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing // Enables createdAt/updatedAt auto-generation
@EnableMongoRepositories(basePackages = "com.lms.repository")
public class MongoConfig {
}