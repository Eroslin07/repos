<template>
  <div class="baseinfo-container">
    <!-- 商户主账号审批待办 -->
    <MerchantApprovalPending v-if="status == 'ZHSQ'" />
    <!-- 收车价格超公允值待办 -->
    <CollectCarPending v-if="status == 'SGYZ'" />
    <!-- 反向二手车统一发票待办 -->
    <Reverse v-if="status == 'SCKP'" />
    <!-- 车辆是否已过户至平台 -->
    <VehicleReceiptTransfer v-if="status == 'SCGH'" />
    <!-- 支付失败待办 -->
    <Payment v-if="status == 'SKZH'" />
    <!-- 正向二手车统一发票和增值税发票待办 -->
    <ForwardDirection v-if="status == 'MCKP'" />
    <!-- 卖车价格超公允值待办 -->
    <SellCarPending v-if="status == 'MGYZ'" />
    <!-- 利润提取代办 -->
    <Profit v-if="status == 'LRTX'" />
    <!-- 车辆是否已过户至买家 -->
    <VehicleTransferBuyer v-if="status == 'MCGH'" />
    <!-- 保证金充值代办 -->
    <BondRecharge v-if="status == 'ZJCZ'" />
    <!-- 保证金提现代办 -->
    <BondWithdrawal v-if="status == 'ZJTX'" />
    <!-- 收车支付流程 -->
    <CollPayment v-if="status == 'SCZF'" />
    <!-- 卖车支付流程 -->
    <SellPayment v-if="status == 'SMCSK'" />
  </div>
</template>
<script setup lang="ts">
import {
  MerchantApprovalPending,
  CollectCarPending,
  Reverse,
  Payment,
  ForwardDirection,
  SellCarPending,
  Profit,
  VehicleReceiptTransfer,
  VehicleTransferBuyer,
  CollPayment,
  SellPayment,
  BondRecharge,
  BondWithdrawal
} from '@/views/workbench/components'
import { defineProps } from 'vue'
import { baseInfoData } from '@/views/workbench/basInfoValue'
console.log(baseInfoData.data, 'baseInfoData')

const props = defineProps({
  status: {
    type: String,
    default: ''
  }
})

console.log(props, 'prop')
</script>

<style lang="scss" scoped>
.baseinfo-container {
  height: 100%;
  :deep(.el-card) {
    height: 100%;
  }
}
</style>
