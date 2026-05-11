<template>
  <div class="pagination" v-if="total > 0">
    <button :disabled="current === 1" @click="changePage(current - 1)">上一页</button>

    <button
      v-for="page in visiblePages"
      :key="page"
      :class="{ active: page === current }"
      @click="changePage(page)"
    >
      {{ page }}
    </button>

    <button :disabled="current === totalPages" @click="changePage(current + 1)">下一页</button>

    <span class="pagination-info">共 {{ total }} 条，{{ totalPages }} 页</span>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  current: {
    type: Number,
    default: 1
  },
  size: {
    type: Number,
    default: 10
  },
  total: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['change'])

const totalPages = computed(() => {
  return Math.ceil(props.total / props.size)
})

const visiblePages = computed(() => {
  const pages = []
  const maxVisible = 5
  let start = Math.max(1, props.current - Math.floor(maxVisible / 2))
  let end = Math.min(totalPages.value, start + maxVisible - 1)

  if (end - start + 1 < maxVisible) {
    start = Math.max(1, end - maxVisible + 1)
  }

  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

function changePage(page) {
  if (page >= 1 && page <= totalPages.value) {
    emit('change', page)
  }
}
</script>