import request from './request'

// ==================== 图书管理 ====================

/**
 * 查询图书列表
 */
export function getAdminBooks(params) {
  return request({
    url: '/admin/books',
    method: 'get',
    params
  })
}

/**
 * 新增图书
 */
export function addBook(data) {
  return request({
    url: '/admin/books',
    method: 'post',
    data
  })
}

/**
 * 修改图书
 */
export function updateBook(id, data) {
  return request({
    url: `/admin/books/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除图书
 */
export function deleteBook(id) {
  return request({
    url: `/admin/books/${id}`,
    method: 'delete'
  })
}

// ==================== 用户管理 ====================

/**
 * 查询用户列表
 */
export function getAdminUsers(params) {
  return request({
    url: '/admin/users',
    method: 'get',
    params
  })
}

/**
 * 新增用户
 */
export function addUser(data) {
  return request({
    url: '/admin/users',
    method: 'post',
    data
  })
}

/**
 * 修改用户
 */
export function updateUser(id, data) {
  return request({
    url: `/admin/users/${id}`,
    method: 'put',
    data
  })
}

/**
 * 启用/禁用用户
 */
export function updateUserStatus(id, status) {
  return request({
    url: `/admin/users/${id}/status`,
    method: 'put',
    data: { status }
  })
}

/**
 * 删除用户
 */
export function deleteUser(id) {
  return request({
    url: `/admin/users/${id}`,
    method: 'delete'
  })
}

// ==================== 借阅管理 ====================

/**
 * 查询所有借阅记录
 */
export function getAdminBorrows(params) {
  return request({
    url: '/admin/borrows',
    method: 'get',
    params
  })
}

/**
 * 查询逾期记录
 */
export function getOverdueBorrows(params) {
  return request({
    url: '/admin/borrows/overdue',
    method: 'get',
    params
  })
}