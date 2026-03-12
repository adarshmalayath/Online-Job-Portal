import { Link } from "react-router-dom";

export default function HomePage() {
  return (
    <main className="auth-page">
      <section className="auth-card">
        <div className="auth-head">
          <p className="eyebrow">Employment Service</p>
          <h1>Frontend Pages</h1>
          <p>Use these routes for your assignment demo.</p>
        </div>

        <div className="route-list">
          <Link className="pill" to="/register">
            /register
          </Link>
          <Link className="pill" to="/login">
            /login
          </Link>
          <Link className="pill" to="/dashboard">
            /dashboard
          </Link>
          <Link className="pill" to="/profile">
            /profile
          </Link>
        </div>
      </section>
    </main>
  );
}
