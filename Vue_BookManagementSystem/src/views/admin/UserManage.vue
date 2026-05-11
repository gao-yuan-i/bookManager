<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h2>👥 用户管理</h2>
      <router-link to="/admin/users/add" class="btn btn-primary">➕ 新增用户</router-link>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <input v-model="keyword" type="text" class="form-input" placeholder="用户名/姓名/手机号" @keyup.enter="search" />
      <button class="btn btn-primary" @click="search">搜索</button>
    </div>

    <!-- 用户表格 -->
    <div class="card">
      <table class="table">
        <thead>
          <tr>
            <th>用户名</th>
            <th>真实姓名</th>
            <th>手机号</th>
            <th>邮箱</th>
            <th>角色</th>
            <th>借阅数</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.username }}</td>
            <td>{{ user.realName || '-' }}</td>
            <td>{{ user.phone || '-' }}</td>
            <td>{{ user.email || '-' }}</td>
            <td>
              <span class="tag" :class="user.role === 'ADMIN' ? 'tag-warning' : 'tag-primary'">
                {{ user.role === 'ADMIN' ? '管理员' : '用户' }}
              </span>
            </td>
            <td>{{ user.borrowCount }} / {{ user.maxBorrow }}</td>
            <td>
              <span class="tag" :class="user.status === 1 ? 'tag-success' : 'tag-danger'">
                {{ user.status === 1 ? '启用' : '禁用' }}
              </span>
            </td>
            <td>
              <router-link :to="'/admin/users/edit/' + user.id" class="btn btn-warning" style="margin-right: 4px;">编辑</router-link>
              <button
                class="btn"
                :class="user.status === 1 ? 'btn-danger' : 'btn-success'"
                style="margin-right: 4px;"
                @click="handleToggleStatus(user)"
              >
                {{ user.status === 1 ? '禁用' : '启用' }}
              </button>
              <button class="btn btn-danger" @click="handleDelete(user)">删除</button>
            </td>
          </tr>
          <tr v-if="users.length === 0">
            <td colspan="8" style="text-align: center; color: #999; padding: 40px;">暂无用户数据</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 分页 -->
    <Pagination :current="page" :size="size" :total="total" @change="handlePageChange" />

    <!-- 删除确认弹窗 -->
    <ConfirmDialog
      :visible="showDeleteDialog"
      title="确认删除"
      :content="`确定要删除用户「${selectedUser?.username}」吗？`"
      type="danger"
      @confirm="confirmDelete"
      @cancel="showDeleteDialog = false"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminUsers, updateUserStatus, deleteUser } from '@/api/admin'
import Pagination from '@/components/Pagination.vue'
import ConfirmDialog from '@/components/ConfirmDialog.vue'

const users = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const keyword = ref('')

const showDeleteDialog = ref(false)
const selectedUser = ref(null)

async function fetchUsers() {
  try {
    const res = await getAdminUsers({
      page: page.value,
      size: size.value,
      keyword: keyword.value || undefined
    })
    users.value = res.data.list
    total.value = res.data.total
  } catch (err) {
    console.error('获取用户列表失败', err)
  }
}

function search() {
  page.value = 1
  fetchUsers()
}

function handlePageChange(newPage) {
  page.value = newPage
  fetchUsers()
}

async function handleToggleStatus(user) {
  const newStatus = user.status === 1 ? 0 : 1
  const action = newStatus === 0 ? '禁用' : '启用'
  if (!confirm(`确定要${action}用户「${user.username}」吗？`)) return
  try {
    await updateUserStatus(user.id, newStatus)
    alert(`${action}成功`)
    fetchUsers()
  } catch (err) {
    alert(err.message || '操作失败')
  }
}

function handleDelete(user) {
  selectedUser.value = user
  showDeleteDialog.value = true
}

async function confirmDelete() {
  try {
    await deleteUser(selectedUser.value.id)
    alert('删除成功')
    showDeleteDialog.value = false
    fetchUsers()
  } catch (err) {
    alert(err.message || '删除失败')
  }
}

onMounted(() => {
  fetchUsers()
})
</script>