<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h2>📖 图书管理</h2>
      <router-link to="/admin/books/add" class="btn btn-primary">➕ 新增图书</router-link>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <input v-model="keyword" type="text" class="form-input" placeholder="书名/作者/ISBN" @keyup.enter="search" />
      <select v-model="category" class="form-input" style="min-width: 120px;">
        <option value="">全部分类</option>
        <option value="计算机">计算机</option>
        <option value="文学">文学</option>
        <option value="历史">历史</option>
        <option value="科幻">科幻</option>
        <option value="心理学">心理学</option>
      </select>
      <button class="btn btn-primary" @click="search">搜索</button>
    </div>

    <!-- 图书表格 -->
    <div class="card">
      <table class="table">
        <thead>
          <tr>
            <th>ISBN</th>
            <th>书名</th>
            <th>作者</th>
            <th>分类</th>
            <th>可借/总数</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="book in books" :key="book.id">
            <td>{{ book.isbn }}</td>
            <td>{{ book.title }}</td>
            <td>{{ book.author }}</td>
            <td><span class="tag tag-primary">{{ book.category }}</span></td>
            <td>{{ book.availableQuantity }} / {{ book.totalQuantity }}</td>
            <td>
              <span class="tag" :class="book.status === 1 ? 'tag-success' : 'tag-danger'">
                {{ book.status === 1 ? '上架' : '下架' }}
              </span>
            </td>
            <td>
              <router-link :to="'/admin/books/edit/' + book.id" class="btn btn-warning" style="margin-right: 6px;">编辑</router-link>
              <button class="btn btn-danger" @click="handleDelete(book)">删除</button>
            </td>
          </tr>
          <tr v-if="books.length === 0">
            <td colspan="7" style="text-align: center; color: #999; padding: 40px;">暂无图书数据</td>
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
      :content="`确定要删除《${selectedBook?.title}》吗？删除后该书将下架。`"
      type="danger"
      @confirm="confirmDelete"
      @cancel="showDeleteDialog = false"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminBooks, deleteBook } from '@/api/admin'
import Pagination from '@/components/Pagination.vue'
import ConfirmDialog from '@/components/ConfirmDialog.vue'

const books = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const keyword = ref('')
const category = ref('')

const showDeleteDialog = ref(false)
const selectedBook = ref(null)

async function fetchBooks() {
  try {
    const res = await getAdminBooks({
      page: page.value,
      size: size.value,
      keyword: keyword.value || undefined,
      category: category.value || undefined
    })
    books.value = res.data.list
    total.value = res.data.total
  } catch (err) {
    console.error('获取图书列表失败', err)
  }
}

function search() {
  page.value = 1
  fetchBooks()
}

function handlePageChange(newPage) {
  page.value = newPage
  fetchBooks()
}

function handleDelete(book) {
  selectedBook.value = book
  showDeleteDialog.value = true
}

async function confirmDelete() {
  try {
    await deleteBook(selectedBook.value.id)
    alert('删除成功')
    showDeleteDialog.value = false
    fetchBooks()
  } catch (err) {
    alert(err.message || '删除失败')
  }
}

onMounted(() => {
  fetchBooks()
})
</script>