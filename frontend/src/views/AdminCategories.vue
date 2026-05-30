<template>
  <div class="admin-page">
    <div class="bg-gradient"></div>
    <Navbar />
    
    <div class="main-content">
      <div class="page-header">
        <div>
          <h1 class="page-title">🏷️ 分类管理</h1>
          <p class="page-subtitle">管理商品分类，调整分类顺序</p>
        </div>
        <button class="btn-primary" @click="dialogVisible = true">
          <span>+ 新增分类</span>
        </button>
      </div>
      
      <div class="glass-card">
        <div class="table-container">
          <table class="data-table">
            <thead>
              <tr>
                <th>分类名称</th>
                <th>描述</th>
                <th>排序</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="cat in categories" :key="cat.id">
                <td><span class="category-name">{{ cat.name }}</span></td>
                <td><span class="category-desc">{{ cat.description }}</span></td>
                <td><span class="sort-badge">{{ cat.sortOrder }}</span></td>
                <td>
                  <div class="action-buttons">
                    <button class="btn-outline btn-small" @click="editCategory(cat)">编辑</button>
                    <button class="btn-danger btn-small" @click="removeCategory(cat.id)">删除</button>
                  </div>
                </td>
              </tr>
              <tr v-if="categories.length === 0">
                <td colspan="4" class="empty-state">
                  <div class="empty-icon">📭</div>
                  <div>暂无分类数据</div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    
    <div v-if="dialogVisible" class="modal-overlay" @click.self="dialogVisible = false">
      <div class="modal-card">
        <div class="modal-header">
          <h3 class="modal-title">{{ editingId ? '编辑分类' : '新增分类' }}</h3>
          <button class="modal-close" @click="dialogVisible = false">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label class="form-label">分类名称</label>
            <input v-model="form.name" class="form-input" placeholder="请输入分类名称" />
          </div>
          <div class="form-group">
            <label class="form-label">描述</label>
            <input v-model="form.description" class="form-input" placeholder="请输入描述" />
          </div>
          <div class="form-group">
            <label class="form-label">排序</label>
            <input v-model.number="form.sortOrder" type="number" min="0" class="form-input" placeholder="0" />
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-outline" @click="dialogVisible = false">取消</button>
          <button class="btn-primary" @click="saveCategory">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import Navbar from '@/components/Navbar.vue';
import { fetchCategories, createCategory, updateCategory, deleteCategory as apiDeleteCategory } from '@/api/admin';

const categories = ref<any[]>([]);
const dialogVisible = ref(false);
const editingId = ref<number | null>(null);
const form = reactive({ name: '', description: '', sortOrder: 0 });

const loadCategories = async () => {
  const res: any = await fetchCategories();
  categories.value = res.data || [];
};

const editCategory = (row: any) => {
  editingId.value = row.id;
  form.name = row.name;
  form.description = row.description;
  form.sortOrder = row.sortOrder;
  dialogVisible.value = true;
};

const saveCategory = async () => {
  try {
    if (editingId.value) {
      await updateCategory({ id: editingId.value, ...form });
      ElMessage.success('分类已更新');
    } else {
      await createCategory(form);
      ElMessage.success('分类已创建');
    }
    dialogVisible.value = false;
    editingId.value = null;
    form.name = '';
    form.description = '';
    form.sortOrder = 0;
    loadCategories();
  } catch (err: any) {
    ElMessage.error(err.response?.data?.message || '保存失败');
  }
};

const removeCategory = async (id: number) => {
  await ElMessageBox.confirm('确认删除该分类吗？', '操作确认');
  await apiDeleteCategory(id);
  ElMessage.success('删除成功');
  loadCategories();
};

onMounted(loadCategories);
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

.category-name {
  color: white;
  font-weight: 500;
  font-size: 1.05rem;
}

.category-desc {
  color: rgba(255, 255, 255, 0.6);
}

.sort-badge {
  display: inline-block;
  padding: 4px 10px;
  background: rgba(0, 212, 255, 0.2);
  color: #00d4ff;
  border-radius: 6px;
  font-size: 0.85rem;
  font-weight: 600;
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
}
</style>
