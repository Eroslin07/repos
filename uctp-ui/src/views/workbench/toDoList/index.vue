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
        <XButton type="primary" preIcon="ep:zoom-in" title="反向" @click="handleReverse()" />
        <XButton type="primary" preIcon="ep:zoom-in" title="支付" @click="handlePayment()" />
        <XButton type="primary" preIcon="ep:zoom-in" title="正向" @click="handleForward()" />
      </template>
    </XTable>
    <MerchantApprovalPending
      v-if="dialogVisible"
      :dialogVisible="dialogVisible"
      @close-dialog="closeDialog"
    />
    <CollectCarPending
      v-if="carVisible"
      :carVisible="carVisible"
      @close-car-dialog="closeCarDialog"
    />
    <Reverse v-if="reverseVisible" :visible="reverseVisible" @cancel-form="cancelReverse" />
    <Payment v-if="paymentVisible" :visible="paymentVisible" @cancel-form="cancelPayment" />
    <ForwardDirection
      v-if="forwardVisible"
      :visible="forwardVisible"
      @cancel-forward="cancelForward"
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
  ForwardDirection
} from '../components'
// 列表相关的变量
const [registerTable] = useXTable({
  allSchemas: allSchemas
})
const { t } = useI18n() // 国际化
// const { push } = useRouter() // 路由

const dialogVisible = ref(false)
const carVisible = ref(false)
const reverseVisible = ref(false)
const paymentVisible = ref(false)
const forwardVisible = ref(false)
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
  carVisible.value = true
}

const closeCarDialog = () => {
  carVisible.value = false
}

// 反向弹框
const handleReverse = () => {
  reverseVisible.value = true
}

// 支付失败弹框
const handlePayment = () => {
  paymentVisible.value = true
}

// 关闭反向弹框
const cancelReverse = () => {
  reverseVisible.value = false
}

// 关闭支付失败弹框
const cancelPayment = () => {
  paymentVisible.value = false
}

// 正向弹框
const handleForward = () => {
  forwardVisible.value = true
}

// 关闭正向弹框
const cancelForward = () => {
  forwardVisible.value = false
}
</script>
