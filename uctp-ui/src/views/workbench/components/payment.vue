<template>
  <div class="reverse">
    <XModal
      v-model="dialogVisible"
      :title="dialogTitle"
      :fullscreen="true"
      :showFooter="false"
      @close="handleClose"
    >
      <!-- 发票单号 -->
      <div style="overflow: hidden; margin-bottom: 10px">
        <p style="float: left">单号：SCKZH202303220001</p>
        <div style="float: right">
          <!-- 操作按钮 -->
          <el-button type="danger" @click="handleClose">关闭</el-button>
          <el-button type="primary" :loading="actionLoading" @click="handlePay">重新支付</el-button>
          <el-button type="danger" @click="handleCancel" disabled>提交作废</el-button>
        </div>
      </div>
      <el-card class="box-card">
        <p style="text-align: center; font-weight: bold; font-size: 24px"
          >收车合同SCHT202303220001收车款支付失败待办</p
        >
        <p style="font-weight: bold; margin-bottom: 10px">收车款信息</p>
        <p
          >您向卖车方<span style="color: #63b0ff">陈某</span>的收款账号<span style="color: #63b0ff"
            >68XXXXXXXXXXXXXXXX</span
          >支付收车款<span style="color: #f57272">30,000.00</span
          >元，因XXXXX原因支付失败，请处理。</p
        >
        <div class="content">
          <p>付款信息</p>
          <p>姓名：陈某</p>
          <p>银行账号：<span style="color: #63b0ff">68XXXXXXXXXXXXXXXX</span></p>
          <p>收车款：<span style="color: #63b0ff">30,000.00元</span></p>
        </div>
        <p style="font-weight: bold; margin-bottom: 10px">合同信息</p>
        <p style="margin-bottom: 10px">XXX收车委托合同 <span style="color: #33a8db">查看</span></p>
        <p style="margin-bottom: 10px">XXX收车合同 <span style="color: #33a8db">查看</span></p>
      </el-card>
    </XModal>
  </div>
</template>

<script setup lang="ts" name="Reverse">
import { propTypes } from '@/utils/propTypes'

const emit = defineEmits(['cancelForm'])

const actionLoading = ref(false) // 遮罩层
const dialogTitle = ref('支付失败待办') // 弹出层标题

const props = defineProps({
  visible: propTypes.bool.def(false)
})

const dialogVisible = computed(() => {
  const obj = props.visible
  return obj
})

// 关闭操作
const handleClose = () => {
  emit('cancelForm')
}

// 重新支付
const handlePay = () => {
  actionLoading.value = true
}

// 提交作废
const handleCancel = () => {}
</script>

<style lang="scss" scoped>
p {
  font-size: 16px;
}

// .box-card {
//   height: 80vh;
// }

.content {
  border: 1px solid #000;
  padding: 10px;
  margin: 10px 0;
}
</style>
