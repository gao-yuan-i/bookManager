const TOKEN_KEY = 'book_manager_token'
const ROLE_KEY = 'book_manager_role'
const USERNAME_KEY = 'book_manager_username'

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token)
}

export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
}

export function getRole() {
  return localStorage.getItem(ROLE_KEY)
}

export function setRole(role) {
  localStorage.setItem(ROLE_KEY, role)
}

export function removeRole() {
  localStorage.removeItem(ROLE_KEY)
}

export function getUsername() {
  return localStorage.getItem(USERNAME_KEY)
}

export function setUsername(username) {
  localStorage.setItem(USERNAME_KEY, username)
}

export function removeUsername() {
  localStorage.removeItem(USERNAME_KEY)
}

export function clearAuth() {
  removeToken()
  removeRole()
  removeUsername()
}