<template>
  <div class="detail-page">
    <Navbar />
    <div class="main-content">
      <div class="product-detail" v-if="product.id">
        <div class="product-images">
          <img :src="product.imageUrl" :alt="product.name" class="main-image" />
        </div>
        
        <div class="product-info">
          <h1 class="product-name">{{ product.name }}</h1>
          <div class="product-desc">{{ product.description }}</div>
          
          <div class="product-meta">
            <div class="meta-item">
              <span class="label">商品分类:</span>
              <span class="value">{{ product.categoryName }}</span>
            </div>
            <div class="meta-item">
              <span class="label">商品库存:</span>
              <span class="value">{{ product.stock }} 件</span>
            </div>
          </div>
          
          <div class="product-price-section">
            <div class="price-label">价格</div>
            <div class="price">
              <span class="symbol">¥</span>
              <span class="value">{{ product.price }}</span>
            </div>
          </div>
          
          <div class="product-actions">
            <div class="quantity-section">
              <span class="label">购买数量:</span>
              <div class="quantity-control">
                <el-button 
                  class="qty-btn"
                  size="small" 
                  :disabled="quantity <= 1" 
                  @click="quantity--"
                >−</el-button>
                <span class="qty-value">{{ quantity }}</span>
                <el-button 
                  class="qty-btn"
                  size="small" 
                  @click="quantity++"
                >+</el-button>
              </div>
            </div>
            
            <div class="action-buttons">
              <el-button type="primary" size="large" @click="addToCart">加入购物车</el-button>
              <el-button size="large" @click="buyNow">立即购买</el-button>
            </div>
          </div>
        </div>
      </div>
      
      <div v-else class="loading">
        <el-skeleton :rows="6" animated />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import Navbar from '@/components/Navbar.vue';
import { fetchProductDetail } from '@/api/product';
import { addCart } from '@/api/cart';

const route = useRoute();
const router = useRouter();
const product = ref<any>({});
const quantity = ref(1);

const loadProduct = async () => {
  const res: any = await fetchProductDetail(Number(route.params.id));
  product.value = res.data;
};

const addToCart = async () => {
  try {
    await addCart({ productId: product.value.id, quantity: quantity.value });
    ElMessage.success('已加入购物车');
  } catch (err: any) {
    ElMessage.error('请先登录');
    router.push('/login');
  }
};

const buyNow = () => {
  addToCart().then(() => {
    router.push('/cart');
  });
};

onMounted(loadProduct);
</script>

<style scoped>
.detail-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.product-detail {
  display: grid;
  grid-template-columns: 400px 1fr;
  gap: 40px;
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.product-images {
  width: 100%;
}

.main-image {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 8px;
  background: #fafafa;
}

.product-info {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.product-name {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  line-height: 1.4;
}

.product-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 4px;
}

.product-meta {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 16px;
  background: #fff7f0;
  border-radius: 4px;
  border: 1px solid #ffe7d6;
}

.meta-item {
  display: flex;
  gap: 12px;
  font-size: 14px;
}

.meta-item .label {
  color: #999;
}

.meta-item .value {
  color: #333;
  font-weight: 500;
}

.product-price-section {
  padding: 20px;
  background: #fff7f0;
  border-radius: 4px;
  border: 1px solid #ffe7d6;
}

.price-label {
  font-size: 14px;
  color: #999;
  margin-bottom: 8px;
}

.price {
  color: #ff5000;
  font-weight: 700;
  font-size: 32px;
}

.price .symbol {
  font-size: 20px;
}

.product-actions {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.quantity-section {
  display: flex;
  align-items: center;
  gap: 16px;
}

.quantity-section .label {
  font-size: 14px;
  color: #666;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 8px;
}

.qty-btn {
  width: 36px;
  height: 36px;
  padding: 0;
  font-weight: bold;
  font-size: 18px;
  border-radius: 4px;
  background: #fff;
  border: 1px solid #ddd;
  color: #333;
}

.qty-btn:hover:not(:disabled) {
  background: #ff5000;
  border-color: #ff5000;
  color: #fff;
}

.qty-value {
  min-width: 60px;
  text-align: center;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.action-buttons {
  display: flex;
  gap: 16px;
}

.action-buttons .el-button {
  flex: 1;
  height: 48px;
  font-size: 16px;
}

.loading {
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

@media (max-width: 768px) {
  .product-detail {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .main-image {
    height: 300px;
  }
  
  .action-buttons {
    flex-direction: column;
  }
}
</style>
