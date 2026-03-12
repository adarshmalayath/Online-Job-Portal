package com.employment.service.adapter;

import com.employment.service.model.Job;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * MockODPAdapter – Implements ODPInterface with in-memory data.
 * In production this would call the real Open Data Portal REST API
 * (e.g. https://data.gov.uk or a local authority jobs feed).
 *
 * Top-down design: this class is the "glue" adapter that maps the
 * external API response format to our required ODPInterface contract.
 */
@Component
public class MockODPAdapter implements ODPInterface {

    private final List<Job> jobDatabase = List.of(
        new Job(101, "Backend Developer",    "Tech Ltd",       "Leicester", List.of("Java", "Spring", "Docker")),
        new Job(102, "Data Analyst",         "DataCorp",       "Leicester", List.of("Python", "SQL", "Excel")),
        new Job(103, "DevOps Engineer",      "CloudOps Ltd",   "Leicester", List.of("Docker", "Kubernetes", "Linux")),
        new Job(104, "Frontend Developer",   "WebAgency",      "Leicester", List.of("JavaScript", "React", "CSS")),
        new Job(105, "Software Engineer",    "InnovateTech",   "Coventry",  List.of("Java", "Python", "Git")),
        new Job(106, "ML Engineer",          "AI Solutions",   "Birmingham",List.of("Python", "TensorFlow", "SQL")),
        new Job(107, "Full Stack Developer", "StartupXYZ",     "Leicester", List.of("Java", "React", "SQL", "Docker")),
        new Job(108, "DBA",                  "DataSystems",    "Leicester", List.of("SQL", "Oracle", "Linux"))
    );

    @Override
    public List<Job> getJobPostings(String location, String keyword) {
        return jobDatabase.stream()
            .filter(j -> location == null || j.getLocation().equalsIgnoreCase(location))
            .filter(j -> keyword == null || keyword.isBlank() ||
                         j.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                         j.getRequiredSkills().stream().anyMatch(s -> s.toLowerCase().contains(keyword.toLowerCase())))
            .toList();
    }

    @Override
    public Job getJobDetails(int jobId) {
        return jobDatabase.stream()
            .filter(j -> j.getJobId() == jobId)
            .findFirst()
            .orElse(null);
    }
}
