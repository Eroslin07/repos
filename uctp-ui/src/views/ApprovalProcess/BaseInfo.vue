<template>
  <div class="baseinfo-container">
    <!-- 商户主账号审批待办 -->
    <MerchantApprovalPending v-if="status == 'ZHSQ'" :type="'need'" />
    <!-- 收车价格超公允值待办 -->
    <CollectCarPending v-if="status == 'SGYZ'" :type="'need'" />
    <!-- 反向二手车统一发票待办 -->
    <Reverse v-if="status == 'SCKP'" :type="'need'" />
    <!-- 车辆是否已过户至平台 -->
    <VehicleReceiptTransfer v-if="status == 'SCGH'" :type="'need'" />
    <!-- 支付失败待办 -->
    <Payment v-if="status == 'SCKZH'" :type="'need'" />
    <!-- 正向二手车统一发票和增值税发票待办 -->
    <ForwardDirection v-if="status == 'MCKP'" :type="'need'" />
    <!-- 卖车价格超公允值待办 -->
    <SellCarPending v-if="status == 'MGYZ'" :type="'need'" />
    <!-- 利润提取代办 -->
    <Profit v-if="status == 'LRTQ'" :type="'need'" />
    <!-- 车辆是否已过户至买家 -->
    <VehicleTransferBuyer v-if="status == 'MCGH'" :type="need" />
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
  VehicleTransferBuyer
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
