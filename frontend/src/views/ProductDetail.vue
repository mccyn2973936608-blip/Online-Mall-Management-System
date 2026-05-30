<template>
  <div>
    <Navbar />
    <el-container class="detail-container">
      <el-card class="detail-card">
        <el-row>
          <el-col :span="10">
            <img :src="product.imageUrl || 'https://via.placeholder.com/420'" alt="商品图" class="detail-image" />
          </el-col>
          <el-col :span="14">
            <h2>{{ product.name }}</h2>
            <p>{{ product.description }}</p>
            <p>分类：{{ product.categoryName }}</p>
            <p>价格：¥{{ product.price }}</p>
            <p>库存：{{ product.stock }}</p>
            <el-input-number v-model="quantity" :min="1" :max="product.stock || 1" />
            <el-button type="primary" @click="addToCart">加入购物车</el-button>
          </el-col>
        </el-row>
      </el-card>
    </el-container>
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
    ElMessage.success('商品已加入购物车');
    router.push({ name: 'Cart' });
  } catch (err: any) {
    ElMessage.error(err.response?.data?.message || '操作失败');
  }
};

onMounted(loadProduct);
</script>

<style scoped>
.detail-container {
  padding: 24px;
}
.detail-card {
  max-width: 1000px;
  margin: auto;
}
.detail-image {
  width: 100%;
  height: 420px;
  object-fit: cover;
}
</style>
