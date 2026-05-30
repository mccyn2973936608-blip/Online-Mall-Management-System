<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-card">
        <h2 class="login-title">用户登录</h2>
        <el-form :model="form" class="login-form">
          <el-form-item>
            <el-input v-model="form.username" placeholder="用户名" size="large" />
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.password" type="password" placeholder="密码" size="large" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="large" class="login-btn" @click="handleLogin" :loading="loading">
              登录
            </el-button>
          </el-form-item>
          <div class="login-footer">
            还没有账号？<router-link to="/register">立即注册</router-link>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { login } from '@/api/auth';
import { useUserStore } from '@/store/user';

const router = useRouter();
const userStore = useUserStore();
const loading = ref(false);
const form = reactive({
  username: '',
  password: ''
});

const handleLogin = async () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码');
    return;
  }
  
  loading.value = true;
  try {
    const res: any = await login(form);
    userStore.setToken(res.data.token);
    userStore.fetchUserInfo();
    ElMessage.success('登录成功');
    router.push('/');
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '登录失败');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-card {
  background: #fff;
  padding: 40px;
  border-radius: 12px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.login-title {
  text-align: center;
  color: #333;
  margin-bottom: 32px;
  font-size: 28px;
  font-weight: 600;
}

.login-form {
  margin-top: 20px;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  background: linear-gradient(to right, #ff5000, #ff6a33);
  border: none;
}

.login-btn:hover {
  background: linear-gradient(to right, #ff6a33, #ff8533);
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  color: #666;
  font-size: 14px;
}

.login-footer a {
  color: #ff5000;
  text-decoration: none;
  font-weight: 500;
}

.login-footer a:hover {
  text-decoration: underline;
}
</style>
