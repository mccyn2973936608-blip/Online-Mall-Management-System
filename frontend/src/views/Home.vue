<template>
  <div class="creative-home">
    <div class="bg-gradient"></div>
    <div class="particles">
      <div class="particle" v-for="i in 50" :key="i" :style="getParticleStyle(i)"></div>
    </div>
    
    <Navbar />
    
    <div class="main-content">
      <div class="hero-section">
        <h1 class="hero-title">
          <span class="neon-text">探索</span>
          <span class="neon-text neon-blue">未来</span>
          <span class="neon-text">购物</span>
        </h1>
        <p class="hero-subtitle">
          发现令人惊叹的产品，开启科技购物新体验
        </p>
        
        <div class="search-container">
          <div class="search-box">
            <el-input
              v-model="query.keyword"
              placeholder="搜索你想要的一切..."
              class="creative-search"
              @keyup.enter="loadProducts"
              prefix-icon="Search"
            />
            <el-button class="search-btn" type="primary" @click="loadProducts">
              <span class="btn-text">探索</span>
            </el-button>
          </div>
        </div>
      </div>
      
      <div class="filters-section">
        <div class="filter-card">
          <div class="filter-header">
            <div class="filter-icon">🎯</div>
            <span class="filter-title">分类筛选</span>
          </div>
          <div class="filter-options">
            <div
              :class="['category-pill', { active: query.categoryId === null }]"
              @click="query.categoryId = null; loadProducts()"
            >
              全部
            </div>
            <div
              v-for="item in categories"
              :key="item.id"
              :class="['category-pill', { active: query.categoryId === item.id }]"
              @click="query.categoryId = item.id; loadProducts()"
            >
              {{ item.name }}
            </div>
          </div>
        </div>
        
        <div class="filter-card">
          <div class="filter-header">
            <div class="filter-icon">📊</div>
            <span class="filter-title">排序方式</span>
          </div>
          <div class="sort-options">
            <div
              :class="['sort-option', { active: query.sort === '' }]"
              @click="query.sort = ''; loadProducts()"
            >
              <div class="sort-icon">🔥</div>
              <span>热门</span>
            </div>
            <div
              :class="['sort-option', { active: query.sort === 'price_asc' }]"
              @click="query.sort = 'price_asc'; loadProducts()"
            >
              <div class="sort-icon">⬆️</div>
              <span>价格升</span>
            </div>
            <div
              :class="['sort-option', { active: query.sort === 'price_desc' }]"
              @click="query.sort = 'price_desc'; loadProducts()"
            >
              <div class="sort-icon">⬇️</div>
              <span>价格降</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="products-grid">
        <div
          v-for="(product, index) in products"
          :key="product.id"
          class="product-card"
          :style="{ animationDelay: index * 0.1 + 's' }"
          @click="viewDetail(product.id)"
        >
          <div class="card-glow"></div>
          <div class="card-image">
            <img :src="product.imageUrl || 'https://via.placeholder.com/400'" :alt="product.name" />
            <div class="image-overlay">
              <div class="view-details">查看详情</div>
            </div>
            <div class="stock-badge">
              <span v-if="product.stock > 50" class="stock-high">库存充足</span>
              <span v-else-if="product.stock > 10" class="stock-medium">库存有限</span>
              <span v-else class="stock-low">即将售罄</span>
            </div>
          </div>
          <div class="card-content">
            <div class="product-category">{{ product.categoryName }}</div>
            <h3 class="product-name">{{ product.name }}</h3>
            <p class="product-desc">{{ product.description }}</p>
            <div class="card-footer">
              <div class="price-wrapper">
                <span class="price-currency">¥</span>
                <span class="price-value">{{ product.price }}</span>
              </div>
              <div class="stock-info">
                <span class="stock-count">{{ product.stock }}</span>件在库
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="empty-state" v-if="products.length === 0">
        <div class="empty-icon">🔍</div>
        <h3>暂无商品</h3>
        <p>尝试其他关键词搜索吧</p>
      </div>
      
      <div class="pagination-section" v-if="total > size">
        <el-pagination
          v-model:current-page="page"
          :page-size="size"
          :total="total"
          layout="prev, pager, next"
          @current-change="onPageChange"
          class="creative-pagination"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import Navbar from '@/components/Navbar.vue';
import { fetchProducts, fetchCategories } from '@/api/product';

const router = useRouter();
const categories = ref<any[]>([]);
const products = ref<any[]>([]);
const total = ref(0);
const page = ref(1);
const size = ref(9);
const query = reactive({ categoryId: null as number | null, keyword: '', sort: '' });

