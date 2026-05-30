<template>
  <div class="cart-page">
    <Navbar />
    <div class="main-content">
      <h1 class="page-title">我的购物车</h1>
      
      <div class="cart-content" v-if="cart.length">
        <div class="cart-list">
          <div v-for="item in cart" :key="item.id" class="cart-item">
            <div class="item-image">
              <img :src="item.imageUrl || 'https://picsum.photos/100/100'" :alt="item.productName" />
            </div>
            <div class="item-info">
              <div class="item-name">{{ item.productName || '商品' + item.productId }}</div>
              <div class="item-price">¥{{ item.price || 0 }}</div>
              <div class="item-quantity">
                <span class="label">数量:</span>
                <div class="quantity-control">
                  <el-button 
                    class="qty-btn decrease"
                    size="small" 
                    :disabled="item.quantity <= 1" 
                    @click="decreaseQuantity(item)"
                  >−</el-button>
                  <span class="qty-value">{{ item.quantity }}</span>
                  <el-button 
                    class="qty-btn increase"
                    size="small" 
                    @click="increaseQuantity(item)"
                  >+</el-button>
                </div>
              </div>
            </div>
            <div class="item-total">
              <div class="total-label">小计</div>
              <div class="total-price">¥{{ ((item.price || 0) * item.quantity).toFixed(2) }}</div>
            </div>
            <div class="item-actions">
              <el-button type="danger" size="small" @click="remove(item.id)">删除</el-button>
            </div>
          </div>
        </div>
        
        <div class="cart-summary">
          <div class="summary-info">
            <span>共 {{ cart.length }} 件商品</span>
            <span class="summary-total">合计: ¥{{ totalAmount }}</span>
          </div>
          <el-button type="primary" size="large" class="checkout-btn" @click="checkout">去结算</el-button>
        </div>
      </div>
      
      <div v-else class="empty-cart">
        <div class="empty-icon">🛒</div>
        <div class="empty-text">购物车是空的</div>
        <el-button type="primary" @click="$router.push('/')">去逛逛</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import Navbar from '@/components/Navbar.vue';
import { fetchCart, updateCart, deleteCart } from '@/api/cart';

const router = useRouter();
const cart = ref<any[]>([]);

const totalAmount = computed(() => {
  return cart.value.reduce((sum, item) => {
    return sum + (item.price || 0) * item.quantity;
  }, 0).toFixed(2);
});

const loadCart = async () => {
  const res: any = await fetchCart();
  cart.value = res.data || [];
};

const increaseQuantity = async (item: any) => {
  item.quantity += 1;
  await updateCart({ cartId: item.id, quantity: item.quantity });
  ElMessage.success('数量已更新');
};

const decreaseQuantity = async (item: any) => {
  if (item.quantity > 1) {
    item.quantity -= 1;
    await updateCart({ cartId: item.id, quantity: item.quantity });
    ElMessage.success('数量已更新');
  }
};

const remove = async (id: number) => {
  await ElMessageBox.confirm('确定删除该商品吗？', '确认删除');
  await deleteCart(id);
  ElMessage.success('已删除');
  loadCart();
};

const checkout = () => {
  router.push({ name: 'Checkout' });
};

onMounted(loadCart);
</script>

<style scoped>
.cart-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.main-content {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid #ff5000;
}

.cart-content {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.cart-list {
  padding: 20px;
}

.cart-item {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.cart-item:last-child {
  border-bottom: none;
}

.item-image {
  width: 100px;
  height: 100px;
  flex-shrink: 0;
  border-radius: 8px;
  overflow: hidden;
  background: #fafafa;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.item-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 300px;
}

.item-price {
  font-size: 14px;
  color: #999;
}

.item-quantity {
  display: flex;
  align-items: center;
  gap: 12px;
}

.item-quantity .label {
  color: #666;
  font-size: 14px;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 8px;
}

.qty-btn {
  width: 32px;
  height: 32px;
  padding: 0;
  font-weight: bold;
  font-size: 18px;
  border-radius: 4px;
}

.qty-btn.decrease {
  background: #fff;
  border: 1px solid #ddd;
  color: #333;
}

.qty-btn.decrease:hover:not(:disabled) {
  background: #f5f5f5;
  color: #ff5000;
}

.qty-btn.increase {
  background: #fff;
  border: 1px solid #ddd;
  color: #333;
}

.qty-btn.increase:hover {
  background: #ff5000;
  border-color: #ff5000;
  color: #fff;
}

.qty-value {
  min-width: 50px;
  text-align: center;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.item-total {
  text-align: center;
  min-width: 100px;
}

.total-label {
  font-size: 14px;
  color: #999;
  margin-bottom: 4px;
}

.total-price {
  font-size: 18px;
  font-weight: 700;
  color: #ff5000;
}

.item-actions {
  min-width: 80px;
}

.cart-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #fff;
  border-top: 2px solid #eee;
  border-radius: 0 0 8px 8px;
}

.summary-info {
  display: flex;
  gap: 20px;
  align-items: center;
  font-size: 14px;
  color: #666;
}

.summary-total {
  font-size: 20px;
  font-weight: 700;
  color: #ff5000;
}

.checkout-btn {
  padding: 12px 48px;
  font-size: 16px;
  background: linear-gradient(to right, #ff5000, #ff6a33);
  border: none;
}

.checkout-btn:hover {
  background: linear-gradient(to right, #ff6a33, #ff8533);
}

.empty-cart {
  background: #fff;
  border-radius: 8px;
  padding: 80px 20px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.empty-icon {
  font-size: 80px;
  margin-bottom: 20px;
}

.empty-text {
  color: #999;
  font-size: 16px;
  margin-bottom: 24px;
}

@media (max-width: 768px) {
  .cart-item {
    flex-wrap: wrap;
  }
  
  .item-image {
    width: 80px;
    height: 80px;
  }
  
  .item-total {
    min-width: auto;
  }
  
  .item-actions {
    min-width: auto;
  }
  
  .cart-summary {
    flex-direction: column;
    gap: 16px;
  }
  
  .checkout-btn {
    width: 100%;
  }
}
</style>
