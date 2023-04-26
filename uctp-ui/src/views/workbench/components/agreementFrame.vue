<template>
  <vxe-modal
    v-model="contractVisible"
    title="合同"
    width="60%"
    :showFooter="false"
    @close="closeDialog"
    :zIndex="3000"
  >
    <iframe :src="src" frameborder="0" style="width: 100%; height: 90vh"></iframe>
  </vxe-modal>
</template>

<script setup lang="ts" name="AgreementFrame">
const emit = defineEmits(['handleCancel'])

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  src: {
    type: String,
    default: ''
  }
})

const contractVisible = computed(() => {
  return props.visible
})

// 关闭弹框
const closeDialog = () => {
  emit('handleCancel')
}
</script>

<style></style>
