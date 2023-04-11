<template>
  <ContentWrap>
    <Drawer :visible="drawerVisible" :status="status" @handle-close-drawer="handleCloseDrawer" />
    <!-- 列表 -->
    <XTable @register="registerTable">
      <!-- 操作：流程跟踪 -->
      <template #toolbar_buttons>
        <XButton
          type="primary"
          preIcon="ep:zoom-in"
          title="流程跟踪"
          v-hasPermi="['system:post:create']"
          @click="handleProcessTrace()"
        />
        <!-- 操作：导出 -->
        <XButton
          type="primary"
          preIcon="ep:download"
          :title="t('action.export')"
          v-hasPermi="['system:post:create']"
          @click="handleExport()"
        />
      </template>
      <template #application_default="{ row }">
        <div class="application" @click="handleApplication(row)">{{ row.name }}</div>
      </template>
    </XTable>
  </ContentWrap>
</template>
<script setup lang="ts" name="Finished">
import { allSchemas } from './finished.data'
import * as RoleApi from '@/api/system/role'
import { Drawer } from '@/components/Drawer'
// 列表相关的变量
const [registerTable] = useXTable({
  allSchemas: allSchemas,
  getListApi: RoleApi.getRolePageApi
})
const { t } = useI18n() // 国际化
// const { push } = useRouter() // 路由

// const dialogVisible = ref(false)

const status = ref('')

// 抽屉
const drawerVisible = ref(false)
const handleCloseDrawer = () => {
  drawerVisible.value = false
}

// 流程跟踪
const handleProcessTrace = () => {
  console.log('流程跟踪')
}

//导出
const handleExport = () => {
  console.log('导出')
}

// 点击申请单号
const handleApplication = (row) => {
  console.log(row)
  // GZSH SCJG SCKP SCKZH MCHT MCKP LRTQ
  status.value = 'GZSH'
  drawerVisible.value = true
}

// 关闭弹框
// const closeDialog = () => {
//   dialogVisible.value = false
//   status.value = ''
// }
</script>

<style scoped>
.application {
  color: #51b5e0;
  cursor: pointer;
}
</style>
