<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h2>{{ isEdit ? '✏️ 编辑用户' : '➕ 新增用户' }}</h2>
      <button class="btn btn-default" @click="$router.back()">← 返回</button>
    </div>

    <div class="card">
      <form @submit.prevent="handleSubmit">
        <div class="form-row">
          <div class="form-group">
            <label class="form-label">用户名 *</label>
            <input v-model="form.username" type="text" class="form-input" :disabled="isEdit" required />
          </div>
          <div class="form-group">
            <label class="form-label">密码 {{ isEdit ? '(留空不修改)' : '*' }}</label>
            <input v-model="form.password" type="password" class="form-input" :required="!isEdit" />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">真实姓名</label>
            <input v-model="form.realName" type="text" class="form-input" />
          </div>
          <div class="form-group">
            <label class="form-label">角色</label>
            <select v-model="form.role" class="form-input">
              <option value="USER">普通用户</option>
              <option value="ADMIN">管理员</option>
            </select>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">手机号码</label>
            <input v-model="form.phone" type="text" class="form-input" />
          </div>
          <div class="form-group">
            <label class="form-label">邮箱</label>
            <input v-model="form.email" type="email" class="form-input" />
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">最大借阅数量</label>
          <input v-model.number="form.maxBorrow" type="number" min="1" class="form-input" style="max-width: 200px;" />
        </div>

        <div style="margin-top: 20px;">
          <button type="submit" class="btn btn-primary" style="padding: 10px 30px;">
            {{ isEdit ? '保存修改' : '新增用户' }}
          </button>
          <button type="button" class="btn btn-default" style="margin-left: 10px; padding: 10px 30px;" @click="$router.back()">取消</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { addUser, updateUser } from '@/api/admin'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  username: '',
  password: '',
  realName: '',
  phone: '',
  email: '',
  role: 'USER',
  maxBorrow: 5
})

async function handleSubmit() {
  try {
    const data = {
      username: form.username,
      realName: form.realName || undefined,
      phone: form.phone || undefined,
      email: form.email || undefined,
      role: form.role,
      maxBorrow: form.maxBorrow
    }
    if (form.password) {
      data.password = form.password
    }

    if (isEdit.value) {
      await updateUser(route.params.id, data)
      alert('修改成功')
    } else {
      await addUser(data)
      alert('新增成功')
    }
    router.back()
  } catch (err) {
    alert(err.message || '操作失败')
  }
}
</script>

<style scoped>
.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 20px;
}
</style>