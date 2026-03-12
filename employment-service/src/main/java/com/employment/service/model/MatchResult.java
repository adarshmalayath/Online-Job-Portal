package com.employment.service.model;

import java.util.List;

public class MatchResult {
    private int matchScore;
    private List<String> missingSkills;
    private Job job;
    private List<Course> recommendedCourses;

    public MatchResult() {}

    public MatchResult(int matchScore, List<String> missingSkills, Job job, List<Course> recommendedCourses) {
        this.matchScore = matchScore;
        this.missingSkills = missingSkills;
        this.job = job;
        this.recommendedCourses = recommendedCourses;
    }

    // Getters and setters
    public int getMatchScore() { return matchScore; }
    public void setMatchScore(int matchScore) { this.matchScore = matchScore; }
    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }
    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }
    public List<Course> getRecommendedCourses() { return recommendedCourses; }
    public void setRecommendedCourses(List<Course> recommendedCourses) { this.recommendedCourses = recommendedCourses; }
}
