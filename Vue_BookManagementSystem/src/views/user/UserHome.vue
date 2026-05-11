<template>
  <div class="container">
    <h2 style="margin-bottom: 20px;">📖 图书列表</h2>

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
            <th>出版社</th>
            <th>分类</th>
            <th>可借数量</th>
            <th>位置</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="book in books" :key="book.id">
            <td>{{ book.isbn }}</td>
            <td>{{ book.title }}</td>
            <td>{{ book.author }}</td>
            <td>{{ book.publisher }}</td>
            <td><span class="tag tag-primary">{{ book.category }}</span></td>
            <td>{{ book.availableQuantity }} / {{ book.totalQuantity }}</td>
            <td>{{ book.location }}</td>
            <td>
              <router-link :to="'/user/book/' + book.id" class="btn btn-primary" style="margin-right: 6px;">详情</router-link>
              <button class="btn btn-success" :disabled="!book.canBorrow" @click="handleBorrow(book)">
                {{ book.canBorrow ? '借书' : '已借完' }}
              </button>
            </td>
          </tr>
          <tr v-if="books.length === 0">
            <td colspan="8" style="text-align: center; color: #999; padding: 40px;">暂无图书数据</td>
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
import { useRouter } from 'vue-router'
import { getUserBooks, borrowBook } from '@/api/user'
import Pagination from '@/components/Pagination.vue'

const router = useRouter()

const books = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const keyword = ref('')
const category = ref('')

async function fetchBooks() {
  try {
    const res = await getUserBooks({
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

async function handleBorrow(book) {
  if (!confirm(`确定要借阅《${book.title}》吗？借阅期限为1个月。`)) return
  try {
    await borrowBook(book.id)
    alert('借书成功！')
    fetchBooks()
  } catch (err) {
    alert(err.message || '借书失败')
  }
}

onMounted(() => {
  fetchBooks()
})
</script>