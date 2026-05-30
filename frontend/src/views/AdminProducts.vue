<template>
  <div class="admin-page">
    <div class="bg-gradient"></div>
    <Navbar />
    
    <div class="main-content">
      <div class="page-header">
        <div>
          <h1 class="page-title">🎁 商品管理</h1>
          <p class="page-subtitle">管理商城商品，上架下架商品</p>
        </div>
        <button class="btn-primary" @click="openDialog">
          <span>+ 新增商品</span>
        </button>
      </div>
      
      <div class="glass-card">
        <div class="table-container">
          <table class="data-table">
            <thead>
              <tr>
                <th>商品名称</th>
                <th>分类</th>
                <th>价格</th>
                <th>库存</th>
                <th>状态</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="product in products" :key="product.id">
                <td>
                  <span class="product-name">{{ product.name }}</span>
                </td>
                <td>
                  <span class="category-tag">{{ product.categoryName }}</span>
                </td>
                <td>
                  <span class="price-text">¥{{ product.price }}</span>
                </td>
                <td>
                  <span :class="['stock-text', product.stock < 10 ? 'stock-low' : '']">{{ product.stock }}</span>
                </td>
                <td>
                  <span :class="['status-badge', product.status === 1 ? 'status-active' : 'status-inactive']">
                    {{ product.status === 1 ? '上架' : '下架' }}
                  </span>
                </td>
                <td>
                  <div class="action-buttons">
                    <button class="btn-outline btn-small" @click="editProduct(product)">编辑</button>
                    <button class="btn-warning btn-small" @click="toggleStatus(product)">{{ product.status === 1 ? '下架' : '上架' }}</button>
                    <button class="btn-danger btn-small" @click="deleteProduct(product.id)">删除</button>
                  </div>
                </td>
              </tr>
              <tr v-if="products.length === 0">
                <td colspan="6" class="empty-state">
                  <div class="empty-icon">📭</div>
                  <div>暂无商品数据</div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    
    <div v-if="dialogVisible" class="modal-overlay" @click.self="dialogVisible = false">
      <div class="modal-card" style="max-width: 600px;">
        <div class="modal-header">
          <h3 class="modal-title">{{ editingId ? '编辑商品' : '新增商品' }}</h3>
          <button class="modal-close" @click="dialogVisible = false">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label class="form-label">商品名称</label>
            <input v-model="form.name" class="form-input" placeholder="请输入商品名称" />
          </div>
          <div class="form-group">
            <label class="form-label">商品分类</label>
            <select v-model="form.categoryId" class="form-input">
              <option value="" disabled>请选择分类</option>
              <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
            </select>
          </div>
          <div class="form-row">
            <div class="form-group" style="flex: 1;">
              <label class="form-label">价格</label>
              <input v-model.number="form.price" type="number" min="0" class="form-input" placeholder="0.00" />
            </div>
            <div class="form-group" style="flex: 1;">
              <label class="form-label">库存</label>
              <input v-model.number="form.stock" type="number" min="0" class="form-input" placeholder="0" />
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">商品描述</label>
            <textarea v-model="form.description" class="form-input" rows="3" placeholder="请输入商品描述"></textarea>
          </div>
          <div class="form-group">
            <label class="form-label">图片链接</label>
            <input v-model="form.imageUrl" class="form-input" placeholder="https://..." />
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-outline" @click="dialogVisible = false">取消</button>
          <button class="btn-primary" @click="saveProduct">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import Navbar from '@/components/Navbar.vue';
import { fetchProducts, fetchCategories, createProduct, updateProduct, changeProductStatus, deleteProduct as apiDeleteProduct } from '@/api/product';

const products = ref<any[]>([]);
const categories = ref<any[]>([]);
const dialogVisible = ref(false);
const editingId = ref<number | null>(null);
const form = reactive({ name: '', categoryId: null as number | null, price: 0, stock: 0, description: '', imageUrl: '', status: 1, categoryName: '' });

const loadProducts = async () => {
  const res: any = await fetchProducts({ page: 1, size: 100 });
  products.value = res.data.records || [];
};

