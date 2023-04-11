<template>
  <div>
    <!-- 商户主账号审批待办 -->
    <MerchantApprovalPending v-if="status == 'GZSH'" :type="'need'" />
    <!-- 收车价格超公允值待办 -->
    <CollectCarPending v-if="status == 'SCJG'" :type="'need'" />
    <!-- 反向二手车统一发票待办 -->
    <Reverse v-if="status == 'SCKP'" :type="'need'" />
    <!-- 支付失败待办 -->
    <Payment v-if="status == 'SCKZH'" :type="'need'" />
    <!-- 正向二手车统一发票和增值税发票待办 -->
    <ForwardDirection v-if="status == 'MCKP'" :type="'need'" />
    <!-- 卖车价格超公允值待办 -->
    <SellCarPending v-if="status == 'MCHT'" :type="'need'" />
    <!-- 利润提取代办 -->
    <Profit v-if="status == 'LRTQ'" :type="'need'" />
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
  Profit
} from '@/views/workbench/components'
import { defineProps } from 'vue'
const props = defineProps({
  status: {
    type: String,
    default: ''
  }
})

console.log(props, 'prop')
</script>
