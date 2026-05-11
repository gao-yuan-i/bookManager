<template>
  <div class="container">
    <h2 style="margin-bottom: 20px;">📋 我的借阅记录</h2>

    <!-- 状态筛选 -->
    <div class="search-bar">
      <select v-model="status" class="form-input" style="min-width: 140px;" @change="fetchBorrows">
        <option :value="undefined">全部状态</option>
        <option value="1">借阅中</option>
        <option value="2">已归还</option>
        <option value="3">已逾期</option>
      </select>
    </div>

    <!-- 借阅记录表格 -->
    <div class="card">
      <table class="table">
        <thead>
          <tr>
            <th>书名</th>
            <th>ISBN</th>
            <th>借阅日期</th>
            <th>应还日期</th>
            <th>归还日期</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in records" :key="record.id">
            <td>{{ record.bookTitle }}</td>
            <td>{{ record.bookIsbn }}</td>
            <td>{{ record.borrowDate }}</td>
            <td>{{ record.dueDate }}</td>
            <td>{{ record.returnDate || '-' }}</td>
            <td>
              <span class="tag" :class="getStatusClass(record.status)">
                {{ getStatusText(record.status) }}
              </span>
            </td>
            <td>
              <button
                v-if="record.status === 1"
                class="btn btn-warning"
                @click="handleReturn(record)"
              >
                归还
              </button>
              <span v-else style="color: #999;">-</span>
            </td>
          </tr>
          <tr v-if="records.length === 0">
            <td colspan="7" style="text-align: center; color: #999; padding: 40px;">暂无借阅记录</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 分页 -->
    <Pagination :current="page" :size="size" :total="total" @change="handlePageChange" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyBorrows, returnBook } from '@/api/user'
import { borrowStatusMap } from '@/utils/format'
import Pagination from '@/components/Pagination.vue'

const records = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const status = ref(undefined)

async function fetchBorrows() {
  page.value = 1
  try {
    const res = await getMyBorrows({
      page: page.value,
      size: size.value,
      status: status.value
    })
    records.value = res.data.list
    total.value = res.data.total
  } catch (err) {
    console.error('获取借阅记录失败', err)
  }
}

function handlePageChange(newPage) {
  page.value = newPage
  fetchBorrows()
}

function getStatusClass(status) {
  return borrowStatusMap(status).class
}

function getStatusText(status) {
  return borrowStatusMap(status).text
}

async function handleReturn(record) {
  if (!confirm(`确定要归还《${record.bookTitle}》吗？`)) return
  try {
    await returnBook(record.id)
    alert('还书成功！')
    fetchBorrows()
  } catch (err) {
    alert(err.message || '还书失败')
  }
}

onMounted(() => {
  fetchBorrows()
})
</script>