<template>
  <ContentWrap>
    <!-- 列表 -->
    <XTable @register="registerTable">
      <!-- 操作：审批 -->
      <template #toolbar_buttons>
        <XButton
          type="primary"
          preIcon="ep:cherry"
          title="审批"
          v-hasPermi="['system:post:create']"
          @click="handleApproval()"
        />
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
    <!-- 商户主账号审批待办 -->
    <MerchantApprovalPending
      v-if="status == 'GZSH'"
      :dialogVisible="dialogVisible"
      :type="'need'"
      @close-dialog="closeDialog"
    />
    <!-- 收车价格超公允值待办 -->
    <CollectCarPending
      v-if="status == 'SCJG'"
      :carVisible="dialogVisible"
      :type="'need'"
      @close-car-dialog="closeDialog"
    />
    <!-- 反向二手车统一发票待办 -->
    <Reverse
      v-if="status == 'SCKP'"
      :visible="dialogVisible"
      :type="'need'"
      @cancel-form="closeDialog"
    />
    <!-- 支付失败待办 -->
    <Payment
      v-if="status == 'SCKZH'"
      :visible="dialogVisible"
      :type="'need'"
      @cancel-form="closeDialog"
    />
    <!-- 卖车价格超公允值待办 -->
    <SellCarPending
      v-if="status == 'MCHT'"
      :visible="dialogVisible"
      :type="'need'"
      @cancle-sell-car="closeDialog"
    />
    <!-- 正向二手车统一发票和增值税发票待办 -->
    <ForwardDirection
      v-if="status == 'MCKP'"
      :visible="dialogVisible"
      :type="'need'"
      @cancel-forward="closeDialog"
    />
    <!-- 利润提取代办 -->
    <Profit
      v-if="status == 'LRTQ'"
      :visible="dialogVisible"
      :type="'need'"
      @cancel-form="closeDialog"
    />
  </ContentWrap>
</template>
<script setup lang="ts" name="ToDoList">
import { allSchemas } from './toDoList.data'
import {
  MerchantApprovalPending,
  CollectCarPending,
  Reverse,
  Payment,
  ForwardDirection,
  SellCarPending,
  Profit
} from '../components'

import * as RoleApi from '@/api/system/role'
// 列表相关的变量
const [registerTable] = useXTable({
  allSchemas: allSchemas,
  getListApi: RoleApi.getRolePageApi
})
const { t } = useI18n() // 国际化
// const { push } = useRouter() // 路由

const dialogVisible = ref(false)

const status = ref('')
// 审批
const handleApproval = () => {
  console.log('审批')
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
  dialogVisible.value = true
}

// 关闭弹框
const closeDialog = () => {
  dialogVisible.value = false
  status.value = ''
}
</script>

<style scoped>
.application {
  color: #51b5e0;
  cursor: pointer;
}
</style>
