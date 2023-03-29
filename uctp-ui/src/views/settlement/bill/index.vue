<template>
  <ContentWrap>
    <!-- 列表 -->
    <XTable @register="registerTable">
      <!-- 操作：新增 -->
      <template #toolbar_buttons>
        <XButton
          type="primary"
          preIcon="ep:zoom-in"
          :title="t('action.add')"
          v-hasPermi="['system:post:create']"
          @click="handleCreate()"
        />
        <XButton
          type="primary"
          preIcon="ep:zoom-in"
          title="下载"
          v-hasPermi="['system:post:create']"
          @click="handleUpdate()"
        />
      </template>
    </XTable>
  </ContentWrap>
</template>

<script setup lang="ts" name="Bill">
// 业务相关的 import
import { allSchemas } from './bill.data'

const { t } = useI18n() // 国际化
const { push } = useRouter() // 路由

// 列表相关的变量
const [registerTable] = useXTable({
  allSchemas: allSchemas
})

// 新增操作
const handleCreate = () => {
  push({
    name: 'bpmFormEditor'
  })
}

// 修改操作
const handleUpdate = async () => {
  await push({
    name: 'bpmFormEditor'
  })
}
</script>

<style scoped></style>
