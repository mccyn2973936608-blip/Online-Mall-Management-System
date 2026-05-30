import { defineStore } from 'pinia';
import { getUserInfo } from '@/api/auth';

interface UserState {
  token: string;
  user: any;
  role: number;
  cartCount: number;
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: localStorage.getItem('mall-token') || '',
    user: JSON.parse(localStorage.getItem('mall-user') || 'null') || {},
    role: Number(localStorage.getItem('mall-role') || 0),
    cartCount: 0,
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
  },
  actions: {
    setToken(token: string) {
      this.token = token;
      localStorage.setItem('mall-token', token);
    },
    setUser(token: string, user: any) {
      this.token = token;
      this.user = user;
      this.role = user.role;
      localStorage.setItem('mall-token', token);
      localStorage.setItem('mall-user', JSON.stringify(user));
      localStorage.setItem('mall-role', String(user.role));
    },
    async fetchUserInfo() {
      try {
        const res: any = await getUserInfo();
        if (res.data) {
          this.user = res.data;
          this.role = res.data.role;
          localStorage.setItem('mall-user', JSON.stringify(res.data));
          localStorage.setItem('mall-role', String(res.data.role));
        }
      } catch (error) {
        console.error('获取用户信息失败', error);
      }
    },
    async fetchCartCount() {
      try {
        const res: any = await fetch('/api/cart/count', {
          headers: { Authorization: `Bearer ${this.token}` }
        }).then(r => r.json());
        if (res.data !== undefined) {
          this.cartCount = res.data;
        }
      } catch (error) {
        console.error('获取购物车数量失败', error);
      }
    },
    logout() {
      this.token = '';
      this.user = {};
      this.role = 0;
      this.cartCount = 0;
      localStorage.removeItem('mall-token');
      localStorage.removeItem('mall-user');
      localStorage.removeItem('mall-role');
    },
  },
});
