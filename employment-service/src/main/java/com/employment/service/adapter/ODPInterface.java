package com.employment.service.adapter;

import com.employment.service.model.Job;
import java.util.List;

/**
 * ODPInt – Required interface for the Open Data Portal.
 * Follows a top-down approach: our application depends on this
 * interface; concrete adapters implement it against real or mock data.
 */
public interface ODPInterface {
    /**
     * Fetch job postings from the data portal filtered by location and optional keyword.
     * @param location city or region (e.g. "Leicester")
     * @param keyword  optional search keyword (e.g. "Java")
     * @return list of matching Job objects
     */
    List<Job> getJobPostings(String location, String keyword);

    /**
     * Fetch a single job by its ID.
     * @param jobId unique job identifier
     * @return Job or null if not found
     */
    Job getJobDetails(int jobId);
}
