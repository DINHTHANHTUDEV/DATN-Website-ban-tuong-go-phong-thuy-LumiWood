// src/axios.js

import axios from "axios";

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

// Nếu bạn dùng token auth (JWT) thì giữ phần này, không thì bỏ.
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem("token"); // hoặc dùng pinia/vuex nếu bạn dùng authStore
  if (token) {
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  return config;
});

export default apiClient;
