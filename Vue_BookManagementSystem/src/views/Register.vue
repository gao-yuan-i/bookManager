<template>
  <div class="register-page">
    <div class="register-card">
      <h1 class="register-title">📝 用户注册</h1>
      <p class="register-subtitle">创建您的图书管理系统账号</p>

      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label class="form-label">用户名 *</label>
          <input v-model="form.username" type="text" class="form-input" placeholder="请输入用户名（3-50位）" required />
        </div>

        <div class="form-group">
          <label class="form-label">密码 *</label>
          <input v-model="form.password" type="password" class="form-input" placeholder="请输入密码（6-20位）" required />
        </div>

        <div class="form-group">
          <label class="form-label">确认密码 *</label>
          <input v-model="form.confirmPassword" type="password" class="form-input" placeholder="请再次输入密码" required />
        </div>

        <div class="form-group">
          <label class="form-label">真实姓名</label>
          <input v-model="form.realName" type="text" class="form-input" placeholder="请输入真实姓名" />
        </div>

        <div class="form-group">
          <label class="form-label">手机号码</label>
          <input v-model="form.phone" type="text" class="form-input" placeholder="请输入手机号码" />
        </div>

        <div class="form-group">
          <label class="form-label">邮箱</label>
          <input v-model="form.email" type="email" class="form-input" placeholder="请输入邮箱" />
        </div>

        <p v-if="errorMsg" class="error-msg">{{ errorMsg }}</p>

        <button type="submit" class="btn btn-primary register-btn" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>
      </form>

      <p class="login-link">
        已有账号？<router-link to="/login">返回登录</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '@/api/auth'

const router = useRouter()

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  realName: '',
  phone: '',
  email: ''
})

const loading = ref(false)
const errorMsg = ref('')

async function handleRegister() {
  errorMsg.value = ''

  if (form.password !== form.confirmPassword) {
    errorMsg.value = '两次输入的密码不一致'
    return
  }

  if (form.password.length < 6) {
    errorMsg.value = '密码长度至少6位'
    return
  }

  loading.value = true

  try {
    await register({
      username: form.username,
      password: form.password,
      realName: form.realName || undefined,
      phone: form.phone || undefined,
      email: form.email || undefined
    })
    alert('注册成功，请登录')
    router.push('/login')
  } catch (err) {
    errorMsg.value = err.message || '注册失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-card {
  background: #fff;
  border-radius: 12px;
  padding: 40px;
  width: 440px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
}

.register-title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 8px;
}

.register-subtitle {
  text-align: center;
  color: #999;
  margin-bottom: 24px;
}

.register-btn {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  margin-top: 10px;
}

.error-msg {
  color: #f56c6c;
  font-size: 14px;
  margin-bottom: 10px;
}

.login-link {
  text-align: center;
  margin-top: 20px;
  color: #999;
}

.login-link a {
  color: #409eff;
}
</style>