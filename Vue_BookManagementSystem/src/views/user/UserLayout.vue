<template>
  <div>
    <!-- 顶部导航栏 -->
    <nav class="navbar">
      <div class="navbar-brand">
        <router-link to="/user/home">📚 图书管理系统</router-link>
      </div>
      <div class="navbar-right">
        <span>👤 {{ userStore.username }}</span>
        <router-link to="/user/my-borrows" class="btn btn-default">我的借阅</router-link>
        <router-link to="/user/my-info" class="btn btn-default">个人信息</router-link>
        <button class="btn btn-danger" @click="handleLogout">退出登录</button>
      </div>
    </nav>

    <!-- 内容区域 -->
    <div class="main-content">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>