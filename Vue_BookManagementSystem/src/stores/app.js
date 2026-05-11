import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  const loading = ref(false)

  function showLoading() {
    loading.value = true
  }

  function hideLoading() {
    loading.value = false
  }

  return {
    loading,
    showLoading,
    hideLoading
  }
})