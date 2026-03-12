package com.employment.service.service;

import com.employment.service.adapter.CourseAPIInterface;
import com.employment.service.adapter.ODPInterface;
import com.employment.service.dto.ProfileRequest;
import com.employment.service.dto.RegisterRequest;
import com.employment.service.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmploymentService {

    private final JobSeekerRepository jobSeekerRepo;
    private final ProfileRepository profileRepo;
    private final ODPInterface odpAdapter;
    private final CourseAPIInterface courseAdapter;

    public EmploymentService(JobSeekerRepository jobSeekerRepo,
                             ProfileRepository profileRepo,
                             ODPInterface odpAdapter,
                             CourseAPIInterface courseAdapter) {
        this.jobSeekerRepo = jobSeekerRepo;
        this.profileRepo = profileRepo;
        this.odpAdapter = odpAdapter;
        this.courseAdapter = courseAdapter;
    }

    // ─── Operation 1: Register User ────────────────────────────────────────────

    public JobSeeker register(RegisterRequest req) {
        if (jobSeekerRepo.existsByEmail(req.email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Email already registered: " + req.email);
        }
        JobSeeker js = new JobSeeker(req.name, req.email, req.password, req.location);
        return jobSeekerRepo.save(js);
    }

    public JobSeeker getJobSeeker(Long userId) {
        return jobSeekerRepo.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User not found: " + userId));
    }

    public List<JobSeeker> getAllJobSeekers() {
        return jobSeekerRepo.findAll();
    }

    // ─── Operation 2: Manage Profile ───────────────────────────────────────────

    public Profile updateProfile(Long userId, ProfileRequest req) {
        JobSeeker js = getJobSeeker(userId);
        // Update location
        js.setLocation(req.location);
        jobSeekerRepo.save(js);

        // Upsert profile
        Profile profile = profileRepo.findByJobSeekerUserId(userId)
            .orElse(new Profile());
        profile.setJobSeeker(js);
        profile.setSkills(req.skills);
        profile.setPreferences(req.preferences);
        return profileRepo.save(profile);
    }

    public Profile getProfile(Long userId) {
        return profileRepo.findByJobSeekerUserId(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Profile not found for user: " + userId));
    }

    // ─── Operation 3: Fetch Local Jobs ─────────────────────────────────────────

    public List<Job> searchJobs(String location, String keyword) {
        return odpAdapter.getJobPostings(location, keyword);
    }

    public Job getJobDetails(int jobId) {
        Job job = odpAdapter.getJobDetails(jobId);
        if (job == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Job not found: " + jobId);
        return job;
    }

    // ─── Operation 4: Match Jobs + Recommend Courses ───────────────────────────

    public List<MatchResult> matchJobs(Long userId) {
        Profile profile = getProfile(userId);
        JobSeeker js = profile.getJobSeeker();
        List<String> userSkills = profile.getSkills()
            .stream().map(String::toLowerCase).toList();

        // Fetch jobs from the ODP adapter for the user's location
        List<Job> jobs = odpAdapter.getJobPostings(js.getLocation(), null);

        // Score each job
        List<MatchResult> results = new ArrayList<>();
        for (Job job : jobs) {
            List<String> required = job.getRequiredSkills()
                .stream().map(String::toLowerCase).toList();
            List<String> missing = required.stream()
                .filter(s -> !userSkills.contains(s))
                .map(s -> capitalise(s))
                .collect(Collectors.toList());
            long matched = required.stream().filter(userSkills::contains).count();
            int score = required.isEmpty() ? 0 :
                (int) Math.round((double) matched / required.size() * 100);

            // Fetch recommended courses for missing skills
            List<Course> courses = courseAdapter.getCourses(missing);

            results.add(new MatchResult(score, missing, job, courses));
        }

        // Sort by score descending
        results.sort(Comparator.comparingInt(MatchResult::getMatchScore).reversed());
        return results;
    }

    // ─── Courses endpoint ──────────────────────────────────────────────────────

    public List<Course> getCourses(List<String> skills) {
        return courseAdapter.getCourses(skills);
    }

    public Course getCourseDetails(int courseId) {
        Course c = courseAdapter.getCourseDetails(courseId);
        if (c == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Course not found: " + courseId);
        return c;
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private String capitalise(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
