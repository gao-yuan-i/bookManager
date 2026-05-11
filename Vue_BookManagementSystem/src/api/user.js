import request from './request'

/**
 * 查询图书列表（分页 + 搜索）
 */
export function getUserBooks(params) {
  return request({
    url: '/user/books',
    method: 'get',
    params
  })
}

/**
 * 查看图书详情
 */
export function getUserBookDetail(id) {
  return request({
    url: `/user/books/${id}`,
    method: 'get'
  })
}

/**
 * 借书
 */
export function borrowBook(bookId) {
  return request({
    url: `/user/borrow/${bookId}`,
    method: 'post'
  })
}

/**
 * 还书
 */
export function returnBook(recordId) {
  return request({
    url: `/user/return/${recordId}`,
    method: 'post'
  })
}

/**
 * 查看我的借阅记录
 */
export function getMyBorrows(params) {
  return request({
    url: '/user/my-borrows',
    method: 'get',
    params
  })
}

/**
 * 查看个人信息
 */
export function getMyInfo() {
  return request({
    url: '/user/my-info',
    method: 'get'
  })
}

/**
 * 修改个人信息
 */
export function updateMyInfo(data) {
  return request({
    url: '/user/my-info',
    method: 'put',
    data
  })
}

/**
 * 修改密码
 */
export function updatePassword(oldPassword, newPassword) {
  return request({
    url: '/user/password',
    method: 'put',
    data: { oldPassword, newPassword }
  })
}