const getParticleStyle = (i: number) => {
  return {
    left: Math.random() * 100 + '%',
    top: Math.random() * 100 + '%',
    animationDelay: Math.random() * 15 + 's',
    animationDuration: 15 + Math.random() * 10 + 's'
  };
};

const loadProducts = async () => {
  try {
    const res: any = await fetchProducts({ page: page.value, size: size.value, categoryId: query.categoryId, keyword: query.keyword, sort: query.sort });
    if (res.data && res.data.records) {
      products.value = res.data.records;
      total.value = res.data.total;
    } else {
      products.value = [];
      total.value = 0;
    }
  } catch (error) {
    console.error('加载商品失败:', error);
    products.value = [];
    total.value = 0;
  }
};

const loadCategories = async () => {
  try {
    const res: any = await fetchCategories();
    categories.value = res.data || [];
  } catch (error) {
    console.error('加载分类失败:', error);
    categories.value = [];
  }
};

const onPageChange = (current: number) => {
  page.value = current;
  loadProducts();
};

const viewDetail = (id: number) => {
  router.push({ name: 'ProductDetail', params: { id } });
};

onMounted(async () => {
  await loadCategories();
  await loadProducts();
});
</script>

<style scoped>
.creative-home {
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
    radial-gradient(circle at 80% 80%, rgba(118, 75, 162, 0.15) 0%, transparent 50%),
    radial-gradient(circle at 40% 20%, rgba(0, 212, 255, 0.1) 0%, transparent 40%);
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

.hero-section {
  text-align: center;
  padding: 80px 0 60px;
}

.hero-title {
  font-size: 4rem;
  font-weight: 900;
  margin-bottom: 16px;
  letter-spacing: 4px;
}

.neon-text {
  display: inline-block;
  position: relative;
  color: #fff;
  text-shadow: 
    0 0 10px rgba(102, 126, 234, 0.8),
    0 0 20px rgba(102, 126, 234, 0.6),
    0 0 40px rgba(102, 126, 234, 0.4);
  animation: neonPulse 2s ease-in-out infinite alternate;
}

.neon-blue {
  color: #00d4ff;
  text-shadow: 
    0 0 10px rgba(0, 212, 255, 0.8),
    0 0 20px rgba(0, 212, 255, 0.6),
    0 0 40px rgba(0, 212, 255, 0.4);
}

@keyframes neonPulse {
  from {
    text-shadow: 
      0 0 10px rgba(102, 126, 234, 0.8),
      0 0 20px rgba(102, 126, 234, 0.6),
      0 0 40px rgba(102, 126, 234, 0.4);
  }
  to {
    text-shadow: 
      0 0 20px rgba(102, 126, 234, 1),
      0 0 40px rgba(102, 126, 234, 0.8),
      0 0 80px rgba(102, 126, 234, 0.6);
  }
}

.hero-subtitle {
  font-size: 1.3rem;
  color: rgba(255, 255, 255, 0.7);
  margin-bottom: 40px;
  letter-spacing: 2px;
}

.search-container {
  display: flex;
  justify-content: center;
  max-width: 600px;
  margin: 0 auto;
}

.search-box {
  display: flex;
  gap: 16px;
  width: 100%;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(102, 126, 234, 0.3);
  border-radius: 50px;
  padding: 8px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
}

.search-box:hover {
  border-color: rgba(102, 126, 234, 0.6);
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.2);
}

.creative-search {
  flex: 1;
}

.creative-search :deep(.el-input__wrapper) {
  background: transparent !important;
  border: none !important;
  box-shadow: none !important;
  padding: 8px 16px;
}

.creative-search :deep(.el-input__inner) {
  color: #fff !important;
  font-size: 1.1rem;
}

.creative-search :deep(.el-input__inner::placeholder) {
  color: rgba(255, 255, 255, 0.5);
}

.search-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 50px;
  padding: 12px 32px;
  font-weight: 600;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
}

.btn-text {
  position: relative;
  z-index: 1;
}

.filters-section {
  display: flex;
  gap: 24px;
  margin-bottom: 48px;
  flex-wrap: wrap;
}

.filter-card {
  flex: 1;
  min-width: 300px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
}

.filter-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.filter-icon {
  font-size: 1.5rem;
}

.filter-title {
  color: #fff;
  font-size: 1.1rem;
  font-weight: 600;
}

