import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import AuthLayout from "../components/AuthLayout";
import { api } from "../lib/api";

const initialState = {
  name: "",
  email: "",
  password: "",
  location: ""
};

export default function RegisterPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState(initialState);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  function onChange(event) {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function onSubmit(event) {
    event.preventDefault();
    setLoading(true);
    setError("");
    setSuccess("");

    try {
      const created = await api("/api/jobseekers/register", {
        method: "POST",
        body: JSON.stringify({
          name: form.name.trim(),
          email: form.email.trim(),
          password: form.password,
          location: form.location.trim()
        })
      });

      setSuccess(`Registration successful. Your user ID is ${created.userId}.`);
      setForm(initialState);

      window.setTimeout(() => {
        navigate("/login", { replace: true });
      }, 800);
    } catch (err) {
      const message = String(err.message || "");
      const duplicateAccount =
        message.toLowerCase().includes("already registered") ||
        message.toLowerCase().includes("conflict");

      if (duplicateAccount) {
        setError("Account already exists. Redirecting to login...");
        window.setTimeout(() => {
          navigate("/login", { replace: true });
        }, 1200);
      } else {
        setError(message);
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <AuthLayout
      title="Register"
      subtitle="Create your job seeker account"
      footer={
        <p>
          Already registered? <Link to="/login">Go to login</Link>
        </p>
      }
    >
      <form className="form" onSubmit={onSubmit}>
        <label>
          Full name
          <input name="name" value={form.name} onChange={onChange} required />
        </label>

        <label>
          Email
          <input name="email" type="email" value={form.email} onChange={onChange} required />
        </label>

        <label>
          Password
          <input
            name="password"
            type="password"
            minLength={8}
            value={form.password}
            onChange={onChange}
            required
          />
        </label>

        <label>
          Location
          <input name="location" value={form.location} onChange={onChange} required />
        </label>

        {error ? <p className="msg msg--error">{error}</p> : null}
        {success ? <p className="msg msg--ok">{success}</p> : null}

        <button type="submit" disabled={loading}>
          {loading ? "Registering..." : "Register"}
        </button>
      </form>
    </AuthLayout>
  );
}
