<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h2>{{ isEdit ? '✏️ 编辑图书' : '➕ 新增图书' }}</h2>
      <button class="btn btn-default" @click="$router.back()">← 返回</button>
    </div>

    <div class="card">
      <form @submit.prevent="handleSubmit">
        <div class="form-row">
          <div class="form-group">
            <label class="form-label">书名 *</label>
            <input v-model="form.title" type="text" class="form-input" required />
          </div>
          <div class="form-group">
            <label class="form-label">ISBN</label>
            <input v-model="form.isbn" type="text" class="form-input" />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">作者</label>
            <input v-model="form.author" type="text" class="form-input" />
          </div>
          <div class="form-group">
            <label class="form-label">出版社</label>
            <input v-model="form.publisher" type="text" class="form-input" />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">出版日期</label>
            <input v-model="form.publishDate" type="date" class="form-input" />
          </div>
          <div class="form-group">
            <label class="form-label">分类</label>
            <select v-model="form.category" class="form-input">
              <option value="">请选择分类</option>
              <option value="计算机">计算机</option>
              <option value="文学">文学</option>
              <option value="历史">历史</option>
              <option value="科幻">科幻</option>
              <option value="心理学">心理学</option>
            </select>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">总数量 *</label>
            <input v-model.number="form.totalQuantity" type="number" min="1" class="form-input" required />
          </div>
          <div class="form-group">
            <label class="form-label">馆藏位置</label>
            <input v-model="form.location" type="text" class="form-input" />
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">封面图片URL</label>
          <input v-model="form.coverImage" type="text" class="form-input" />
        </div>

        <div class="form-group">
          <label class="form-label">内容简介</label>
          <textarea v-model="form.description" class="form-input" rows="4"></textarea>
        </div>

        <div style="margin-top: 20px;">
          <button type="submit" class="btn btn-primary" style="padding: 10px 30px;">
            {{ isEdit ? '保存修改' : '新增图书' }}
          </button>
          <button type="button" class="btn btn-default" style="margin-left: 10px; padding: 10px 30px;" @click="$router.back()">取消</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { addBook, updateBook, getAdminBooks } from '@/api/admin'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  isbn: '',
  title: '',
  author: '',
  publisher: '',
  publishDate: '',
  category: '',
  description: '',
  coverImage: '',
  totalQuantity: 1,
  location: ''
})

async function fetchBookDetail() {
  try {
    // 用搜索接口查单个
    const res = await getAdminBooks({ page: 1, size: 1, keyword: route.params.id })
    // 简单处理，需要后端单独给个详情接口，这里先跳过了
    // 如果有专门的图书详情接口更好，目前用列表不一定能拿到
  } catch (err) {
    console.error('获取图书详情失败', err)
  }
}

async function handleSubmit() {
  try {
    const data = { ...form }
    if (!data.isbn) delete data.isbn
    if (!data.description) data.description = undefined

    if (isEdit.value) {
      await updateBook(route.params.id, data)
      alert('修改成功')
    } else {
      await addBook(data)
      alert('新增成功')
    }
    router.back()
  } catch (err) {
    alert(err.message || '操作失败')
  }
}

onMounted(() => {
  if (isEdit.value) {
    // 简单实现：需要后端单独给/admin/books/{id}接口返回数据
    // 目前用路由params传id即可
  }
})
</script>

<style scoped>
.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 20px;
}
textarea.form-input {
  resize: vertical;
  font-family: inherit;
}
</style>