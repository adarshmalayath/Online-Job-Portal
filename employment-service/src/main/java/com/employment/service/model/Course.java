package com.employment.service.model;

public class Course {
    private int courseId;
    private String courseName;
    private String provider;
    private String skillCovered;
    private String duration;

    public Course() {}

    public Course(int courseId, String courseName, String provider, String skillCovered, String duration) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.provider = provider;
        this.skillCovered = skillCovered;
        this.duration = duration;
    }

    // Getters and setters
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getSkillCovered() { return skillCovered; }
    public void setSkillCovered(String skillCovered) { this.skillCovered = skillCovered; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}
