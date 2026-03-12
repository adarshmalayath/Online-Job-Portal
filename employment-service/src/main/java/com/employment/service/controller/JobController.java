package com.employment.service.controller;

import com.employment.service.model.Job;
import com.employment.service.service.EmploymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ODPInt – Jobs endpoint backed by the Open Data Portal adapter.
 */
@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Jobs", description = "ODPInt – Fetch job postings from the Open Data Portal adapter")
public class JobController {

    private final EmploymentService service;

    public JobController(EmploymentService service) {
        this.service = service;
    }

    // ─── GET /api/jobs ──────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Search for job postings",
               description = "Pre: location provided OR keyword provided. " +
                             "Post: job postings list returned with title, location, requiredSkills.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Jobs returned successfully")
    })
    public ResponseEntity<List<Job>> searchJobs(
            @RequestParam(required = false)
            @Parameter(description = "Filter by city, e.g. Leicester") String location,
            @RequestParam(required = false)
            @Parameter(description = "Keyword to search job titles or skills") String keyword) {
        return ResponseEntity.ok(service.searchJobs(location, keyword));
    }

    // ─── GET /api/jobs/{jobId} ──────────────────────────────────────────────────

    @GetMapping("/{jobId}")
    @Operation(summary = "Get details of a specific job")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Job found"),
        @ApiResponse(responseCode = "404", description = "Job not found")
    })
    public ResponseEntity<Job> getJobDetails(
            @PathVariable @Parameter(description = "Job ID") int jobId) {
        return ResponseEntity.ok(service.getJobDetails(jobId));
    }
}
