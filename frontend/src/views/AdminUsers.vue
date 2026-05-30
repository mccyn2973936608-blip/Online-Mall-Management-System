<template>
  <div class="admin-page">
    <div class="bg-gradient"></div>
    <Navbar />
    
    <div class="main-content">
      <div class="page-header">
        <div>
          <h1 class="page-title">👥 会员管理</h1>
          <p class="page-subtitle">管理系统会员用户，编辑用户信息</p>
        </div>
        <button class="btn-primary" @click="openDialog">
          <span>+ 新增会员</span>
        </button>
      </div>
      
      <div class="glass-card">
        <div class="table-container">
          <table class="data-table">
            <thead>
              <tr>
                <th>用户名</th>
                <th>手机号</th>
                <th>邮箱</th>
                <th>角色</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in users" :key="user.id">
                <td>
                  <div class="user-info">
                    <div class="user-avatar">{{ user.username?.charAt(0)?.toUpperCase() || 'U' }}</div>
                    <span class="user-name">{{ user.username }}</span>
                  </div>
                </td>
                <td>{{ user.phone }}</td>
                <td>{{ user.email }}</td>
                <td>
                  <span :class="['role-badge', user.role === 1 ? 'role-admin' : 'role-member']">
                    {{ user.role === 1 ? '管理员' : '普通会员' }}
                  </span>
                </td>
                <td>
                  <div class="action-buttons">
                    <button class="btn-outline btn-small" @click="editUser(user)">编辑</button>
                    <button class="btn-danger btn-small" @click="deleteUser(user.id)">删除</button>
                  </div>
                </td>
              </tr>
              <tr v-if="users.length === 0">
                <td colspan="5" class="empty-state">
                  <div class="empty-icon">📭</div>
                  <div>暂无用户数据</div>
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
          <h3 class="modal-title">{{ editingId ? '编辑会员' : '新增会员' }}</h3>
          <button class="modal-close" @click="dialogVisible = false">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label class="form-label">用户名</label>
            <input v-model="form.username" class="form-input" placeholder="请输入用户名" />
          </div>
          <div class="form-group">
            <label class="form-label">密码</label>
            <input v-model="form.password" type="password" class="form-input" placeholder="请输入密码" />
          </div>
          <div class="form-group">
            <label class="form-label">手机号</label>
            <input v-model="form.phone" class="form-input" placeholder="请输入手机号" />
          </div>
          <div class="form-group">
            <label class="form-label">邮箱</label>
            <input v-model="form.email" class="form-input" placeholder="请输入邮箱" />
          </div>
          <div class="form-group">
            <label class="form-label">角色</label>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" v-model="form.role" :value="0" />
                <span>普通会员</span>
              </label>
              <label class="radio-option">
                <input type="radio" v-model="form.role" :value="1" />
                <span>管理员</span>
              </label>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-outline" @click="dialogVisible = false">取消</button>
          <button class="btn-primary" @click="saveUser">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import Navbar from '@/components/Navbar.vue';
import { fetchAdminUsers, createAdminUser, updateAdminUser, deleteAdminUser } from '@/api/admin';

const users = ref<any[]>([]);
const dialogVisible = ref(false);
const editingId = ref<number | null>(null);
const form = reactive({ username: '', password: '', phone: '', email: '', role: 0 });

const loadUsers = async () => {
  const res: any = await fetchAdminUsers({ page: 1, size: 100 });
  users.value = res.data.records || [];
};

const openDialog = () => {
  editingId.value = null;
  form.username = '';
  form.password = '';
  form.phone = '';
  form.email = '';
  form.role = 0;
  dialogVisible.value = true;
};

const editUser = (row: any) => {
  editingId.value = row.id;
  form.username = row.username;
  form.password = '';
  form.phone = row.phone;
  form.email = row.email;
  form.role = row.role;
  dialogVisible.value = true;
};

const saveUser = async () => {
  try {
    if (editingId.value) {
      await updateAdminUser({ id: editingId.value, ...form });
      ElMessage.success('更新成功');
    } else {
      await createAdminUser(form);
      ElMessage.success('创建成功');
    }
    dialogVisible.value = false;
    loadUsers();
  } catch (err: any) {
    ElMessage.error(err.response?.data?.message || '保存失败');
  }
};

const deleteUser = async (id: number) => {
  await ElMessageBox.confirm('确认删除该会员吗？', '操作确认');
  await deleteAdminUser(id);
  ElMessage.success('删除成功');
  loadUsers();
};

onMounted(loadUsers);
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
  background: 
    radial-gradient(circle at 20% 50%, rgba(102, 126, 234, 0.1) 0%, transparent 50%);
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

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 700;
  font-size: 0.95rem;
}

.user-name {
  color: white;
  font-weight: 500;
}

.role-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 50px;
  font-size: 0.8rem;
  font-weight: 600;
}

.role-admin {
  background: rgba(102, 126, 234, 0.2);
  color: #667eea;
  border: 1px solid rgba(102, 126, 234, 0.4);
}

.role-member {
  background: rgba(0, 212, 255, 0.2);
  color: #00d4ff;
  border: 1px solid rgba(0, 212, 255, 0.4);
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
  from {
    opacity: 0;
    transform: translateY(-20px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
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

.modal-body {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
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

.form-input::placeholder {
  color: rgba(255, 255, 255, 0.3);
}

.radio-group {
  display: flex;
  gap: 24px;
}

.radio-option {
  display: flex;
  align-items: center;
  gap: 8px;
  color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
}

.radio-option input {
  accent-color: #667eea;
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
  
  .data-table {
    font-size: 0.85rem;
  }
  
  .data-table th,
  .data-table td {
    padding: 12px;
  }
}
</style>
