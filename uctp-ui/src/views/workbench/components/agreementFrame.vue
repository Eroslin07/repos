<template>
  <ContentWrap>
    <vxe-modal
      v-model="contractVisible"
      title="合同"
      width="60%"
      :showFooter="false"
      @close="closeDialog"
      style="height: 100%"
      :zIndex="3000"
    >
      <iframe
        src="https://element-plus.org/zh-CN/component/form.html#%E5%85%B8%E5%9E%8B%E8%A1%A8%E5%8D%95"
        frameborder="0"
        style="width: 100%; height: 90vh"
      ></iframe>
    </vxe-modal>
  </ContentWrap>
</template>

<script setup lang="ts" name="AgreementFrame">
import { propTypes } from '@/utils/propTypes'
const emit = defineEmits(['handleCancel'])

const props = defineProps({
  visible: propTypes.bool.def(false)
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
