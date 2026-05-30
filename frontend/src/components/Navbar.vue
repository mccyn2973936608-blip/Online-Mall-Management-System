<template>
  <div>
    <div class="mall-navbar">
      <div class="navbar-container">
        <router-link to="/" class="navbar-brand">
          <span class="brand-text">商城</span>
        </router-link>
        
        <div class="navbar-search">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索商品"
            class="search-input"
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch" class="search-btn">搜索</el-button>
            </template>
          </el-input>
        </div>
        
        <div class="navbar-menu">
          <router-link to="/" class="menu-item">首页</router-link>
          <router-link to="/cart" class="menu-item">
            <el-badge :value="cartCount" :hidden="cartCount === 0" class="cart-badge">
              <span>购物车</span>
            </el-badge>
          </router-link>
          <router-link v-if="!isLoggedIn" to="/login" class="menu-item">登录</router-link>
          <router-link v-if="!isLoggedIn" to="/register" class="menu-item">注册</router-link>
          <el-dropdown v-if="isLoggedIn" @command="handleCommand">
            <span class="menu-item user-dropdown">
              <span class="user-avatar">{{ userInitial }}</span>
              {{ username }}
              <i class="el-icon-arrow-down"></i>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="orders">我的订单</el-dropdown-item>
                <el-dropdown-item v-if="isAdmin" command="admin">
                  <span class="admin-item">🔧 管理后台</span>
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/store/user';

const router = useRouter();
const userStore = useUserStore();
const searchKeyword = ref('');

const isLoggedIn = computed(() => userStore.isLoggedIn);
const username = computed(() => userStore.user?.username || '用户');
const userInitial = computed(() => (userStore.user?.username || 'U')[0].toUpperCase());
const isAdmin = computed(() => userStore.role === 1);
const cartCount = computed(() => userStore.cartCount || 0);

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/', query: { keyword: searchKeyword.value } });
  }
};

const handleCommand = (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/profile');
      break;
    case 'orders':
      router.push('/orders');
      break;
    case 'admin':
      router.push('/admin');
      break;
    case 'logout':
      userStore.logout();
      router.push('/login');
      break;
  }
};

onMounted(() => {
  if (isLoggedIn.value) {
    userStore.fetchUserInfo();
    userStore.fetchCartCount();
  }
});
</script>

<style scoped>
.mall-navbar {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  padding: 16px 20px;
  gap: 40px;
}

.navbar-brand {
  text-decoration: none;
  flex-shrink: 0;
}

.brand-text {
  font-size: 28px;
  font-weight: 700;
  color: #ff5000;
  letter-spacing: 2px;
}

.navbar-search {
  flex: 1;
  max-width: 600px;
}

.search-input {
  width: 100%;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 20px 0 0 20px;
}

.search-input :deep(.el-input-group__append) {
  border-radius: 0 20px 20px 0;
  background: #ff5000;
  border-color: #ff5000;
  color: white;
}

.search-btn {
  background: #ff5000 !important;
  border-color: #ff5000 !important;
  color: white !important;
}

.navbar-menu {
  display: flex;
  align-items: center;
  gap: 24px;
}

.menu-item {
  text-decoration: none;
  color: #333;
  font-size: 15px;
  transition: color 0.3s;
  display: flex;
  align-items: center;
  gap: 4px;
}

.menu-item:hover {
  color: #ff5000;
}

.user-dropdown {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #ff5000, #ff6a33);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 14px;
}

.admin-item {
  color: #ff5000;
  font-weight: 600;
}

.cart-badge :deep(.el-badge__content) {
  background-color: #ff5000;
}
</style>
