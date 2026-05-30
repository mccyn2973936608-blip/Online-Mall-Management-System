<template>
  <div>
    <Navbar />
    <el-container class="checkout-container">
      <el-card>
        <h3>提交订单</h3>
        <el-form label-width="100px" ref="orderForm" :model="form" :rules="rules">
          <el-form-item label="收货人" prop="receiverName">
            <el-input v-model="form.receiverName" />
          </el-form-item>
          <el-form-item label="电话" prop="receiverPhone">
            <el-input v-model="form.receiverPhone" />
          </el-form-item>
          <el-form-item label="地址" prop="receiverAddress">
            <el-input v-model="form.receiverAddress" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input type="textarea" v-model="form.remark" />
          </el-form-item>
          <el-form-item label="购物车">
            <el-table :data="cart" style="width: 100%" v-if="cart.length">
              <el-table-column prop="productId" label="商品ID" width="90" />
              <el-table-column prop="quantity" label="数量" width="120" />
              <el-table-column prop="createTime" label="添加时间" />
            </el-table>
            <div v-if="!cart.length" class="empty">购物车为空，无法提交订单。</div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submit" :disabled="!cart.length">提交订单</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import Navbar from '@/components/Navbar.vue';
import { fetchCart } from '@/api/cart';
import { submitOrder as submitOrderApi } from '@/api/order';

const router = useRouter();
const cart = ref<any[]>([]);
const orderForm = ref();
const form = reactive({ receiverName: '', receiverPhone: '', receiverAddress: '', remark: '' });
const rules = {
  receiverName: [{ required: true, message: '请输入收货人', trigger: 'blur' }],
  receiverPhone: [{ required: true, message: '请输入收货电话', trigger: 'blur' }],
  receiverAddress: [{ required: true, message: '请输入收货地址', trigger: 'blur' }],
};

const loadCart = async () => {
  const res: any = await fetchCart();
  cart.value = res.data;
};

const submit = () => {
  orderForm.value.validate(async (valid: boolean) => {
    if (!valid) return;
    try {
      const cartIds = cart.value.map((item) => item.id);
      const res: any = await submitOrderApi({ ...form, cartIds });
      ElMessage.success('订单提交成功');
      router.push({ name: 'MyOrders' });
    } catch (err: any) {
      ElMessage.error(err.response?.data?.message || '提交失败');
    }
  });
};

onMounted(loadCart);
</script>

<style scoped>
.checkout-container {
  padding: 24px;
}
.empty {
  padding: 24px;
  color: #909399;
}
</style>
