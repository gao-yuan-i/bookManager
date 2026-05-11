<template>
  <div class="search-bar">
    <input
      v-model="searchKeyword"
      type="text"
      class="form-input"
      :placeholder="placeholder"
      @keyup.enter="handleSearch"
    />
    <slot></slot>
    <button class="btn btn-primary" @click="handleSearch">搜索</button>
    <button v-if="searchKeyword" class="btn btn-default" @click="handleClear">清空</button>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  modelValue: { type: String, default: '' },
  placeholder: { type: String, default: '请输入关键词搜索' }
})

const emit = defineEmits(['update:modelValue', 'search'])

const searchKeyword = ref(props.modelValue)

watch(() => props.modelValue, (val) => {
  searchKeyword.value = val
})

watch(searchKeyword, (val) => {
  emit('update:modelValue', val)
})

function handleSearch() {
  emit('search', searchKeyword.value)
}

function handleClear() {
  searchKeyword.value = ''
  emit('search', '')
}
</script>

<style scoped>
.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  align-items: center;
}
.search-bar .form-input {
  flex: 1;
  max-width: 400px;
}
</style>