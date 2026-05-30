import { defineStore } from 'pinia';

interface UserState {
  token: string;
  user: any;
  role: number;
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: localStorage.getItem('mall-token') || '',
    user: JSON.parse(localStorage.getItem('mall-user') || 'null') || {},
    role: Number(localStorage.getItem('mall-role') || 0),
  }),
  actions: {
    setUser(token: string, user: any) {
      this.token = token;
      this.user = user;
      this.role = user.role;
      localStorage.setItem('mall-token', token);
      localStorage.setItem('mall-user', JSON.stringify(user));
      localStorage.setItem('mall-role', String(user.role));
    },
    logout() {
      this.token = '';
      this.user = {};
      this.role = 0;
      localStorage.removeItem('mall-token');
      localStorage.removeItem('mall-user');
      localStorage.removeItem('mall-role');
    },
  },
});
