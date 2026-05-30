<template>
  <div>
    <Navbar />
    <el-container class="orders-container">
      <el-card>
        <h3>我的订单</h3>
        <el-collapse v-model:active-name="activeNames">
          <el-collapse-item v-for="item in orders" :key="item.order.id" :title="`订单号：${item.order.orderNo} ￥${item.order.totalAmount} 状态：${statusText(item.order.status)}`" :name="String(item.order.id)">
            <div class="order-info">
              <p>收货人：{{ item.order.receiverName }}</p>
              <p>地址：{{ item.order.receiverAddress }}</p>
              <p>电话：{{ item.order.receiverPhone }}</p>
            </div>
            <el-table :data="item.items" style="width: 100%">
              <el-table-column prop="productName" label="商品" />
              <el-table-column prop="quantity" label="数量" width="90" />
              <el-table-column prop="unitPrice" label="单价" width="120" />
              <el-table-column prop="totalPrice" label="小计" width="120" />
            </el-table>
            <div class="order-actions" v-if="item.order.status === 0">
              <el-button type="danger" @click="cancel(item.order.id)">取消订单</el-button>
            </div>
          </el-collapse-item>
        </el-collapse>
      </el-card>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import Navbar from '@/components/Navbar.vue';
import { getMyOrders } from '@/api/user';
import { cancelOrder } from '@/api/order';

const orders = ref<any[]>([]);
const activeNames = ref<string[]>([]);

const loadOrders = async () => {
  const res: any = await getMyOrders();
  orders.value = res.data;
};

const statusText = (status: number) => {
  return status === 0 ? '待确认' : status === 1 ? '已确认' : status === 2 ? '已完成' : '已取消';
};

const cancel = async (id: number) => {
  await ElMessageBox.confirm('您确定要取消该订单吗？', '确认操作');
  try {
    await cancelOrder(id);
    ElMessage.success('订单已取消');
    loadOrders();
  } catch (err: any) {
    ElMessage.error(err.response?.data?.message || '取消失败');
  }
};

onMounted(loadOrders);
</script>

<style scoped>
.orders-container {
  padding: 24px;
}
.order-info {
  margin-bottom: 16px;
}
.order-actions {
  margin-top: 12px;
}
</style>
