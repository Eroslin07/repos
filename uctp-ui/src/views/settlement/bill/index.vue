<template>
  <ContentWrap>
    <!-- 列表 -->
    <XTable @register="registerTable">
      <!-- 操作：新增 -->
      <template #toolbar_buttons>
        <XButton
          type="primary"
          preIcon="ep:zoom-in"
          title="开具"
          v-hasPermi="['settlement:bill:opener']"
          @click="handleCreate()"
        />
        <XButton
          type="warning"
          preIcon="ep:download"
          title="下载"
          @click="exportList('发票数据.xls')"
        />
      </template>
    </XTable>
  </ContentWrap>
</template>
<script setup lang="ts" name="Bill">
import { allSchemas } from './bill.data'
import * as InvoiceApi from '@/api/settlement/invoice'

// const { t } = useI18n() // 国际化
// 列表相关的变量
const [registerTable, { exportList }] = useXTable({
  allSchemas: allSchemas,
  getListApi: InvoiceApi.getInvoice,
  exportListApi: InvoiceApi.exportTack
})

// 新增操作
const handleCreate = () => {}
</script>
<style scoped>
.card {
  width: 100%;
  max-height: 400px;
  overflow-y: scroll;
}
</style>
开户行
