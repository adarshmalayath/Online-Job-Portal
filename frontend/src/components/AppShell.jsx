import { Link, useNavigate } from "react-router-dom";
import { clearCurrentUser, getCurrentUser } from "../lib/auth";

export default function AppShell({ title, subtitle, children }) {
  const user = getCurrentUser();
  const navigate = useNavigate();

  function logout() {
    clearCurrentUser();
    navigate("/login", { replace: true });
  }

  return (
    <div className="shell">
      <header className="topbar">
        <div>
          <p className="eyebrow">Employment Service Portal</p>
          <h1>{title}</h1>
          <p className="subcopy">{subtitle}</p>
        </div>

        <nav className="top-actions">
          <Link className="pill" to="/dashboard">
            Dashboard
          </Link>
          <Link className="pill" to="/profile">
            Update Profile
          </Link>
          <button className="pill pill--danger" onClick={logout} type="button">
            Logout
          </button>
        </nav>
      </header>

      <section className="whoami">
        Logged in as <strong>{user?.name}</strong> ({user?.email}) · User ID <strong>{user?.userId}</strong>
      </section>

      <main className="content-grid">{children}</main>
    </div>
  );
}
