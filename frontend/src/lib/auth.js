const AUTH_KEY = "employment.currentUser";

export function getCurrentUser() {
  const raw = localStorage.getItem(AUTH_KEY);
  if (!raw) return null;

  try {
    return JSON.parse(raw);
  } catch {
    return null;
  }
}

export function setCurrentUser(user) {
  localStorage.setItem(AUTH_KEY, JSON.stringify(user));
}

export function clearCurrentUser() {
  localStorage.removeItem(AUTH_KEY);
}
