<template>
  <div>
    <h2 style="margin-bottom: 24px;">📊 管理首页</h2>

    <div class="stats-grid">
      <div class="stat-card" style="background: #ecf5ff;">
        <div class="stat-number">{{ stats.bookCount }}</div>
        <div class="stat-label">图书总数</div>
      </div>
      <div class="stat-card" style="background: #f0f9eb;">
        <div class="stat-number">{{ stats.userCount }}</div>
        <div class="stat-label">用户总数</div>
      </div>
      <div class="stat-card" style="background: #fdf6ec;">
        <div class="stat-number">{{ stats.borrowingCount }}</div>
        <div class="stat-label">借阅中</div>
      </div>
      <div class="stat-card" style="background: #fef0f0;">
        <div class="stat-number">{{ stats.overdueCount }}</div>
        <div class="stat-label">逾期未还</div>
      </div>
    </div>

    <div class="shortcut-grid">
      <router-link to="/admin/books" class="shortcut-card">
        📖 图书管理 →<br><small>增删改查图书信息</small>
      </router-link>
      <router-link to="/admin/books/add" class="shortcut-card">
        ➕ 新增图书 →<br><small>添加新书入库</small>
      </router-link>
      <router-link to="/admin/users" class="shortcut-card">
        👥 用户管理 →<br><small>管理用户账号</small>
      </router-link>
      <router-link to="/admin/borrows" class="shortcut-card">
        📋 借阅管理 →<br><small>查看借阅与逾期记录</small>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminBooks } from '@/api/admin'
import { getAdminUsers } from '@/api/admin'
import { getAdminBorrows, getOverdueBorrows } from '@/api/admin'

const stats = ref({
  bookCount: 0,
  userCount: 0,
  borrowingCount: 0,
  overdueCount: 0
})

async function fetchStats() {
  try {
    const [booksRes, usersRes, borrowsRes, overdueRes] = await Promise.all([
      getAdminBooks({ page: 1, size: 1 }),
      getAdminUsers({ page: 1, size: 1 }),
      getAdminBorrows({ page: 1, size: 1, status: 1 }),
      getOverdueBorrows({ page: 1, size: 1 })
    ])
    stats.value.bookCount = booksRes.data.total
    stats.value.userCount = usersRes.data.total
    stats.value.borrowingCount = borrowsRes.data.total
    stats.value.overdueCount = overdueRes.data.total
  } catch (err) {
    console.error('获取统计数据失败', err)
  }
}

onMounted(() => {
  fetchStats()
})
</script>

<style scoped>
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}
.stat-card {
  padding: 24px;
  border-radius: 8px;
  text-align: center;
}
.stat-number {
  font-size: 36px;
  font-weight: 700;
}
.stat-label {
  color: #666;
  margin-top: 8px;
}
.shortcut-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}
.shortcut-card {
  background: #fff;
  padding: 28px;
  border-radius: 8px;
  text-align: center;
  font-size: 18px;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: transform 0.2s;
}
.shortcut-card:hover {
  transform: translateY(-2px);
}
.shortcut-card small {
  display: block;
  font-weight: 400;
  color: #999;
  margin-top: 8px;
  font-size: 13px;
}
</style>