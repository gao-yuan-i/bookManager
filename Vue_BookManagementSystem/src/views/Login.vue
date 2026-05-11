<template>
  <div class="login-page">
    <div class="login-card">
      <h1 class="login-title">📚 图书管理系统</h1>
      <p class="login-subtitle">欢迎回来，请登录您的账号</p>

      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label class="form-label">用户名</label>
          <input
            v-model="form.username"
            type="text"
            class="form-input"
            placeholder="请输入用户名"
            required
          />
        </div>

        <div class="form-group">
          <label class="form-label">密码</label>
          <input
            v-model="form.password"
            type="password"
            class="form-input"
            placeholder="请输入密码"
            required
          />
        </div>

        <div class="form-group">
          <label class="form-label">登录方式</label>
          <div class="role-select">
            <label class="role-option" :class="{ active: form.role === 'USER' }">
              <input type="radio" v-model="form.role" value="USER" />
              <span>👤 用户登录</span>
            </label>
            <label class="role-option" :class="{ active: form.role === 'ADMIN' }">
              <input type="radio" v-model="form.role" value="ADMIN" />
              <span>🔧 管理员登录</span>
            </label>
          </div>
        </div>

        <p v-if="errorMsg" class="error-msg">{{ errorMsg }}</p>

        <button type="submit" class="btn btn-primary login-btn" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>

      <p class="register-link">
        还没有账号？
        <router-link to="/register">立即注册</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/auth'
import { useUserStore } from '@/stores/user'
import { jwtDecode } from 'jwt-decode'

const router = useRouter()
const userStore = useUserStore()

const form = reactive({
  username: '',
  password: '',
  role: 'USER'
})

const loading = ref(false)
const errorMsg = ref('')

async function handleLogin() {
  errorMsg.value = ''
  loading.value = true

  try {
    const res = await login(form.username, form.password)
    const token = res.data.token

    // 解析 token 获取角色
    const decoded = jwtDecode(token)
    const role = decoded.role

    // 检查角色是否匹配
    if (form.role !== role) {
      errorMsg.value = `该账号不是${form.role === 'ADMIN' ? '管理员' : '普通用户'}账号`
      loading.value = false
      return
    }

    // 保存登录状态
    userStore.setLoginInfo(token, role, decoded.username)

    // 跳转
    if (role === 'ADMIN') {
      router.push('/admin/home')
    } else {
      router.push('/user/home')
    }
  } catch (err) {
    errorMsg.value = err.message || '登录失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  background: #fff;
  border-radius: 12px;
  padding: 40px;
  width: 420px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
}

.login-title {
  text-align: center;
  font-size: 28px;
  margin-bottom: 8px;
}

.login-subtitle {
  text-align: center;
  color: #999;
  margin-bottom: 30px;
}

.role-select {
  display: flex;
  gap: 12px;
}

.role-option {
  flex: 1;
  padding: 12px;
  border: 2px solid #dcdfe6;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.role-option input {
  display: none;
}

.role-option.active {
  border-color: #409eff;
  background: #ecf5ff;
}

.login-btn {
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

.register-link {
  text-align: center;
  margin-top: 20px;
  color: #999;
}

.register-link a {
  color: #409eff;
}
</style>