<template>
  <div>
    <Navbar />
    <el-container class="cart-container">
      <el-card>
        <h3>购物车</h3>
        <el-table :data="cart" v-if="cart.length" style="width: 100%">
          <el-table-column prop="productId" label="商品ID" width="90" />
          <el-table-column prop="quantity" label="数量" width="120">
            <template #default="{ row }">
              <el-input-number v-model="row.quantity" :min="1" @change="updateCount(row)" />
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="添加时间" />
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button type="danger" size="mini" @click="remove(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div v-if="!cart.length" class="empty">购物车为空，请先添加商品。</div>
        <div class="cart-footer" v-if="cart.length">
          <el-button type="primary" @click="checkout">去结算</el-button>
        </div>
      </el-card>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import Navbar from '@/components/Navbar.vue';
import { fetchCart, updateCart, deleteCart } from '@/api/cart';

const router = useRouter();
const cart = ref<any[]>([]);

const loadCart = async () => {
  const res: any = await fetchCart();
  cart.value = res.data;
};

const updateCount = async (row: any) => {
  await updateCart({ cartId: row.id, quantity: row.quantity });
  ElMessage.success('数量已更新');
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
.cart-container {
  padding: 24px;
}
.empty {
  padding: 40px;
  text-align: center;
  color: #909399;
}
.cart-footer {
  margin-top: 16px;
  text-align: right;
}
</style>
