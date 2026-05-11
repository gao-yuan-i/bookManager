<template>
  <div class="container">
    <button class="btn btn-default" @click="$router.back()" style="margin-bottom: 16px;">← 返回</button>

    <div class="card" v-if="book">
      <h2>{{ book.title }}</h2>
      <div class="detail-grid">
        <div class="detail-item">
          <strong>ISBN：</strong>{{ book.isbn }}
        </div>
        <div class="detail-item">
          <strong>作者：</strong>{{ book.author }}
        </div>
        <div class="detail-item">
          <strong>出版社：</strong>{{ book.publisher }}
        </div>
        <div class="detail-item">
          <strong>出版日期：</strong>{{ book.publishDate }}
        </div>
        <div class="detail-item">
          <strong>分类：</strong><span class="tag tag-primary">{{ book.category }}</span>
        </div>
        <div class="detail-item">
          <strong>可借数量：</strong>{{ book.availableQuantity }} / {{ book.totalQuantity }}
        </div>
        <div class="detail-item">
          <strong>馆藏位置：</strong>{{ book.location }}
        </div>
        <div class="detail-item">
          <strong>入库时间：</strong>{{ book.createTime }}
        </div>
      </div>

      <div class="detail-description" v-if="book.description">
        <h3>内容简介</h3>
        <p>{{ book.description }}</p>
      </div>

      <div style="margin-top: 20px;">
        <button
          class="btn btn-success"
          style="padding: 10px 30px; font-size: 16px;"
          :disabled="!book.canBorrow"
          @click="handleBorrow"
        >
          {{ book.canBorrow ? '立即借阅' : '已全部借出' }}
        </button>
      </div>
    </div>

    <div v-else style="text-align: center; padding: 60px; color: #999;">加载中...</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getUserBookDetail, borrowBook } from '@/api/user'

const route = useRoute()
const router = useRouter()
const book = ref(null)

async function fetchDetail() {
  try {
    const res = await getUserBookDetail(route.params.id)
    book.value = res.data
  } catch (err) {
    alert(err.message || '获取图书详情失败')
    router.back()
  }
}

async function handleBorrow() {
  if (!confirm(`确定要借阅《${book.value.title}》吗？借阅期限为1个月。`)) return
  try {
    await borrowBook(book.value.id)
    alert('借书成功！')
    fetchDetail()
  } catch (err) {
    alert(err.message || '借书失败')
  }
}

onMounted(() => {
  fetchDetail()
})
</script>

<style scoped>
.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin: 20px 0;
}
.detail-item {
  padding: 8px 0;
}
.detail-description {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}
.detail-description h3 {
  margin-bottom: 10px;
}
.detail-description p {
  line-height: 1.8;
  color: #666;
}
</style>