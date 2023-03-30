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
          v-hasPermi="['settlement:bill:export']"
          @click="exportList('用户数据.xls')"
        />
      </template>
      <template #actionbtns_default="{ row }">
        <!-- 操作：删除 -->
        <XTextButton
          preIcon="ep:delete"
          :title="t('action.del')"
          v-hasPermi="['settlement:bill:delete']"
          @click="deleteData(row.id)"
        />
      </template>
    </XTable>
  </ContentWrap>
</template>
<script setup lang="ts" name="Bill">
import { allSchemas } from './bill.data'
import * as RoleApi from '@/api/system/role'
import * as UserApi from '@/api/system/user'

const { t } = useI18n() // 国际化
// 列表相关的变量
const [registerTable, { exportList }] = useXTable({
  allSchemas: allSchemas,
  getListApi: RoleApi.getRolePageApi,
  deleteApi: RoleApi.deleteRoleApi,
  exportListApi: UserApi.exportUserApi
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
