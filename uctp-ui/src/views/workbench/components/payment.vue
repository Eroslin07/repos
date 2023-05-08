<template>
  <ContentWrap>
    <el-container>
      <el-main>
        <div>
          <div style="font-size: 16px" class="title">
            <span>单号：{{ baseInfoData.data.serialNo }}</span>
            <span>商户经办人：{{ baseInfoData.data.variables.startUserName }}</span>
            <span>商户电话：{{ baseInfoData.data.variables.startUserMobile }}</span>
          </div>
        </div>
        <div class="xinxi">收车款信息</div>
        <div class="title-bg-box">
          <icon icon="svg-icon:notice" class="notice"></icon>
          <div class="title-box">
            您向卖车方
            <span style="color: #63b0ff">{{ mainValue.formDataJson.bankName }}</span>
            的收款账号
            <span style="color: #63b0ff">{{ mainValue.formDataJson.bankNo }}</span>
            支付收车款
            <span style="color: #63b0ff">{{ mainValue.formDataJson.balanceAmount }}</span>
            元，因 {{}} 原因支付失败，请处理！
          </div>
        </div>
        <div class="content-box">
          <el-row>
            <el-col :span="2" class="bg-yell">姓名：</el-col>
            <el-col :span="4"> {{ mainValue.formDataJson.bankName || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">开户行：</el-col>
            <el-col :span="4">{{ mainValue.formDataJson.bankOfDeposit || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">银行账号： </el-col>
            <el-col :span="4">{{ mainValue.formDataJson.bankNo || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">收车款： </el-col>
            <el-col :span="4">{{
              mainValue.formDataJson.balanceAmount + '元' || '暂无数据'
            }}</el-col>
          </el-row>
        </div>
        <div class="xinxi" style="margin-top: 16px">合同信息</div>
        <!-- <div class="content">
          <el-row>
            <template
              v-for="item in baseInfoData.data.variables.formDataJson.formMain.formDataJson
                .carInvoiceInfoVO.contractList"
              :key="item.contractFileId"
            >
              <el-col :span="4" class="bg-yell">
                <span>{{ item.contractName }}</span>
              </el-col>
              <el-col :span="4">
                <span><button class="colr159" @click="viewContract(item)">查看</button></span>
              </el-col>
            </template>
          </el-row>
        </div> -->
      </el-main>
    </el-container>
    <AgreementFrame
      :visible="contractVisible"
      :src="contractFileUrl"
      @handle-cancel="handleCancel"
    />
  </ContentWrap>
</template>
<script lang="ts" setup name="MerchantApprovalPending">
import { AgreementFrame } from './index'
import { allSchemas } from '../toDoList/toDoList.data'
import { defineProps } from 'vue'
import { propTypes } from '@/utils/propTypes'

import { baseInfoData } from '@/views/workbench/basInfoValue'

const [] = useXTable({
  allSchemas: allSchemas
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

// 合同弹框
const contractVisible = ref(false)
const contractFileUrl = ref('')

// 查看合同
// const viewContract = (item: any) => {
//   contractFileUrl.value = item.contractFileUrl
//   contractVisible.value = true
// }

// 关闭合同弹框
const handleCancel = () => {
  contractVisible.value = false
}
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
.content {
  .el-row:first-child {
    border-top: none;
    .el-col {
      border-top: 1px solid #eaeaea;
    }
  }
}
.title-bg-box {
  margin: 14px 0 16px;
  width: 100%;
  overflow: hidden;
  height: 34px;
  background: rgba(250, 100, 0, 0.1);
  border-radius: 2px;
  padding-top: 8px;
  align-items: center;
  padding-left: 40px;
  position: relative;
  .notice {
    position: absolute;
    left: 20px;
    top: 8px;
  }
  .title-box {
    width: 200%;
    display: flex;
    color: #fa6400;
  }
}
</style>
