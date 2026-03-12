import { useState } from "react";
import AppShell from "../components/AppShell";
import { api } from "../lib/api";
import { getCurrentUser } from "../lib/auth";

function getCourseLink(course) {
  const provider = String(course?.provider || "").toLowerCase();
  const courseName = String(course?.courseName || "").trim();
  const query = encodeURIComponent(courseName);

  if (provider.includes("udemy")) return `https://www.udemy.com/courses/search/?q=${query}`;
  if (provider.includes("coursera")) return `https://www.coursera.org/search?query=${query}`;
  if (provider.includes("futurelearn")) return `https://www.futurelearn.com/search?q=${query}`;
  if (provider.includes("edx")) return `https://www.edx.org/search?q=${query}`;
  if (provider.includes("oracle")) return `https://education.oracle.com/searchresults?Ntt=${query}`;

  const fallbackQuery = encodeURIComponent(`${course?.provider || ""} ${courseName}`.trim());
  return `https://www.google.com/search?q=${fallbackQuery}`;
}

export default function DashboardPage() {
  const user = getCurrentUser();
  const [jobFilters, setJobFilters] = useState({
    location: user?.location || "",
    keyword: ""
  });
  const [jobs, setJobs] = useState([]);
  const [matches, setMatches] = useState([]);
  const [jobsError, setJobsError] = useState("");
  const [matchError, setMatchError] = useState("");
  const [jobsLoading, setJobsLoading] = useState(false);
  const [matchLoading, setMatchLoading] = useState(false);

  function updateFilter(event) {
    const { name, value } = event.target;
    setJobFilters((prev) => ({ ...prev, [name]: value }));
  }

  async function searchJobs(event) {
    event.preventDefault();
    setJobsLoading(true);
    setJobsError("");

    try {
      const params = new URLSearchParams();
      if (jobFilters.location.trim()) params.set("location", jobFilters.location.trim());
      if (jobFilters.keyword.trim()) params.set("keyword", jobFilters.keyword.trim());

      const result = await api(`/api/jobs${params.toString() ? `?${params.toString()}` : ""}`);
      setJobs(result || []);
    } catch (err) {
      setJobsError(err.message);
    } finally {
      setJobsLoading(false);
    }
  }

  async function loadMatches() {
    setMatchLoading(true);
    setMatchError("");

    try {
      const result = await api(`/api/jobseekers/${user.userId}/match`);
      setMatches(result || []);
    } catch (err) {
      setMatchError(err.message);
    } finally {
      setMatchLoading(false);
    }
  }

  return (
    <AppShell title="Job Dashboard" subtitle="Search local jobs and get personalized matches">
      <article className="card">
        <h2>Job Search</h2>

        <form className="form form--inline" onSubmit={searchJobs}>
          <label>
            Location
            <input name="location" value={jobFilters.location} onChange={updateFilter} placeholder="Leicester" />
          </label>

          <label>
            Keyword
            <input name="keyword" value={jobFilters.keyword} onChange={updateFilter} placeholder="Java" />
          </label>

          <button type="submit" disabled={jobsLoading}>
            {jobsLoading ? "Searching..." : "Search Jobs"}
          </button>
        </form>

        {jobsError ? <p className="msg msg--error">{jobsError}</p> : null}

        <div className="stack-list">
          {jobs.length === 0 ? <p className="empty">No jobs loaded yet.</p> : null}
          {jobs.map((job) => (
            <div className="item" key={job.jobId}>
              <div className="item-head">
                <h3>{job.title}</h3>
                <span className="tag">#{job.jobId}</span>
              </div>
              <p>
                <strong>{job.company}</strong> · {job.location}
              </p>
              <div className="chips">
                {(job.requiredSkills || []).map((skill) => (
                  <span className="chip" key={`${job.jobId}-${skill}`}>
                    {skill}
                  </span>
                ))}
              </div>
            </div>
          ))}
        </div>
      </article>

      <article className="card">
        <div className="item-head">
          <h2>My Match Results</h2>
          <button type="button" onClick={loadMatches} disabled={matchLoading}>
            {matchLoading ? "Loading..." : "Load Matches"}
          </button>
        </div>

        {matchError ? <p className="msg msg--error">{matchError}</p> : null}

        <div className="stack-list">
          {matches.length === 0 ? <p className="empty">No matches loaded yet.</p> : null}

          {matches.map((m, idx) => (
            <div className="item" key={`${m.job?.jobId || idx}-${idx}`}>
              <div className="item-head">
                <h3>{m.job?.title || "Unknown Job"}</h3>
                <span className="tag tag--score">{m.matchScore}%</span>
              </div>

              <p>
                <strong>{m.job?.company}</strong> · {m.job?.location}
              </p>

              <p className="item-subtitle">Missing Skills</p>
              <div className="chips">
                {(m.missingSkills || []).length === 0 ? <span className="empty">No gaps</span> : null}
                {(m.missingSkills || []).map((skill) => (
                  <span className="chip chip--warn" key={`${m.job?.jobId}-${skill}`}>
                    {skill}
                  </span>
                ))}
              </div>

              <p className="item-subtitle">Recommended Courses</p>
              <ul className="courses">
                {(m.recommendedCourses || []).length === 0 ? <li className="empty">No courses required</li> : null}
                {(m.recommendedCourses || []).map((course) => (
                  <li key={`${m.job?.jobId}-${course.courseId}`}>
                    <a className="course-link" href={getCourseLink(course)} target="_blank" rel="noreferrer">
                      {course.courseName}
                    </a>{" "}
                    ({course.provider}) - {course.skillCovered}
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>
      </article>
    </AppShell>
  );
}
