package com.employment.service.adapter;

import com.employment.service.model.Course;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MockCourseAPIAdapter – Implements CourseAPIInterface with in-memory data.
 * In production this would call an external course platform API
 * (e.g. Coursera, Udemy, FutureLearn).
 */
@Component
public class MockCourseAPIAdapter implements CourseAPIInterface {

    private final List<Course> courseDatabase = List.of(
        new Course(301, "Docker Fundamentals",          "Udemy",        "Docker",      "8 hours"),
        new Course(302, "Kubernetes for Beginners",     "Coursera",     "Kubernetes",  "20 hours"),
        new Course(303, "Spring Boot Masterclass",      "Udemy",        "Spring",      "15 hours"),
        new Course(304, "Python for Data Science",      "FutureLearn",  "Python",      "12 hours"),
        new Course(305, "SQL Bootcamp",                 "Udemy",        "SQL",         "10 hours"),
        new Course(306, "React – The Complete Guide",   "Udemy",        "React",       "40 hours"),
        new Course(307, "JavaScript Essentials",        "Coursera",     "JavaScript",  "18 hours"),
        new Course(308, "Linux Command Line Basics",    "edX",          "Linux",       "6 hours"),
        new Course(309, "TensorFlow Developer Cert",    "Coursera",     "TensorFlow",  "30 hours"),
        new Course(310, "Git & GitHub for Developers",  "Udemy",        "Git",         "5 hours"),
        new Course(311, "Java Programming Masterclass", "Udemy",        "Java",        "35 hours"),
        new Course(312, "Oracle Database Fundamentals", "Oracle",       "Oracle",      "14 hours")
    );

    @Override
    public List<Course> getCourses(List<String> missingSkills) {
        if (missingSkills == null || missingSkills.isEmpty()) return List.of();
        List<String> lower = missingSkills.stream().map(String::toLowerCase).toList();
        return courseDatabase.stream()
            .filter(c -> lower.contains(c.getSkillCovered().toLowerCase()))
            .collect(Collectors.toList());
    }

    @Override
    public Course getCourseDetails(int courseId) {
        return courseDatabase.stream()
            .filter(c -> c.getCourseId() == courseId)
            .findFirst()
            .orElse(null);
    }
}
