<template>
  <ContentWrap>
    <!-- 列表 -->
    <XTable @register="registerTable">
      <!-- 操作：新增 -->
      <template #toolbar_buttons>
        <!-- 操作：导出 -->
        <XButton
          type="warning"
          title="下载"
          preIcon="ep:download"
          @click="exportList('合同档案.xls')"
        />
      </template>
    </XTable>
  </ContentWrap>
</template>
<script setup lang="ts" name="Record">
import { allSchemas } from './record.data'
import * as ContractRecordApi from '@/api/contract/record'

// const { t } = useI18n() // 国际化
// 列表相关的变量
const [registerTable, { exportList }] = useXTable({
  allSchemas: allSchemas,
  getListApi: ContractRecordApi.getRecordPageApi,
  exportListApi: ContractRecordApi.exportRecordApi
})
</script>
