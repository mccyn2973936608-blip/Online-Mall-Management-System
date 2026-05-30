<template>
  <div class="page">
    <Navbar />
    <el-card class="page-card">
      <h2>注册新会员</h2>
      <el-form :model="form" :rules="rules" ref="registerForm" label-width="90px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">注册</el-button>
          <el-button type="text" @click="goLogin">返回登录</el-button>
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
import { register } from '@/api/auth';

const router = useRouter();
const form = ref({ username: '', password: '', phone: '', email: '' });
const registerForm = ref();
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
};

const submit = () => {
  registerForm.value.validate(async (valid: boolean) => {
    if (!valid) return;
    try {
      await register(form.value);
      ElMessage.success('注册成功，请登录');
      router.push({ name: 'Login' });
    } catch (err: any) {
      ElMessage.error(err.response?.data?.message || '注册失败');
    }
  });
};

const goLogin = () => {
  router.push({ name: 'Login' });
};
</script>

<style scoped>
.page {
  min-height: 100vh;
}
.page-card {
  width: 500px;
  margin: 80px auto;
  padding: 24px;
}
</style>
