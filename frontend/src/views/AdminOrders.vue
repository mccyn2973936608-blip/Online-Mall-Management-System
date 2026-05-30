<template>
  <div class="admin-page">
    <div class="bg-gradient"></div>
    <Navbar />
    
    <div class="main-content">
      <div class="page-header">
        <div>
          <h1 class="page-title">📋 订单管理</h1>
          <p class="page-subtitle">处理客户订单，确认和完成订单</p>
        </div>
      </div>
      
      <div class="glass-card">
        <div class="search-form">
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">订单号</label>
              <input v-model="query.orderNo" class="form-input" placeholder="订单号" />
            </div>
            <div class="form-group">
              <label class="form-label">会员名</label>
              <input v-model="query.username" class="form-input" placeholder="会员名" />
            </div>
            <div class="form-group">
              <label class="form-label">状态</label>
              <select v-model="query.status" class="form-input">
                <option :value="null">全部</option>
                <option :value="0">待确认</option>
                <option :value="1">已确认</option>
                <option :value="2">已完成</option>
                <option :value="3">已取消</option>
              </select>
            </div>
            <div class="form-group form-btn">
              <button class="btn-primary" @click="loadOrders">查询</button>
            </div>
          </div>
        </div>
        
        <div class="table-container">
          <table class="data-table">
            <thead>
              <tr>
                <th>订单号</th>
                <th>会员名</th>
                <th>金额</th>
                <th>状态</th>
                <th>收货地址</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in orders" :key="order.id">
                <td><span class="order-no">{{ order.orderNo }}</span></td>
                <td><span class="username">{{ order.username }}</span></td>
                <td><span class="price">¥{{ order.totalAmount }}</span></td>
                <td>
                  <span :class="['status-badge', getStatusClass(order.status)]">
                    {{ statusText(order.status) }}
                  </span>
                </td>
                <td><span class="address">{{ order.receiverAddress }}</span></td>
                <td>
                  <div class="action-buttons">
                    <button 
                      v-if="order.status === 0" 
                      class="btn-outline btn-small" 
                      @click="confirm(order.id)"
                    >
                      确认
                    </button>
                    <button 
                      v-if="order.status === 1" 
                      class="btn-success btn-small" 
                      @click="complete(order.id)"
                    >
                      完成
                    </button>
                    <button 
                      v-if="order.status !== 3" 
                      class="btn-danger btn-small" 
                      @click="cancel(order.id)"
                    >
                      取消
                    </button>
                  </div>
                </td>
              </tr>
              <tr v-if="orders.length === 0">
                <td colspan="6" class="empty-state">
                  <div class="empty-icon">📭</div>
                  <div>暂无订单数据</div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import Navbar from '@/components/Navbar.vue';
import { fetchAdminOrders, confirmOrder, completeOrder, cancelAdminOrder } from '@/api/admin';

const orders = ref<any[]>([]);
const query = reactive({ orderNo: '', username: '', status: null as number | null });

const loadOrders = async () => {
  const res: any = await fetchAdminOrders({ 
    page: 1, 
    size: 100, 
    orderNo: query.orderNo, 
    username: query.username, 
    status: query.status 
  });
  orders.value = res.data?.records || [];
};

const statusText = (status: number) => {
  return status === 0 ? '待确认' : status === 1 ? '已确认' : status === 2 ? '已完成' : '已取消';
};

const getStatusClass = (status: number) => {
  return status === 0 ? 'status-pending' : status === 1 ? 'status-confirmed' : status === 2 ? 'status-completed' : 'status-cancelled';
};

const confirm = async (id: number) => {
  await ElMessageBox.confirm('确认订单并更新状态吗？', '确认操作');
  await confirmOrder(id);
  ElMessage.success('订单已确认');
  loadOrders();
};

const complete = async (id: number) => {
  await ElMessageBox.confirm('标记订单为已完成吗？', '确认操作');
  await completeOrder(id);
  ElMessage.success('订单已完成');
  loadOrders();
};

const cancel = async (id: number) => {
  await ElMessageBox.confirm('确认取消订单吗？', '确认操作');
  await cancelAdminOrder(id);
  ElMessage.success('订单已取消');
  loadOrders();
};

onMounted(loadOrders);
</script>

