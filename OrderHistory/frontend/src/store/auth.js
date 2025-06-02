import { defineStore } from 'pinia';
import apiClient from '@/http/axios';
import { useCartStore } from './cart';
import { clearCartSessionId } from '@/utils/cartSession';

const getStoredUser = () => {
  const user = localStorage.getItem('user');
  return user ? JSON.parse(user) : null;
};
const getStoredToken = () => localStorage.getItem('token');

const MOCK_ADMIN_TOKEN = "fake-admin-jwt-token-tuong-go-phong-thuy-admin123";
const MOCK_USER_TOKEN = "fake-user-jwt-token-tuong-go-phong-thuy-user123";

const MOCK_ADMIN_PROFILE = {
  username: "admin",
  role: "ROLE_ADMIN",
  fullName: "Quản Trị Viên Phong Thủy",
  email: "admin@tuonggophongthuy.mock",
};

const MOCK_USER_PROFILE = {
  username: "user",
  role: "ROLE_USER",
  fullName: "Khách Hàng Thân Thiết",
  email: "user@tuonggophongthuy.mock",
};


export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: getStoredUser(),
    token: getStoredToken(),
    returnUrl: null,
    loginError: null,
    loading: false
  }),

  getters: {
    isAuthenticated: (state) => !!state.token && !!state.user,
    isAdmin: (state) => state.user?.role === 'ROLE_ADMIN',
    authHeader: (state) => state.token ? { Authorization: `Bearer ${state.token}` } : {}
  },

  actions: {
    async login(username, password) {
      this.loading = true;
      this.loginError = null;

      return new Promise((resolve) => {
        setTimeout(() => {
          let success = false;
          let loggedInUsername;
          let role;
          let accessToken;

          if (username === 'admin' && password === '123') {
            loggedInUsername = MOCK_ADMIN_PROFILE.username;
            role = MOCK_ADMIN_PROFILE.role;
            accessToken = MOCK_ADMIN_TOKEN;
            success = true;
          } else if (username === 'user' && password === '123') {
            loggedInUsername = MOCK_USER_PROFILE.username;
            role = MOCK_USER_PROFILE.role;
            accessToken = MOCK_USER_TOKEN;
            success = true;
          }

          if (success) {
            this.token = accessToken;
            this.user = { username: loggedInUsername, role: role };

            localStorage.setItem('user', JSON.stringify(this.user));
            localStorage.setItem('token', this.token);

            apiClient.defaults.headers.common['Authorization'] = `Bearer ${this.token}`;

            this.loading = false;
            resolve(true);
          } else {
            this.loginError = 'Tên đăng nhập hoặc mật khẩu không đúng (mock).';
            this.loading = false;
            resolve(false);
          }
        }, 500 + Math.random() * 500);
      });
    },

    logout() {
      const cartStore = useCartStore();
      cartStore.handleLogoutCleanup();

      this.user = null;
      this.token = null;
      this.returnUrl = null;
      this.loginError = null;

      localStorage.removeItem('user');
      localStorage.removeItem('token');

      delete apiClient.defaults.headers.common['Authorization'];
      clearCartSessionId();
    },

    setReturnUrl(url) {
      this.returnUrl = url;
    }
  }
});