.filter-options {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.category-pill {
  padding: 10px 20px;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(102, 126, 234, 0.3);
  border-radius: 50px;
  color: rgba(255, 255, 255, 0.8);
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.category-pill:hover {
  background: rgba(102, 126, 234, 0.2);
  border-color: rgba(102, 126, 234, 0.5);
  transform: translateY(-2px);
}

.category-pill.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-color: transparent;
  color: #fff;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

.sort-options {
  display: flex;
  gap: 16px;
}

.sort-option {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px 12px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: 16px;
  color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  transition: all 0.3s ease;
}

.sort-option:hover {
  background: rgba(102, 126, 234, 0.15);
  border-color: rgba(102, 126, 234, 0.4);
  transform: translateY(-3px);
}

.sort-option.active {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.3) 0%, rgba(118, 75, 162, 0.3) 100%);
  border-color: #667eea;
  color: #fff;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.sort-icon {
  font-size: 1.5rem;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 32px;
  margin-bottom: 48px;
}

.product-card {
  position: relative;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: 24px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.4s ease;
  animation: cardAppear 0.6s ease forwards;
  opacity: 0;
  transform: translateY(30px);
}

@keyframes cardAppear {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.product-card:hover {
  transform: translateY(-10px);
  border-color: rgba(102, 126, 234, 0.6);
}

.card-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(102, 126, 234, 0.15) 0%, transparent 70%);
  opacity: 0;
  transition: opacity 0.4s ease;
  pointer-events: none;
}

.product-card:hover .card-glow {
  opacity: 1;
}

.card-image {
  position: relative;
  height: 240px;
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}

.product-card:hover .card-image img {
  transform: scale(1.1);
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to bottom, transparent 0%, rgba(0, 0, 0, 0.7) 100%);
  opacity: 0;
  transition: opacity 0.4s ease;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  padding-bottom: 32px;
}

.product-card:hover .image-overlay {
  opacity: 1;
}

.view-details {
  padding: 12px 32px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50px;
  color: #fff;
  font-weight: 600;
  transform: translateY(20px);
  transition: transform 0.4s ease;
}

.product-card:hover .view-details {
  transform: translateY(0);
}

.stock-badge {
  position: absolute;
  top: 16px;
  right: 16px;
}

.stock-high, .stock-medium, .stock-low {
  padding: 6px 14px;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
  backdrop-filter: blur(10px);
}

.stock-high {
  background: rgba(0, 255, 127, 0.2);
  color: #00ff7f;
  border: 1px solid rgba(0, 255, 127, 0.4);
}

.stock-medium {
  background: rgba(255, 200, 0, 0.2);
  color: #ffc800;
  border: 1px solid rgba(255, 200, 0, 0.4);
}

.stock-low {
  background: rgba(255, 71, 87, 0.2);
  color: #ff4757;
  border: 1px solid rgba(255, 71, 87, 0.4);
}

.card-content {
  padding: 24px;
}

.product-category {
  display: inline-block;
  padding: 6px 14px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.2) 0%, rgba(118, 75, 162, 0.2) 100%);
  border-radius: 50px;
  color: #667eea;
  font-size: 0.8rem;
  font-weight: 600;
  margin-bottom: 12px;
}

.product-name {
  color: #fff;
  font-size: 1.3rem;
  font-weight: 700;
  margin-bottom: 8px;
}

.product-desc {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.95rem;
  line-height: 1.5;
  margin-bottom: 20px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.price-wrapper {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.price-currency {
  color: #667eea;
  font-size: 1.1rem;
  font-weight: 700;
}

.price-value {
  color: #fff;
  font-size: 2rem;
  font-weight: 900;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.stock-info {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.9rem;
}

.stock-count {
  color: #00d4ff;
  font-weight: 700;
}

.empty-state {
  text-align: center;
  padding: 80px 0;
}

.empty-icon {
  font-size: 5rem;
  margin-bottom: 24px;
}

.empty-state h3 {
  color: #fff;
  font-size: 1.5rem;
  margin-bottom: 8px;
}

.empty-state p {
  color: rgba(255, 255, 255, 0.6);
}

.pagination-section {
  display: flex;
  justify-content: center;
}

.creative-pagination :deep(.el-pager li),
.creative-pagination :deep(.btn-prev),
.creative-pagination :deep(.btn-next) {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(102, 126, 234, 0.3);
  color: rgba(255, 255, 255, 0.8);
  border-radius: 12px;
  margin: 0 4px;
}

.creative-pagination :deep(.el-pager li:hover),
.creative-pagination :deep(.btn-prev:hover),
.creative-pagination :deep(.btn-next:hover) {
  background: rgba(102, 126, 234, 0.2);
  border-color: rgba(102, 126, 234, 0.5);
  color: #fff;
}

.creative-pagination :deep(.el-pager li.is-active) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-color: transparent;
  color: #fff;
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 2.5rem;
  }
  
  .filters-section {
    flex-direction: column;
  }
  
  .products-grid {
    grid-template-columns: 1fr;
  }
}
</style>