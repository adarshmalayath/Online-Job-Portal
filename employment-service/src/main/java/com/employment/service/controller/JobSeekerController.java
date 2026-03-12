package com.employment.service.controller;

import com.employment.service.dto.ProfileRequest;
import com.employment.service.dto.RegisterRequest;
import com.employment.service.model.JobSeeker;
import com.employment.service.model.MatchResult;
import com.employment.service.model.Profile;
import com.employment.service.service.EmploymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ReqInt – User-facing REST interface.
 * Provides endpoints for registration, profile management,
 * job matching and course recommendations.
 */
@RestController
@RequestMapping("/api/jobseekers")
@Tag(name = "Job Seekers", description = "ReqInt – User registration, profile and matching operations")
public class JobSeekerController {

    private final EmploymentService service;

    public JobSeekerController(EmploymentService service) {
        this.service = service;
    }

    // ─── POST /api/jobseekers/register ─────────────────────────────────────────

    @PostMapping("/register")
    @Operation(summary = "Register a new job seeker",
               description = "Pre: email not already registered, password ≥ 8 chars. " +
                             "Post: account created, user stored.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    public ResponseEntity<JobSeeker> register(@Valid @RequestBody RegisterRequest req) {
        JobSeeker js = service.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(js);
    }

    // ─── GET /api/jobseekers ────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "List all registered job seekers")
    public ResponseEntity<List<JobSeeker>> getAllJobSeekers() {
        return ResponseEntity.ok(service.getAllJobSeekers());
    }

    // ─── GET /api/jobseekers/{userId} ──────────────────────────────────────────

    @GetMapping("/{userId}")
    @Operation(summary = "Get a job seeker by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<JobSeeker> getJobSeeker(
            @PathVariable @Parameter(description = "User ID") Long userId) {
        return ResponseEntity.ok(service.getJobSeeker(userId));
    }

    // ─── PUT /api/jobseekers/{userId}/profile ──────────────────────────────────

    @PutMapping("/{userId}/profile")
    @Operation(summary = "Create or update the user's profile",
               description = "Pre: userId exists, skills not empty, location valid. " +
                             "Post: profile saved/updated.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Profile> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody ProfileRequest req) {
        return ResponseEntity.ok(service.updateProfile(userId, req));
    }

    // ─── GET /api/jobseekers/{userId}/profile ──────────────────────────────────

    @GetMapping("/{userId}/profile")
    @Operation(summary = "Get the profile of a job seeker")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile found"),
        @ApiResponse(responseCode = "404", description = "Profile or user not found")
    })
    public ResponseEntity<Profile> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getProfile(userId));
    }

    // ─── GET /api/jobseekers/{userId}/match ────────────────────────────────────

    @GetMapping("/{userId}/match")
    @Operation(summary = "Match jobs to the user's profile and recommend courses",
               description = "Pre: profile exists. " +
                             "Post: ranked list of jobs with match scores and recommended courses.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Match results returned"),
        @ApiResponse(responseCode = "404", description = "Profile or user not found")
    })
    public ResponseEntity<List<MatchResult>> matchJobs(@PathVariable Long userId) {
        return ResponseEntity.ok(service.matchJobs(userId));
    }
}
