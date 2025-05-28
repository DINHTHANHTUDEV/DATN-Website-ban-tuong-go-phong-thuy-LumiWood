// auth.js – Pinia store kết hợp gọi API đăng nhập / đăng ký backend

import { defineStore } from 'pinia';
import apiClient from '@/http/axios';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: JSON.parse(localStorage.getItem('user')) || null,
    token: localStorage.getItem('token') || null,
    loginError: null,
    loading: false,
    returnUrl: null
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.user?.role === 'ROLE_ADMIN'
  },

  actions: {
    /**
     * Gọi API backend để đăng nhập
     * Gắn token + user vào localStorage
     */
    async login(username, password) {
      this.loading = true;
      this.loginError = null;

      try {
        const response = await apiClient.post('/api/auth/login', {
          username,
          password
        });

        const { token, role } = response.data;

        this.token = token;
        this.user = { username, role };

        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(this.user));

        return true;
      } catch (error) {
        this.loginError = error.response?.data || 'Đăng nhập thất bại';
        return false;
      } finally {
        this.loading = false;
      }
    },

    /**
     * Gọi API backend để đăng ký người dùng mới
     */
    async register(registerData) {
      try {
        const response = await apiClient.post('/api/auth/register', registerData);
        return response.data; // "Đăng ký thành công" hoặc lỗi
      } catch (error) {
        throw error.response?.data || 'Đăng ký thất bại';
      }
    },

    /**
     * Xoá token và user khỏi localStorage và state
     */
    logout() {
      this.token = null;
      this.user = null;
      this.loginError = null;
      this.returnUrl = null;

      localStorage.removeItem('token');
      localStorage.removeItem('user');
    },

    /**
     * Lưu URL cần redirect sau khi đăng nhập
     */
    setReturnUrl(url) {
      this.returnUrl = url;
    }
  }
});
