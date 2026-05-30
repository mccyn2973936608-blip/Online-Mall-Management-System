<template>
  <div class="home-page">
    <Navbar />
    
    <div class="main-content">
      <!-- 分类导航 -->
      <div class="category-nav">
        <div 
          v-for="item in categories" 
          :key="item.id"
          :class="['category-item', { active: query.categoryId === item.id }]"
          @click="selectCategory(item.id)"
        >
          {{ item.name }}
        </div>
      </div>
      
      <!-- 商品列表 -->
      <div class="product-list" v-if="products.length">
        <div 
          v-for="product in products" 
          :key="product.id"
          class="product-card"
          @click="viewDetail(product.id)"
        >
          <div class="product-image">
            <img :src="product.imageUrl" :alt="product.name" />
          </div>
          <div class="product-info">
            <div class="product-name">{{ product.name }}</div>
            <div class="product-desc">{{ product.description }}</div>
            <div class="product-bottom">
              <div class="product-price">
                <span class="price-symbol">¥</span>
                <span class="price-value">{{ product.price }}</span>
              </div>
              <div class="product-sales">销量: {{ product.stock }}</div>
            </div>
            <div class="product-actions">
              <el-button type="primary" size="small" @click.stop="addToCart(product)">加入购物车</el-button>
              <el-button size="small" @click.stop="viewDetail(product.id)">查看详情</el-button>
            </div>
          </div>
        </div>
      </div>
      
      <div v-else class="empty-state">
        <div class="empty-icon">📦</div>
        <div>暂无商品</div>
      </div>
      
      <!-- 分页 -->
      <div class="pagination-wrapper" v-if="total > size">
        <el-pagination
          v-model:current-page="page"
          :page-size="size"
          :total="total"
          layout="prev, pager, next"
          @current-change="onPageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import Navbar from '@/components/Navbar.vue';
import { fetchProducts, fetchCategories } from '@/api/product';
import { addCart } from '@/api/cart';

const router = useRouter();
const route = useRoute();
const categories = ref<any[]>([]);
const products = ref<any[]>([]);
const total = ref(0);
const page = ref(1);
const size = ref(20);
const query = reactive({ categoryId: null as number | null });

const selectCategory = (id: number | null) => {
  query.categoryId = query.categoryId === id ? null : id;
  page.value = 1;
  loadProducts();
};

const loadProducts = async () => {
  try {
    const res: any = await fetchProducts({ 
      page: page.value, 
      size: size.value, 
      categoryId: query.categoryId,
      keyword: route.query.keyword as string || ''
    });
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

const addToCart = async (product: any) => {
  try {
    await addCart({ productId: product.id, quantity: 1 });
    ElMessage.success('已加入购物车');
  } catch (error) {
    ElMessage.error('请先登录');
    router.push('/login');
  }
};

onMounted(async () => {
  await loadCategories();
  await loadProducts();
  
  // 处理搜索关键词
  if (route.query.keyword) {
    loadProducts();
  }
});
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.category-nav {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 20px;
  display: flex;
  gap: 16px;
  overflow-x: auto;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.category-item {
  padding: 8px 20px;
  border-radius: 20px;
  cursor: pointer;
  white-space: nowrap;
  transition: all 0.3s;
  color: #666;
  font-size: 14px;
}

.category-item:hover {
  color: #ff5000;
}

.category-item.active {
  background: #ff5000;
  color: #fff;
}

.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px;
}

.product-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.product-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
  background: #fafafa;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

.product-info {
  padding: 16px;
}

.product-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-desc {
  font-size: 13px;
  color: #999;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.product-price {
  color: #ff5000;
  font-weight: 700;
  font-size: 20px;
}

.price-symbol {
  font-size: 14px;
}

.product-sales {
  color: #999;
  font-size: 13px;
}

.product-actions {
  display: flex;
  gap: 8px;
}

.product-actions .el-button {
  flex: 1;
}

.empty-state {
  text-align: center;
  padding: 80px 20px;
  color: #999;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  padding: 20px;
}
</style>
