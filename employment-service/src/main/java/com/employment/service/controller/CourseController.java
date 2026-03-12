package com.employment.service.controller;

import com.employment.service.model.Course;
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
 * CAPInt – Courses endpoint backed by the Course API adapter.
 */
@RestController
@RequestMapping("/api/courses")
@Tag(name = "Courses", description = "CAPInt – Retrieve courses for missing skills via the Course API adapter")
public class CourseController {

    private final EmploymentService service;

    public CourseController(EmploymentService service) {
        this.service = service;
    }

    // ─── GET /api/courses ───────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Get courses covering specified skills",
               description = "Supply one or more skill names to retrieve relevant upskilling courses.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Courses returned successfully")
    })
    public ResponseEntity<List<Course>> getCourses(
            @RequestParam @Parameter(description = "List of skills, e.g. skills=Docker&skills=Python")
            List<String> skills) {
        return ResponseEntity.ok(service.getCourses(skills));
    }

    // ─── GET /api/courses/{courseId} ────────────────────────────────────────────

    @GetMapping("/{courseId}")
    @Operation(summary = "Get details of a specific course")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Course found"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<Course> getCourseDetails(
            @PathVariable @Parameter(description = "Course ID") int courseId) {
        return ResponseEntity.ok(service.getCourseDetails(courseId));
    }
}
