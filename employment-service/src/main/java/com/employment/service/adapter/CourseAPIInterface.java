package com.employment.service.adapter;

import com.employment.service.model.Course;
import java.util.List;

/**
 * CAPInt – Required interface for the Course API.
 * Top-down approach: any course provider implementing this interface
 * can be plugged in without changing the core application.
 */
public interface CourseAPIInterface {
    /**
     * Retrieve courses that cover any of the specified missing skills.
     * @param missingSkills list of skill names the user lacks
     * @return list of recommended Course objects
     */
    List<Course> getCourses(List<String> missingSkills);

    /**
     * Retrieve details of a specific course.
     * @param courseId unique course identifier
     * @return Course or null if not found
     */
    Course getCourseDetails(int courseId);
}
