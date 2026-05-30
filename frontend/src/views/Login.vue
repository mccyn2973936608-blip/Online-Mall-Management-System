<template>
  <div class="page">
    <Navbar />
    <el-card class="page-card">
      <h2>用户登录</h2>
      <el-form :model="form" :rules="rules" ref="loginForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="form.rememberMe">记住我</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">登录</el-button>
          <el-button type="text" @click="goRegister">注册账号</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import Navbar from '@/components/Navbar.vue';
import { login } from '@/api/auth';
import { useUserStore } from '@/store/user';

const router = useRouter();
const userStore = useUserStore();
const form = ref({ username: '', password: '', rememberMe: false });
const loginForm = ref();

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
};

const submit = () => {
  loginForm.value.validate(async (valid: boolean) => {
    if (!valid) return;
    try {
      const res: any = await login(form.value);
      userStore.setUser(res.data.token, res.data.user);
      ElMessage.success('登录成功');
      router.push({ name: 'Home' });
    } catch (err: any) {
      ElMessage.error(err.response?.data?.message || '登录失败');
    }
  });
};

const goRegister = () => {
  router.push({ name: 'Register' });
};
</script>

<style scoped>
.page {
  min-height: 100vh;
}
.page-card {
  width: 420px;
  margin: 100px auto;
  padding: 24px;
}
</style>
