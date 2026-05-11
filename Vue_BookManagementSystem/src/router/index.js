import { createRouter, createWebHistory } from 'vue-router'
import { getToken, getRole } from '@/utils/auth'

const routes = [
  // ==================== 公共页面 ====================
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录 - 图书管理系统' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册 - 图书管理系统' }
  },

  // ==================== 用户端 ====================
  {
    path: '/user',
    component: () => import('@/views/user/UserLayout.vue'),
    meta: { requiresAuth: true, role: 'USER' },
    children: [
      {
        path: '',
        redirect: '/user/home'
      },
      {
        path: 'home',
        name: 'UserHome',
        component: () => import('@/views/user/UserHome.vue'),
        meta: { title: '图书列表' }
      },
      {
        path: 'book/:id',
        name: 'BookDetail',
        component: () => import('@/views/user/BookDetail.vue'),
        meta: { title: '图书详情' }
      },
      {
        path: 'my-borrows',
        name: 'MyBorrows',
        component: () => import('@/views/user/MyBorrows.vue'),
        meta: { title: '我的借阅' }
      },
      {
        path: 'my-info',
        name: 'MyInfo',
        component: () => import('@/views/user/MyInfo.vue'),
        meta: { title: '个人信息' }
      }
    ]
  },

  // ==================== 管理员端 ====================
  {
    path: '/admin',
    component: () => import('@/views/admin/AdminLayout.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' },
    children: [
      {
        path: '',
        redirect: '/admin/home'
      },
      {
        path: 'home',
        name: 'AdminHome',
        component: () => import('@/views/admin/AdminHome.vue'),
        meta: { title: '管理首页' }
      },
      {
        path: 'books',
        name: 'BookManage',
        component: () => import('@/views/admin/BookManage.vue'),
        meta: { title: '图书管理' }
      },
      {
        path: 'books/add',
        name: 'BookAdd',
        component: () => import('@/views/admin/BookForm.vue'),
        meta: { title: '新增图书' }
      },
      {
        path: 'books/edit/:id',
        name: 'BookEdit',
        component: () => import('@/views/admin/BookForm.vue'),
        meta: { title: '编辑图书' }
      },
      {
        path: 'users',
        name: 'UserManage',
        component: () => import('@/views/admin/UserManage.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'users/add',
        name: 'UserAdd',
        component: () => import('@/views/admin/UserForm.vue'),
        meta: { title: '新增用户' }
      },
      {
        path: 'users/edit/:id',
        name: 'UserEdit',
        component: () => import('@/views/admin/UserForm.vue'),
        meta: { title: '编辑用户' }
      },
      {
        path: 'borrows',
        name: 'BorrowManage',
        component: () => import('@/views/admin/BorrowManage.vue'),
        meta: { title: '借阅管理' }
      }
    ]
  },

  // ==================== 默认重定向 ====================
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：检查登录状态和角色
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title || '图书管理系统'

  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)

  if (requiresAuth) {
    const token = getToken()
    const role = getRole()

    // 未登录
    if (!token) {
      return next('/login')
    }

    // 检查角色权限
    const requiredRole = to.meta.role
    if (requiredRole && role !== requiredRole) {
      // 角色不匹配，跳回对应首页
      if (role === 'ADMIN') {
        return next('/admin/home')
      } else {
        return next('/user/home')
      }
    }

    return next()
  }

  // 公开页面：如果已登录，直接跳到对应首页
  const token = getToken()
  const role = getRole()
  if (token && (to.path === '/login' || to.path === '/register' || to.path === '/')) {
    if (role === 'ADMIN') {
      return next('/admin/home')
    } else {
      return next('/user/home')
    }
  }

  next()
})

export default router