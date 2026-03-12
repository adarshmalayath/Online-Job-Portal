import { Link } from "react-router-dom";

export default function AuthLayout({ title, subtitle, children, footer }) {
  return (
    <main className="auth-page">
      <section className="auth-card">
        <div className="auth-head">
          <p className="eyebrow">Employment Service</p>
          <h1>{title}</h1>
          <p>{subtitle}</p>
        </div>

        {children}

        {footer ? <div className="auth-footer">{footer}</div> : null}

        <Link className="back-link" to="/">
          Home
        </Link>
      </section>
    </main>
  );
}
