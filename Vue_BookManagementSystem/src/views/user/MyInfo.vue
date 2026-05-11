<template>
  <div class="container">
    <h2 style="margin-bottom: 20px;">👤 个人信息</h2>

    <div class="card" v-if="userInfo">
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">用户名</span>
          <span class="info-value">{{ userInfo.username }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">真实姓名</span>
          <span class="info-value">{{ userInfo.realName || '-' }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">手机号码</span>
          <span class="info-value">{{ userInfo.phone || '-' }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">邮箱</span>
          <span class="info-value">{{ userInfo.email || '-' }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">角色</span>
          <span class="info-value">
            <span class="tag tag-primary">{{ userInfo.role === 'ADMIN' ? '管理员' : '普通用户' }}</span>
          </span>
        </div>
        <div class="info-item">
          <span class="info-label">当前借阅</span>
          <span class="info-value">{{ userInfo.borrowCount }} / {{ userInfo.maxBorrow }} 本</span>
        </div>
        <div class="info-item">
          <span class="info-label">注册时间</span>
          <span class="info-value">{{ userInfo.createTime }}</span>
        </div>
      </div>

      <button class="btn btn-primary" style="margin-top: 20px;" @click="showEditInfo = true">修改信息</button>
      <button class="btn btn-warning" style="margin-top: 20px; margin-left: 10px;" @click="showEditPassword = true">修改密码</button>
    </div>

    <!-- 修改信息弹窗 -->
    <ConfirmDialog
      v-if="showEditInfo"
      :visible="showEditInfo"
      title="修改个人信息"
      :type="'primary'"
      @cancel="showEditInfo = false"
      @confirm="showEditInfo = false"
    >
      <!-- 自定义内容放弹窗里 -->
    </ConfirmDialog>

    <!-- 修改信息弹窗 -->
    <div v-if="showEditInfo" class="dialog-overlay" @click.self="showEditInfo = false">
      <div class="dialog-box">
        <div class="dialog-title">修改个人信息</div>
        <div class="form-group">
          <label class="form-label">真实姓名</label>
          <input v-model="editForm.realName" type="text" class="form-input" />
        </div>
        <div class="form-group">
          <label class="form-label">手机号码</label>
          <input v-model="editForm.phone" type="text" class="form-input" />
        </div>
        <div class="form-group">
          <label class="form-label">邮箱</label>
          <input v-model="editForm.email" type="email" class="form-input" />
        </div>
        <div class="dialog-footer">
          <button class="btn btn-default" @click="showEditInfo = false">取消</button>
          <button class="btn btn-primary" @click="handleUpdateInfo">保存</button>
        </div>
      </div>
    </div>

    <!-- 修改密码弹窗 -->
    <div v-if="showEditPassword" class="dialog-overlay" @click.self="showEditPassword = false">
      <div class="dialog-box">
        <div class="dialog-title">修改密码</div>
        <div class="form-group">
          <label class="form-label">原密码</label>
          <input v-model="passwordForm.oldPassword" type="password" class="form-input" />
        </div>
        <div class="form-group">
          <label class="form-label">新密码</label>
          <input v-model="passwordForm.newPassword" type="password" class="form-input" />
        </div>
        <div class="dialog-footer">
          <button class="btn btn-default" @click="showEditPassword = false">取消</button>
          <button class="btn btn-primary" @click="handleUpdatePassword">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getMyInfo, updateMyInfo, updatePassword } from '@/api/user'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const userInfo = ref(null)
const showEditInfo = ref(false)
const showEditPassword = ref(false)

const editForm = reactive({
  realName: '',
  phone: '',
  email: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: ''
})

async function fetchMyInfo() {
  try {
    const res = await getMyInfo()
    userInfo.value = res.data
    editForm.realName = res.data.realName || ''
    editForm.phone = res.data.phone || ''
    editForm.email = res.data.email || ''
  } catch (err) {
    console.error('获取个人信息失败', err)
  }
}

async function handleUpdateInfo() {
  try {
    await updateMyInfo({
      realName: editForm.realName || undefined,
      phone: editForm.phone || undefined,
      email: editForm.email || undefined
    })
    alert('修改成功')
    showEditInfo.value = false
    fetchMyInfo()
  } catch (err) {
    alert(err.message || '修改失败')
  }
}

async function handleUpdatePassword() {
  try {
    await updatePassword(passwordForm.oldPassword, passwordForm.newPassword)
    alert('密码修改成功，请重新登录')
    userStore.logout()
    window.location.href = '/login'
  } catch (err) {
    alert(err.message || '修改密码失败')
  }
}

onMounted(() => {
  fetchMyInfo()
})
</script>

<style scoped>
.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}
.info-item {
  padding: 8px 0;
}
.info-label {
  color: #999;
  margin-right: 8px;
}
.info-value {
  font-weight: 500;
}
</style>