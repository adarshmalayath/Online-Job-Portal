package com.employment.service.model;

import java.util.List;

public class Job {
    private int jobId;
    private String title;
    private String company;
    private String location;
    private List<String> requiredSkills;

    public Job() {}

    public Job(int jobId, String title, String company, String location, List<String> requiredSkills) {
        this.jobId = jobId;
        this.title = title;
        this.company = company;
        this.location = location;
        this.requiredSkills = requiredSkills;
    }

    // Getters and setters
    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
}
