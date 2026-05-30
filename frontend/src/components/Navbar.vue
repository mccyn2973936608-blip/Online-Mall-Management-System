<template>
  <header class="navbar">
    <div class="nav-content">
      <div class="brand" @click="goHome">
        <span class="brand-icon">🚀</span>
        <span class="brand-text">未来商城</span>
        <div class="brand-glow"></div>
      </div>
      
      <nav class="menu">
        <button class="nav-item" @click="goHome">
          <span class="nav-icon">🏠</span>
          <span>首页</span>
        </button>
        <button class="nav-item" @click="goCart">
          <span class="nav-icon">🛒</span>
          <span>购物车</span>
        </button>
        <button class="nav-item" @click="goOrders">
          <span class="nav-icon">📦</span>
          <span>我的订单</span>
        </button>
        <button v-if="isAdmin" class="nav-item nav-admin" @click="goAdmin">
          <span class="nav-icon">⚡</span>
          <span>管理员</span>
        </button>
      </nav>
      
      <div class="user-section">
        <template v-if="!token">
          <button class="btn-outline" @click="goLogin">登录</button>
          <button class="btn-primary" @click="goRegister">注册</button>
        </template>
        <template v-else>
          <div class="user-dropdown">
            <div class="user-btn" @click="showDropdown = !showDropdown">
              <div class="user-avatar">
                {{ (user.nickname || user.username)?.charAt(0)?.toUpperCase() || 'U' }}
              </div>
              <span class="user-name">{{ user.nickname || user.username }}</span>
              <span class="dropdown-icon" :class="{ open: showDropdown }">▼</span>
            </div>
            <div class="dropdown-menu" :class="{ open: showDropdown }">
              <div class="dropdown-item" @click="goProfile">
                <span class="item-icon">👤</span>
                <span>个人中心</span>
              </div>
              <div class="dropdown-divider"></div>
              <div class="dropdown-item logout-item" @click="logout">
                <span class="item-icon">🚪</span>
                <span>退出登录</span>
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useUserStore } from '@/store/user';
import { computed, ref, onMounted, onUnmounted } from 'vue';

const router = useRouter();
const userStore = useUserStore();
const token = computed(() => userStore.token);
const user = computed(() => userStore.user);
const isAdmin = computed(() => userStore.role === 1);
const showDropdown = ref(false);

const goHome = () => router.push({ name: 'Home' });
const goLogin = () => router.push({ name: 'Login' });
const goRegister = () => router.push({ name: 'Register' });
const goProfile = () => router.push({ name: 'Profile' });
const goCart = () => router.push({ name: 'Cart' });
const goOrders = () => router.push({ name: 'MyOrders' });
const goAdmin = () => router.push({ name: 'AdminDashboard' });
const logout = () => {
  userStore.logout();
  showDropdown.value = false;
  router.push({ name: 'Login' });
};

const handleClickOutside = (e: Event) => {
  const target = e.target as HTMLElement;
  if (!target.closest('.user-dropdown')) {
    showDropdown.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>

<style scoped>
.navbar {
  position: relative;
  z-index: 100;
  background: rgba(10, 10, 26, 0.8);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(102, 126, 234, 0.2);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
}

.nav-content {
  max-width: 1600px;
  margin: 0 auto;
  padding: 0 5%;
  height: 70px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  position: relative;
  padding: 8px 16px;
  border-radius: 16px;
  transition: all 0.3s ease;
}

.brand:hover {
  background: rgba(102, 126, 234, 0.1);
}

.brand-icon {
  font-size: 1.8rem;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-5px); }
}

.brand-text {
  font-size: 1.4rem;
  font-weight: 900;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #00d4ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: 2px;
}

.brand-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 150%;
  height: 150%;
  background: radial-gradient(circle, rgba(102, 126, 234, 0.2) 0%, transparent 70%);
  transform: translate(-50%, -50%);
  opacity: 0;
  transition: opacity 0.3s ease;
  pointer-events: none;
}

.brand:hover .brand-glow {
  opacity: 1;
}

.menu {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: transparent;
  border: 1px solid transparent;
  border-radius: 12px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.nav-item:hover {
  background: rgba(102, 126, 234, 0.15);
  border-color: rgba(102, 126, 234, 0.3);
  color: #fff;
  transform: translateY(-2px);
}

.nav-icon {
  font-size: 1.1rem;
}

.nav-admin {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.2) 0%, rgba(118, 75, 162, 0.2) 100%);
  border-color: rgba(102, 126, 234, 0.4);
  color: #667eea;
}

.nav-admin:hover {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.3) 0%, rgba(118, 75, 162, 0.3) 100%);
  color: #fff;
}

.user-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.btn-outline {
  padding: 10px 24px;
  background: transparent;
  border: 1px solid rgba(102, 126, 234, 0.5);
  border-radius: 12px;
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-outline:hover {
  background: rgba(102, 126, 234, 0.15);
  border-color: #667eea;
  color: #fff;
  transform: translateY(-2px);
}

.btn-primary {
  padding: 10px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 12px;
  color: #fff;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

.user-dropdown {
  position: relative;
}

.user-btn {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.user-btn:hover {
  background: rgba(102, 126, 234, 0.15);
  border-color: rgba(102, 126, 234, 0.4);
}

.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: 700;
  font-size: 0.95rem;
}

.user-name {
  color: #fff;
  font-weight: 600;
  font-size: 0.9rem;
}

.dropdown-icon {
  color: rgba(255, 255, 255, 0.5);
  font-size: 0.7rem;
  transition: transform 0.3s ease;
}

.dropdown-icon.open {
  transform: rotate(180deg);
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 12px);
  right: 0;
  min-width: 200px;
  background: rgba(10, 10, 26, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  padding: 8px;
  opacity: 0;
  visibility: hidden;
  transform: translateY(-10px);
  transition: all 0.3s ease;
}

.dropdown-menu.open {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 12px;
  color: rgba(255, 255, 255, 0.8);
  cursor: pointer;
  transition: all 0.3s ease;
}

.dropdown-item:hover {
  background: rgba(102, 126, 234, 0.15);
  color: #fff;
}

.item-icon {
  font-size: 1.1rem;
}

.dropdown-divider {
  height: 1px;
  background: rgba(102, 126, 234, 0.2);
  margin: 8px 0;
}

.logout-item {
  color: rgba(255, 71, 87, 0.8);
}

.logout-item:hover {
  background: rgba(255, 71, 87, 0.15);
  color: #ff4757;
}

@media (max-width: 768px) {
  .nav-content {
    padding: 0 16px;
  }
  
  .menu {
    display: none;
  }
  
  .brand-text {
    font-size: 1.2rem;
  }
}
</style>