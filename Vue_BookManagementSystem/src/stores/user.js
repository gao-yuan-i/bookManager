import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getToken, setToken, removeToken, getRole, setRole, removeRole, getUsername, setUsername, removeUsername, clearAuth } from '@/utils/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken() || '')
  const role = ref(getRole() || '')
  const username = ref(getUsername() || '')

  // 登录后保存状态
  function setLoginInfo(newToken, newRole, newUsername) {
    token.value = newToken
    role.value = newRole
    username.value = newUsername
    setToken(newToken)
    setRole(newRole)
    setUsername(newUsername)
  }

  // 登出
  function logout() {
    token.value = ''
    role.value = ''
    username.value = ''
    clearAuth()
  }

  // 是否已登录
  const isLoggedIn = () => {
    return !!token.value
  }

  // 是否是管理员
  const isAdmin = () => {
    return role.value === 'ADMIN'
  }

  return {
    token,
    role,
    username,
    setLoginInfo,
    logout,
    isLoggedIn,
    isAdmin
  }
})