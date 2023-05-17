<script setup lang="ts">
import { useAppStore } from '@/store/modules/app'
import { useDesign } from '@/hooks/web/useDesign'

const { getPrefixCls } = useDesign()

const prefixCls = getPrefixCls('footer')

const appStore = useAppStore()

const title = computed(() => appStore.getTitle)

const year = computed(() => {
  let date = new Date()
  var year = date.getFullYear()
  return year
})
</script>

<template>
  <div
    :class="prefixCls"
    class="text-center text-[var(--el-text-color-placeholder)] bg-[var(--app-contnet-bg-color)] h-[var(--app-footer-height)] leading-[var(--app-footer-height)] dark:bg-[var(--el-bg-color)]"
  >
    <p style="font-size: 14px"
      >Copyright ©{{ year }}-{{ title }}
      <a href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank"
        >蜀ICP备2023011585号</a
      ></p
    >
  </div>
</template>
