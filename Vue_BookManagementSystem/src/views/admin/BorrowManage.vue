<template>
  <div>
    <h2 style="margin-bottom: 20px;">📋 借阅管理</h2>

    <!-- 筛选 -->
    <div class="search-bar">
      <select v-model="status" class="form-input" style="min-width: 140px;" @change="fetchRecords">
        <option :value="undefined">全部状态</option>
        <option value="1">借阅中</option>
        <option value="2">已归还</option>
      </select>
      <button
        class="btn"
        :class="showOverdueOnly ? 'btn-danger' : 'btn-default'"
        @click="toggleOverdue"
      >
        ⚠️ 仅看逾期
      </button>
    </div>

    <!-- 借阅记录表格 -->
    <div class="card">
      <table class="table">
        <thead>
          <tr>
            <th>借阅人</th>
            <th>书名</th>
            <th>ISBN</th>
            <th>借阅日期</th>
            <th>应还日期</th>
            <th>归还日期</th>
            <th>状态</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in records" :key="record.id">
            <td>{{ record.realName }} ({{ record.username }})</td>
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
          </tr>
          <tr v-if="records.length === 0">
            <td colspan="7" style="text-align: center; color: #999; padding: 40px;">暂无记录</td>
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
import { getAdminBorrows, getOverdueBorrows } from '@/api/admin'
import { borrowStatusMap } from '@/utils/format'
import Pagination from '@/components/Pagination.vue'

const records = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const status = ref(undefined)
const showOverdueOnly = ref(false)

async function fetchRecords() {
  page.value = 1
  try {
    let res
    if (showOverdueOnly.value) {
      res = await getOverdueBorrows({ page: page.value, size: size.value })
    } else {
      res = await getAdminBorrows({
        page: page.value,
        size: size.value,
        status: status.value
      })
    }
    records.value = res.data.list
    total.value = res.data.total
  } catch (err) {
    console.error('获取借阅记录失败', err)
  }
}

function handlePageChange(newPage) {
  page.value = newPage
  fetchRecords()
}

function toggleOverdue() {
  showOverdueOnly.value = !showOverdueOnly.value
  status.value = undefined
  fetchRecords()
}

function getStatusClass(status) {
  return borrowStatusMap(status).class
}

function getStatusText(status) {
  return borrowStatusMap(status).text
}

onMounted(() => {
  fetchRecords()
})
</script>