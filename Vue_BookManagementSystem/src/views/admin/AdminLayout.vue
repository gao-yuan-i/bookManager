<template>
  <div>
    <!-- 顶部导航 -->
    <nav class="navbar">
      <div class="navbar-brand">📚 图书管理系统（管理员）</div>
      <div class="navbar-right">
        <span>🔧 {{ userStore.username }}</span>
        <button class="btn btn-default" @click="$router.push('/user/home')">切换用户端</button>
        <button class="btn btn-danger" @click="handleLogout">退出登录</button>
      </div>
    </nav>

    <!-- 侧边栏 + 内容 -->
    <div class="layout">
      <aside class="sidebar">
        <router-link to="/admin/home" class="sidebar-item" active-class="active">📊 管理首页</router-link>
        <router-link to="/admin/books" class="sidebar-item" active-class="active">📖 图书管理</router-link>
        <router-link to="/admin/users" class="sidebar-item" active-class="active">👥 用户管理</router-link>
        <router-link to="/admin/borrows" class="sidebar-item" active-class="active">📋 借阅管理</router-link>
      </aside>
      <main class="main-content">
        <router-view />
      </main>
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