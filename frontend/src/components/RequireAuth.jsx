import { Navigate, useLocation } from "react-router-dom";
import { getCurrentUser } from "../lib/auth";

export default function RequireAuth({ children }) {
  const user = getCurrentUser();
  const location = useLocation();

  if (!user) {
    return <Navigate to="/login" state={{ from: location.pathname }} replace />;
  }

  return children;
}
