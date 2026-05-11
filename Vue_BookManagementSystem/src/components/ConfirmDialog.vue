<template>
  <div v-if="visible" class="dialog-overlay" @click.self="handleCancel">
    <div class="dialog-box">
      <div class="dialog-title">{{ title }}</div>
      <div class="dialog-content">{{ content }}</div>
      <div class="dialog-footer">
        <button class="btn btn-default" @click="handleCancel">取消</button>
        <button class="btn" :class="confirmClass" @click="handleConfirm">确定</button>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  visible: { type: Boolean, default: false },
  title: { type: String, default: '提示' },
  content: { type: String, default: '确定执行此操作吗？' },
  type: { type: String, default: 'primary' } // primary / danger
})

const emit = defineEmits(['confirm', 'cancel'])

const confirmClass = computed(() => {
  return props.type === 'danger' ? 'btn-danger' : 'btn-primary'
})

function handleConfirm() {
  emit('confirm')
}

function handleCancel() {
  emit('cancel')
}
</script>

<script>
import { computed } from 'vue'
export default { name: 'ConfirmDialog' }
</script>