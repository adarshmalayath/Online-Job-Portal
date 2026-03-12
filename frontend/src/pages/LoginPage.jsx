import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import AuthLayout from "../components/AuthLayout";
import { api } from "../lib/api";
import { setCurrentUser } from "../lib/auth";

export default function LoginPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  function onChange(event) {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function onSubmit(event) {
    event.preventDefault();
    setLoading(true);
    setError("");

    try {
      const payload = await api("/auth/login", {
        method: "POST",
        body: JSON.stringify({
          email: form.email.trim(),
          password: form.password
        })
      });

      setCurrentUser(payload.user);
      navigate("/dashboard", { replace: true });
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <AuthLayout
      title="Login"
      subtitle="Use your registered email and password"
      footer={
        <p>
          New user? <Link to="/register">Create an account</Link>
        </p>
      }
    >
      <form className="form" onSubmit={onSubmit}>
        <label>
          Email
          <input name="email" type="email" value={form.email} onChange={onChange} required />
        </label>

        <label>
          Password
          <input name="password" type="password" value={form.password} onChange={onChange} required />
        </label>

        {error ? <p className="msg msg--error">{error}</p> : null}

        <button type="submit" disabled={loading}>
          {loading ? "Signing in..." : "Login"}
        </button>
      </form>
    </AuthLayout>
  );
}
