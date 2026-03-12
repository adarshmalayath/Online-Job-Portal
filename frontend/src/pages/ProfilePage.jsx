import { useEffect, useState } from "react";
import AppShell from "../components/AppShell";
import { api } from "../lib/api";
import { getCurrentUser, setCurrentUser } from "../lib/auth";

function toStringSkills(skills = []) {
  return skills.join(", ");
}

function parseSkills(raw) {
  return raw
    .split(",")
    .map((s) => s.trim())
    .filter(Boolean);
}

export default function ProfilePage() {
  const currentUser = getCurrentUser();
  const [form, setForm] = useState({
    location: currentUser?.location || "",
    preferences: "",
    skills: ""
  });
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  useEffect(() => {
    let active = true;

    async function loadProfile() {
      setLoading(true);
      setError("");

      try {
        const profile = await api(`/api/jobseekers/${currentUser.userId}/profile`);
        if (!active) return;

        setForm({
          location: profile.jobSeeker?.location || currentUser.location || "",
          preferences: profile.preferences || "",
          skills: toStringSkills(profile.skills || [])
        });
      } catch (err) {
        if (!active) return;
        if (!String(err.message || "").toLowerCase().includes("not found")) {
          setError(err.message);
        }
      } finally {
        if (active) setLoading(false);
      }
    }

    loadProfile();

    return () => {
      active = false;
    };
  }, [currentUser.userId, currentUser.location]);

  function onChange(event) {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function onSubmit(event) {
    event.preventDefault();
    setSaving(true);
    setError("");
    setSuccess("");

    try {
      const payload = {
        location: form.location.trim(),
        preferences: form.preferences.trim(),
        skills: parseSkills(form.skills)
      };

      if (payload.skills.length === 0) {
        throw new Error("At least one skill is required.");
      }

      await api(`/api/jobseekers/${currentUser.userId}/profile`, {
        method: "PUT",
        body: JSON.stringify(payload)
      });

      setCurrentUser({
        ...currentUser,
        location: payload.location
      });

      setSuccess("Profile updated successfully.");
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <AppShell title="Update Profile" subtitle="Keep your skills and preferences up to date for better matches">
      <article className="card card--single">
        <h2>Profile</h2>

        <form className="form" onSubmit={onSubmit}>
          <section className="readonly-section">
            <p className="item-subtitle">Account Info</p>
            <div className="readonly-grid">
              <label>
                User ID
                <input className="readonly-input" value={currentUser?.userId ?? ""} readOnly />
              </label>

              <label>
                Name
                <input className="readonly-input" value={currentUser?.name || ""} readOnly />
              </label>

              <label>
                Email
                <input className="readonly-input" value={currentUser?.email || ""} readOnly />
              </label>
            </div>
          </section>

          <label>
            Skills (comma separated)
            <input
              name="skills"
              value={form.skills}
              onChange={onChange}
              placeholder="Java, SQL, Docker"
              disabled={loading}
              required
            />
          </label>

          <label>
            Preferences
            <input
              name="preferences"
              value={form.preferences}
              onChange={onChange}
              placeholder="Backend roles"
              disabled={loading}
            />
          </label>

          <label>
            Location
            <input
              name="location"
              value={form.location}
              onChange={onChange}
              placeholder="Leicester"
              disabled={loading}
              required
            />
          </label>

          {error ? <p className="msg msg--error">{error}</p> : null}
          {success ? <p className="msg msg--ok">{success}</p> : null}

          <button type="submit" disabled={saving || loading}>
            {saving ? "Saving..." : "Update Profile"}
          </button>
        </form>
      </article>
    </AppShell>
  );
}
