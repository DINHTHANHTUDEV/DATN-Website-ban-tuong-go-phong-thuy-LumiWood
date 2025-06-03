import apiClient from "@/http/axiosArticle.js";

export const getAdminArticles = (params) => {
  return apiClient.get("/admin/articles", { params });
};

export const getAdminArticleById = (id) => {
  if (!id) return Promise.reject(new Error("Article ID is required"));
  return apiClient.get(`/admin/articles/${id}`);
};

export const createAdminArticle = (articleData) => {
  return apiClient.post("/admin/articles", articleData);
};

export const updateAdminArticle = (id, articleData) => {
  if (!id) return Promise.reject(new Error("Article ID is required"));
  return apiClient.put(`/admin/articles/${id}`, articleData);
};

export const deleteAdminArticle = (id) => {
  if (!id) return Promise.reject(new Error("Article ID is required"));
  return apiClient.delete(`/admin/articles/${id}`);
};

export const publishAdminArticle = (id) => {
  if (!id) return Promise.reject(new Error("Article ID is required"));
  return apiClient.patch(`/admin/articles/${id}/publish`);
};

export const unpublishAdminArticle = (id) => {
  if (!id) return Promise.reject(new Error("Article ID is required"));
  return apiClient.patch(`/admin/articles/${id}/unpublish`);
};
