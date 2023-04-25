<template>
  <ContentWrap>
    <el-container>
      <el-main>
        <div>
          <div style="font-size: 16px" class="title">
            <span>单号：{{ baseInfoData.data.serialNo }}</span>
            <span>商户经办人：{{ mainValue.formDataJson.merchantName }}</span>
            <span>商户电话：{{ mainValue.formDataJson.telNo }}</span>
          </div>
        </div>
        <div class="xinxi">利润提取信息</div>
        <div class="content-box">
          <el-row>
            <el-col :span="2" class="bg-yell">商户：</el-col>
            <el-col :span="4"> {{ mainValue.formDataJson.merchantName || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">手机号：</el-col>
            <el-col :span="4">{{ mainValue.formDataJson.telNo || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">提取金额： </el-col>
            <el-col :span="4">{{ mainValue.formDataJson.amount || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">剩余金额： </el-col>
            <el-col :span="4">{{ mainValue.formDataJson.balanceAmount || '暂无数据' }}</el-col>
          </el-row>
          <el-row>
            <el-col :span="2" class="bg-yell">收款账号： </el-col>
            <el-col :span="4">{{ mainValue.formDataJson.bankNo || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">开户行： </el-col>
            <el-col :span="16">{{ mainValue.formDataJson.bankName || '暂无数据' }}</el-col>
          </el-row>
          <el-row>
            <el-col :span="2" class="bg-yell" style="height: 100px">发票照片： </el-col>
            <el-col :span="22" style="height: 100px">
              <el-image
                v-for="item in mainValue.formDataJson.invoiceFiles"
                :key="item.fileId"
                style="width: 200px; height: 100px; margin-right: 10px"
                :src="item.fileUrl"
                :zoom-rate="1.2"
                :preview-src-list="idcardUrlArr"
                fit="cover"
                :initial-index="0"
              />
            </el-col>
          </el-row>
        </div>
        <div class="xinxi" style="margin-top: 16px">利润明细</div>
        <el-table :data="mainValue.formDataJson.profitDetails" style="width: 100%" height="250">
          <el-table-column prop="idx" label="序号" width="60" />
          <el-table-column prop="merchantName" label="商户名称" min-width="180" />
          <el-table-column prop="category" label="分类" min-width="180" />
          <el-table-column prop="telNo" label="商户手机号" min-width="180" />
          <el-table-column prop="carSalesAmount" label="收车金额（元）" min-width="180" />
          <el-table-column prop="vehicleReceiptAmount" label="卖车金额（元）" min-width="180" />
          <el-table-column prop="feeTotalAmount" label="各项费用合计（元）" min-width="180" />
        </el-table>
      </el-main>
    </el-container>
  </ContentWrap>
</template>
<script lang="ts" setup name="MerchantApprovalPending">
import { allSchemas } from '../toDoList/toDoList.data'
import { defineProps } from 'vue'
import { propTypes } from '@/utils/propTypes'

import { baseInfoData } from '@/views/workbench/basInfoValue'

const [] = useXTable({
  allSchemas: allSchemas
})

// 预览图片
const idcardUrlArr = computed(() => {
  return mainValue.formDataJson.invoiceFiles.map((item) => item.fileUrl)
})

// const emit = defineEmits(['cancelForm'])
const props = defineProps({
  // visible: propTypes.bool.def(false),
  type: propTypes.bool.def(undefined)
})
console.log(props)
// 详情
let mainValue = reactive({
  formDataJson: { idCardUrl: [{ url: '' }], businessLicense: [{ url: '' }] }
})

nextTick(() => {
  mainValue.formDataJson = { ...baseInfoData.data.variables.formDataJson.formMain.formDataJson }
})
</script>
<style lang="scss" scoped>
.title {
  > span {
    margin-right: 20px;
    font-weight: 600;
    color: #333333;
  }
  margin-bottom: 16px;
}
.xinxi {
  margin-bottom: 10px;
  font-size: 16px;
  font-weight: bold;
  padding-left: 10px;
  border-left: 4px solid #fa6400;
}
.header {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
}
.btn {
  text-align: right;
}
.content-box {
  color: #606266;
  .bg-yell {
    background: #f5f5f5;
    display: flex;
    text-align: right;
    justify-content: flex-end;
    padding-right: 5px;
  }
}
:deep(.el-main) {
  padding: 0;
}
:deep(.el-col) {
  border-right: 1px solid #eaeaea;
  border-bottom: 1px solid #eaeaea;
  display: flex;
  height: 40px;
  align-items: center;
}
.content-box .el-row:first-child {
  border-top: 1px solid #eaeaea;
}
.content-box .el-row .el-col:nth-child(1) {
  border-left: 1px solid #eaeaea;
}
.content-box .el-row .el-col:nth-child(even) {
  padding-left: 15px;
}
.image {
  padding: 16px 0 20px 18px;
  border-left: 1px solid #eaeaea;
  border-right: 1px solid #eaeaea;
  border-bottom: 1px solid #eaeaea;
}
</style>