const loadCategories = async () => {
  const res: any = await fetchCategories();
  categories.value = res.data || [];
};

const openDialog = () => {
  editingId.value = null;
  form.name = '';
  form.categoryId = null;
  form.price = 0;
  form.stock = 0;
  form.description = '';
  form.imageUrl = '';
  dialogVisible.value = true;
};

const editProduct = (row: any) => {
  editingId.value = row.id;
  form.name = row.name;
  form.categoryId = row.categoryId;
  form.price = row.price;
  form.stock = row.stock;
  form.description = row.description;
  form.imageUrl = row.imageUrl;
  dialogVisible.value = true;
};

const saveProduct = async () => {
  try {
    if (editingId.value) {
      await updateProduct({ id: editingId.value, ...form });
      ElMessage.success('商品已更新');
    } else {
      await createProduct(form);
      ElMessage.success('商品已创建');
    }
    dialogVisible.value = false;
    loadProducts();
  } catch (err: any) {
    ElMessage.error(err.response?.data?.message || '保存失败');
  }
};

const toggleStatus = async (row: any) => {
  await changeProductStatus(row.id, row.status === 1 ? 0 : 1);
  ElMessage.success('状态已更新');
  loadProducts();
};

const deleteProduct = async (id: number) => {
  await ElMessageBox.confirm('确定删除该商品吗？', '确认删除');
  await apiDeleteProduct(id);
  ElMessage.success('删除成功');
  loadProducts();
};

onMounted(async () => {
  await loadCategories();
  await loadProducts();
});
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
  display: flex;
  justify-content: space-between;
  align-items: center;
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
}

.btn-warning {
  background: rgba(255, 200, 0, 0.2);
  border: 1px solid rgba(255, 200, 0, 0.5);
  border-radius: 8px;
  color: #ffc800;
  font-weight: 500;
  padding: 8px 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-warning:hover {
  background: rgba(255, 200, 0, 0.3);
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
}

.btn-small {
  padding: 6px 12px;
  font-size: 0.85rem;
}

.glass-card {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
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

.product-name {
  color: white;
  font-weight: 500;
}

.category-tag {
  display: inline-block;
  padding: 4px 10px;
  background: rgba(0, 212, 255, 0.2);
  color: #00d4ff;
  border-radius: 6px;
  font-size: 0.85rem;
}

.price-text {
  color: #00ff7f;
  font-weight: 600;
  font-size: 1.1rem;
}

.stock-text {
  color: rgba(255, 255, 255, 0.8);
}

.stock-low {
  color: #ff4757;
  font-weight: 600;
}

.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 50px;
  font-size: 0.8rem;
  font-weight: 600;
}

.status-active {
  background: rgba(0, 255, 127, 0.2);
  color: #00ff7f;
  border: 1px solid rgba(0, 255, 127, 0.4);
}

.status-inactive {
  background: rgba(255, 71, 87, 0.2);
  color: #ff4757;
  border: 1px solid rgba(255, 71, 87, 0.4);
}

.action-buttons {
  display: flex;
  gap: 8px;
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

.form-row {
  display: flex;
  gap: 16px;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-card {
  background: rgba(10, 10, 26, 0.95);
  border: 1px solid rgba(102, 126, 234, 0.3);
  border-radius: 20px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.4);
  animation: modalIn 0.3s ease;
}

@keyframes modalIn {
  from { opacity: 0; transform: translateY(-20px) scale(0.95); }
  to { opacity: 1; transform: translateY(0) scale(1); }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.modal-title {
  color: white;
  font-size: 1.25rem;
  font-weight: 700;
}

.modal-close {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.05);
  border: none;
  color: rgba(255, 255, 255, 0.7);
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.modal-close:hover {
  background: rgba(255, 71, 87, 0.2);
  color: #ff4757;
}

.modal-body { padding: 24px; }

.form-group { margin-bottom: 20px; }

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

.form-input::placeholder {
  color: rgba(255, 255, 255, 0.3);
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .form-row { flex-direction: column; }
}
</style>