<style scoped>
.admin-page {
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
  background: radial-gradient(circle at 20% 50%, rgba(102, 126, 234, 0.1) 0%, transparent 50%);
  pointer-events: none;
  z-index: 1;
}

.main-content {
  position: relative;
  z-index: 10;
  padding: 40px 5%;
  max-width: 1600px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 32px;
}

.page-title {
  font-size: 2rem;
  font-weight: 900;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 8px;
}

.page-subtitle {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.95rem;
}

.glass-card {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
}

.search-form {
  padding: 24px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.form-row {
  display: flex;
  gap: 16px;
  align-items: flex-end;
  flex-wrap: wrap;
}

.form-group {
  flex: 1;
  min-width: 180px;
}

.form-btn {
  flex: 0 0 auto;
}

.form-label {
  display: block;
  color: rgba(255, 255, 255, 0.8);
  font-weight: 500;
  margin-bottom: 8px;
  font-size: 0.9rem;
}

.form-input {
  width: 100%;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(102, 126, 234, 0.3);
  border-radius: 12px;
  color: white;
  font-size: 0.95rem;
  transition: all 0.2s ease;
  box-sizing: border-box;
}

.form-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 12px;
  color: white;
  font-weight: 600;
  padding: 12px 24px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

.btn-outline {
  background: transparent;
  border: 1px solid rgba(102, 126, 234, 0.5);
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.8);
  font-weight: 500;
  padding: 8px 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-outline:hover {
  background: rgba(102, 126, 234, 0.15);
  border-color: #667eea;
  color: white;
}

.btn-success {
  background: rgba(0, 255, 127, 0.2);
  border: 1px solid rgba(0, 255, 127, 0.5);
  border-radius: 8px;
  color: #00ff7f;
  font-weight: 500;
  padding: 8px 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-success:hover {
  background: rgba(0, 255, 127, 0.3);
  color: white;
}

.btn-danger {
  background: rgba(255, 71, 87, 0.2);
  border: 1px solid rgba(255, 71, 87, 0.5);
  border-radius: 8px;
  color: #ff4757;
  font-weight: 500;
  padding: 8px 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-danger:hover {
  background: rgba(255, 71, 87, 0.3);
  color: white;
}

.btn-small {
  padding: 6px 12px;
  font-size: 0.85rem;
}

.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table thead {
  background: rgba(102, 126, 234, 0.1);
}

.data-table th {
  padding: 16px 20px;
  text-align: left;
  color: white;
  font-weight: 600;
  font-size: 0.9rem;
  border-bottom: 1px solid rgba(102, 126, 234, 0.2);
}

.data-table td {
  padding: 16px 20px;
  color: rgba(255, 255, 255, 0.8);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.data-table tbody tr {
  transition: background 0.2s ease;
}

.data-table tbody tr:hover {
  background: rgba(102, 126, 234, 0.05);
}

.order-no {
  color: #00d4ff;
  font-weight: 600;
  font-family: monospace;
}

.username {
  color: white;
  font-weight: 500;
}

.price {
  color: #00ff7f;
  font-weight: 600;
  font-size: 1.1rem;
}

.address {
  color: rgba(255, 255, 255, 0.7);
}

.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 50px;
  font-size: 0.8rem;
  font-weight: 600;
}

.status-pending {
  background: rgba(255, 200, 0, 0.2);
  color: #ffc800;
  border: 1px solid rgba(255, 200, 0, 0.4);
}

.status-confirmed {
  background: rgba(102, 126, 234, 0.2);
  color: #667eea;
  border: 1px solid rgba(102, 126, 234, 0.4);
}

.status-completed {
  background: rgba(0, 255, 127, 0.2);
  color: #00ff7f;
  border: 1px solid rgba(0, 255, 127, 0.4);
}

.status-cancelled {
  background: rgba(255, 71, 87, 0.2);
  color: #ff4757;
  border: 1px solid rgba(255, 71, 87, 0.4);
}

.action-buttons {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.empty-state {
  text-align: center;
  padding: 48px 20px;
  color: rgba(255, 255, 255, 0.5);
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 16px;
}

@media (max-width: 768px) {
  .form-row {
    flex-direction: column;
    align-items: stretch;
  }
  
  .form-btn {
    width: 100%;
  }
}
</style>
