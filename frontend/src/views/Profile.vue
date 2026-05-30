<template>
  <div>
    <Navbar />
    <el-container class="profile-container">
      <el-card class="profile-card">
        <h3>个人信息</h3>
        <el-form :model="form" :rules="rules" ref="profileForm" label-width="100px">
          <el-form-item label="昵称" prop="nickname">
            <el-input v-model="form.nickname" />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="form.phone" />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" />
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="form.address" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="saveProfile">保存信息</el-button>
          </el-form-item>
        </el-form>
      </el-card>
      <el-card class="profile-card">
        <h3>修改密码</h3>
        <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input type="password" v-model="passwordForm.oldPassword" />
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input type="password" v-model="passwordForm.newPassword" />
          </el-form-item>
          <el-form-item>
            <el-button type="warning" @click="changePassword">修改密码</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import Navbar from '@/components/Navbar.vue';
import { getProfile, updateProfile, updatePassword } from '@/api/user';

const profileForm = ref();
const passwordFormRef = ref();
const form = reactive({ nickname: '', phone: '', email: '', address: '' });
const passwordForm = reactive({ oldPassword: '', newPassword: '' });
const rules = {
  nickname: [{ required: true, message: '昵称不能为空', trigger: 'blur' }],
  phone: [{ required: true, message: '手机号不能为空', trigger: 'blur' }],
  email: [{ required: true, message: '邮箱不能为空', trigger: 'blur' }],
};
const passwordRules = {
  oldPassword: [{ required: true, message: '旧密码不能为空', trigger: 'blur' }],
  newPassword: [{ required: true, message: '新密码不能为空', trigger: 'blur' }],
};

const loadProfile = async () => {
  const res: any = await getProfile();
  Object.assign(form, res.data);
};

const saveProfile = () => {
  profileForm.value.validate(async (valid: boolean) => {
    if (!valid) return;
    try {
      await updateProfile(form);
      ElMessage.success('个人信息已保存');
    } catch (err: any) {
      ElMessage.error(err.response?.data?.message || '保存失败');
    }
  });
};

const changePassword = () => {
  passwordFormRef.value.validate(async (valid: boolean) => {
    if (!valid) return;
    try {
      await updatePassword(passwordForm);
      ElMessage.success('密码修改成功');
      passwordForm.oldPassword = '';
      passwordForm.newPassword = '';
    } catch (err: any) {
      ElMessage.error(err.response?.data?.message || '修改失败');
    }
  });
};

onMounted(loadProfile);
</script>

<style scoped>
.profile-container {
  padding: 24px;
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
}
.profile-card {
  width: 100%;
  max-width: 620px;
}
</style>
