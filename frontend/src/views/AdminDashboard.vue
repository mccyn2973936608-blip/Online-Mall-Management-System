<template>
  <div class="admin-dashboard">
    <div class="bg-gradient"></div>
    <div class="particles">
      <div class="particle" v-for="i in 30" :key="i" :style="getParticleStyle(i)"></div>
    </div>
    
    <Navbar />
    
    <div class="main-content">
      <div class="page-header">
        <h1 class="page-title">⚡ 管理控制中心</h1>
        <p class="page-subtitle">欢迎回来，指挥官！掌握全局，运筹帷幄</p>
      </div>
      
      <div class="stats-grid">
        <div class="stat-card stat-blue">
          <div class="stat-icon">👥</div>
          <div class="stat-content">
            <div class="stat-value">{{ userCount }}</div>
            <div class="stat-label">会员总数</div>
          </div>
        </div>
        <div class="stat-card stat-purple">
          <div class="stat-icon">📦</div>
          <div class="stat-content">
            <div class="stat-value">{{ productCount }}</div>
            <div class="stat-label">商品总数</div>
          </div>
        </div>
        <div class="stat-card stat-cyan">
          <div class="stat-icon">🛒</div>
          <div class="stat-content">
            <div class="stat-value">{{ orderCount }}</div>
            <div class="stat-label">订单总数</div>
          </div>
        </div>
        <div class="stat-card stat-green">
          <div class="stat-icon">📂</div>
          <div class="stat-content">
            <div class="stat-value">{{ categoryCount }}</div>
            <div class="stat-label">分类总数</div>
          </div>
        </div>
      </div>
      
      <h2 class="section-title">快速导航</h2>
      <div class="quick-actions">
        <div class="action-card" @click="go('AdminUsers')">
          <div class="action-icon">👤</div>
          <div class="action-title">会员管理</div>
          <div class="action-desc">管理系统会员，编辑用户信息</div>
        </div>
        <div class="action-card" @click="go('AdminProducts')">
          <div class="action-icon">🎁</div>
          <div class="action-title">商品管理</div>
          <div class="action-desc">管理商品信息，上架下架商品</div>
        </div>
        <div class="action-card" @click="go('AdminCategories')">
          <div class="action-icon">🏷️</div>
          <div class="action-title">分类管理</div>
          <div class="action-desc">管理商品分类，调整分类顺序</div>
        </div>
        <div class="action-card" @click="go('AdminOrders')">
          <div class="action-icon">📋</div>
          <div class="action-title">订单管理</div>
          <div class="action-desc">处理订单，确认完成取消</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import Navbar from '@/components/Navbar.vue';
import { fetchAdminUsers } from '@/api/admin';
import { fetchProducts, fetchCategories } from '@/api/product';
import { fetchAdminOrders } from '@/api/admin';

const router = useRouter();
const userCount = ref(0);
const productCount = ref(0);
const orderCount = ref(0);
const categoryCount = ref(0);

const getParticleStyle = (i: number) => {
  return {
    left: Math.random() * 100 + '%',
    top: Math.random() * 100 + '%',
    animationDelay: Math.random() * 15 + 's',
    animationDuration: 15 + Math.random() * 10 + 's'
  };
};

const loadStats = async () => {
  try {
    const [userRes, productRes, categoryRes, orderRes] = await Promise.all([
      fetchAdminUsers({ page: 1, size: 1 }),
      fetchProducts({ page: 1, size: 1 }),
      fetchCategories(),
      fetchAdminOrders({ page: 1, size: 1 })
    ]);
    userCount.value = (userRes.data as any)?.total || 0;
    productCount.value = (productRes.data as any)?.total || 0;
    categoryCount.value = (categoryRes.data as any)?.length || 0;
    orderCount.value = (orderRes.data as any)?.total || 0;
  } catch (e) {
    console.error('Load stats failed', e);
  }
};

const go = (name: string) => router.push({ name });

onMounted(loadStats);
</script>

<style scoped>
.admin-dashboard {
  min-height: 100vh;
  position: relative;
  overflow-x: hidden;
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a3e 50%, #0d1b2a 100%);
}

.bg-gradient {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: 
    radial-gradient(circle at 20% 50%, rgba(102, 126, 234, 0.15) 0%, transparent 50%),
    radial-gradient(circle at 80% 80%, rgba(118, 75, 162, 0.15) 0%, transparent 50%);
  pointer-events: none;
  z-index: 1;
}

.particles {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  pointer-events: none;
  z-index: 2;
}

.particle {
  position: absolute;
  width: 4px;
  height: 4px;
  background: rgba(102, 126, 234, 0.6);
  border-radius: 50%;
  box-shadow: 0 0 10px rgba(102, 126, 234, 0.8), 0 0 20px rgba(102, 126, 234, 0.4);
  animation: float 15s infinite ease-in-out;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100vh) rotate(720deg);
    opacity: 0;
  }
}

.main-content {
  position: relative;
  z-index: 10;
  padding: 40px 5%;
  max-width: 1600px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 48px;
}

.page-title {
  font-size: 3rem;
  font-weight: 900;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #00d4ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 12px;
}

.page-subtitle {
  color: rgba(255, 255, 255, 0.7);
  font-size: 1.1rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-bottom: 48px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
}

.stat-blue {
  border-color: rgba(102, 126, 234, 0.4);
  box-shadow: 0 0 20px rgba(102, 126, 234, 0.2);
}

.stat-purple {
  border-color: rgba(118, 75, 162, 0.4);
  box-shadow: 0 0 20px rgba(118, 75, 162, 0.2);
}

.stat-cyan {
  border-color: rgba(0, 212, 255, 0.4);
  box-shadow: 0 0 20px rgba(0, 212, 255, 0.2);
}

.stat-green {
  border-color: rgba(0, 255, 127, 0.4);
  box-shadow: 0 0 20px rgba(0, 255, 127, 0.2);
}

.stat-icon {
  font-size: 3rem;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 2.5rem;
  font-weight: 900;
  color: white;
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.95rem;
}

.section-title {
  color: white;
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.action-card {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: 20px;
  padding: 32px 24px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.action-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.action-card:hover {
  transform: translateY(-8px);
  border-color: rgba(102, 126, 234, 0.5);
  box-shadow: 0 15px 40px rgba(102, 126, 234, 0.2);
}

.action-card:hover::before {
  opacity: 1;
}

.action-icon {
  font-size: 3rem;
  margin-bottom: 16px;
  position: relative;
  z-index: 1;
}

.action-title {
  color: white;
  font-size: 1.25rem;
  font-weight: 700;
  margin-bottom: 8px;
  position: relative;
  z-index: 1;
}

.action-desc {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.9rem;
  line-height: 1.5;
  position: relative;
  z-index: 1;
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .quick-actions {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .page-title {
    font-size: 2rem;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .quick-actions {
    grid-template-columns: 1fr;
  }
}
</style>
