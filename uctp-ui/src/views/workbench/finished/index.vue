<template>
  <ContentWrap>
    <!-- 列表 -->
    <XTable @register="registerTable">
      <!-- 操作：审批 -->
      /* eslint-disable */
      <template #toolbar_buttons>
        <!-- <XButton
          type="primary"
          preIcon="ep:cherry"
          title="审批"
          v-hasPermi="['system:post:create']"
          @click="handleApproval()"
        /> -->
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
        <!-- 操作：查看详情 -->
        <XButton
          type="primary"
          preIcon="ep:download"
          title="商户详情"
          v-hasPermi="['system:post:create']"
          @click="handleDetail()"
        />
        <XButton
          type="primary"
          preIcon="ep:download"
          title="收车详情"
          v-hasPermi="['system:post:create']"
          @click="handleCarDetail()"
        />
      </template>
    </XTable>
    <MerchantApprovalPending :dialogVisible="dialogVisible" @close-dialog="closeDialog" />
    <CollectCarPending :carVisible="carVisible" @close-car-dialog="closeCarDialog" />
  </ContentWrap>
</template>
<script setup lang="ts" name="ToDoList">
import { allSchemas } from './finished.data'
import MerchantApprovalPending from '../components/MerchantApprovalPending.vue'
import CollectCarPending from '../components/CollectCarPending.vue'
// 列表相关的变量
const [registerTable] = useXTable({
  allSchemas: allSchemas
})
const { t } = useI18n() // 国际化
// const { push } = useRouter() // 路由

const dialogVisible = ref(false)
const carVisible = ref(false)
// 审批
// const handleApproval = () => {
//   console.log('审批')
// }

// 流程跟踪
const handleProcessTrace = () => {
  console.log('流程跟踪')
}

//导出
const handleExport = () => {
  console.log('导出')
}

// 查看详情
const handleDetail = () => {
  console.log('查看详情')
  dialogVisible.value = true
}

// 关闭弹框
const closeDialog = () => {
  dialogVisible.value = false
}

// 收车详情
const handleCarDetail = () => {
  console.log(2222)
  carVisible.value = true
}

const closeCarDialog = () => {
  carVisible.value = false
}
</script>
