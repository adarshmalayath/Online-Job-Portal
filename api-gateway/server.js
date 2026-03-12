import cors from "cors";
import dotenv from "dotenv";
import express from "express";
import morgan from "morgan";
import { createProxyMiddleware } from "http-proxy-middleware";

dotenv.config();

const app = express();
const PORT = Number(process.env.PORT || 8081);
const BACKEND_URL = process.env.BACKEND_URL || "http://localhost:8080";
const FRONTEND_ORIGIN = process.env.FRONTEND_ORIGIN || "http://localhost:5173";

app.use(morgan("dev"));
app.use(
  cors({
    origin: FRONTEND_ORIGIN,
    methods: ["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"],
    allowedHeaders: ["Content-Type", "Authorization"]
  })
);

app.get("/health", (_req, res) => {
  res.json({
    service: "api-gateway",
    status: "UP",
    backend: BACKEND_URL,
    timestamp: new Date().toISOString()
  });
});

app.post("/auth/login", express.json(), async (req, res) => {
  const email = String(req.body?.email || "").trim().toLowerCase();
  const password = String(req.body?.password || "");

  if (!email || !password) {
    return res.status(400).json({
      error: "Bad Request",
      message: "email and password are required"
    });
  }

  try {
    const usersResponse = await fetch(`${BACKEND_URL}/api/jobseekers`);

    if (!usersResponse.ok) {
      return res.status(502).json({
        error: "Bad Gateway",
        message: "Could not read users from employment-service"
      });
    }

    const users = await usersResponse.json();
    const user = users.find((u) => String(u.email || "").toLowerCase() === email);

    if (!user || String(user.password || "") !== password) {
      return res.status(401).json({
        error: "Unauthorized",
        message: "Invalid email or password"
      });
    }

    return res.json({
      message: "Login successful",
      user: {
        userId: user.userId,
        name: user.name,
        email: user.email,
        location: user.location
      }
    });
  } catch (error) {
    return res.status(502).json({
      error: "Bad Gateway",
      message: "Employment service is unavailable",
      details: error.message
    });
  }
});

app.use(
  "/api",
  createProxyMiddleware({
    target: BACKEND_URL,
    changeOrigin: true,
    secure: false,
    pathRewrite: (path) => `/api${path}`,
    timeout: 10_000,
    proxyTimeout: 10_000,
    on: {
      error: (err, req, res) => {
        if (!res.headersSent) {
          res.status(502).json({
            error: "Bad Gateway",
            message: "Employment service is unavailable",
            details: err.message,
            path: req.originalUrl
          });
        }
      }
    }
  })
);

app.use((_req, res) => {
  res.status(404).json({ error: "Not Found" });
});

app.listen(PORT, () => {
  console.log(`API gateway running on http://localhost:${PORT}`);
  console.log(`Proxy target: ${BACKEND_URL}`);
});
