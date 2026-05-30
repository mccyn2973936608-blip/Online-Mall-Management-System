<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-card">
        <h2 class="register-title">用户注册</h2>
        <el-form :model="form" class="register-form">
          <el-form-item>
            <el-input v-model="form.username" placeholder="用户名" size="large" />
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.password" type="password" placeholder="密码" size="large" />
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" size="large" />
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.nickname" placeholder="昵称" size="large" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="large" class="register-btn" @click="handleRegister" :loading="loading">
              注册
            </el-button>
          </el-form-item>
          <div class="register-footer">
            已有账号？<router-link to="/login">立即登录</router-link>
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
import { register } from '@/api/auth';

const router = useRouter();
const loading = ref(false);
const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: ''
});

const handleRegister = async () => {
  if (!form.username || !form.password || !form.confirmPassword) {
    ElMessage.warning('请填写所有必填项');
    return;
  }
  
  if (form.password !== form.confirmPassword) {
    ElMessage.warning('两次密码输入不一致');
    return;
  }
  
  loading.value = true;
  try {
    await register({
      username: form.username,
      password: form.password,
      nickname: form.nickname || form.username
    });
    ElMessage.success('注册成功，请登录');
    router.push('/login');
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '注册失败');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.register-card {
  background: #fff;
  padding: 40px;
  border-radius: 12px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.register-title {
  text-align: center;
  color: #333;
  margin-bottom: 32px;
  font-size: 28px;
  font-weight: 600;
}

.register-form {
  margin-top: 20px;
}

.register-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  background: linear-gradient(to right, #ff5000, #ff6a33);
  border: none;
}

.register-btn:hover {
  background: linear-gradient(to right, #ff6a33, #ff8533);
}

.register-footer {
  text-align: center;
  margin-top: 24px;
  color: #666;
  font-size: 14px;
}

.register-footer a {
  color: #ff5000;
  text-decoration: none;
  font-weight: 500;
}

.register-footer a:hover {
  text-decoration: underline;
}
</style>
