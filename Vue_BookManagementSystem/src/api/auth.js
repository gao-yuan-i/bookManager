import request from './request'

/**
 * 用户登录
 */
export function login(username, password) {
  return request({
    url: '/login',
    method: 'post',
    data: { username, password }
  })
}

/**
 * 用户注册
 */
export function register(data) {
  return request({
    url: '/register',
    method: 'post',
    data
  })
}