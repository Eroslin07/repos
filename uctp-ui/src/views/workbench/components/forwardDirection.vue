<template>
  <div class="forwardDirection">
    <el-card>
      <div style="font-size: 16px" class="title">
        <span>单号：{{ baseInfoData.data.serialNo }}</span>
        <span>商户经办人：{{ baseInfoData.data.variables.startUserName }}</span>
        <span>商户电话：{{ baseInfoData.data.variables.startUserMobile }}</span>
      </div>
      <p style="font-weight: bold; margin-bottom: 10px">发票信息</p>
      <div class="title-bg-box">
        <icon icon="svg-icon:notice" class="notice"></icon>
        <div class="title-box">
          卖车合同
          <span style="color: #63b0ff">{{ baseInfoData.data.variables.contractCode }}</span>
          已经签约完成并已付款。现需要向买车方
          <span style="color: #63b0ff">{{
            baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
              .buyerName || '--'
          }}</span>
          开具二手车销售统一发票（正向）和增值税普通发票，开票内容如下：
        </div>
      </div>
      <div class="content">
        <el-row>
          <el-col :span="4" class="bg-blue">
            <span>买车单位/个人</span>
          </el-col>
          <el-col :span="8">
            <span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .buyerName
            }}</span>
          </el-col>
          <el-col :span="4" class="bg-blue"><span>单位代码/身份证号码</span></el-col>
          <el-col :span="8"
            ><span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .buyerIdCard
            }}</span></el-col
          >
        </el-row>
        <el-row>
          <el-col :span="4" class="bg-blue">
            <span>买车单位/个人住址</span>
          </el-col>
          <el-col :span="8">
            <span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .buyerAddress
            }}</span>
          </el-col>
          <el-col :span="4" class="bg-blue"><span>电话</span></el-col>
          <el-col :span="8"
            ><span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .buyerTel
            }}</span></el-col
          >
        </el-row>
        <el-row>
          <el-col :span="4" class="bg-blue">
            <span>卖方单位/个人</span>
          </el-col>
          <el-col :span="8">
            <span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .sellerName
            }}</span>
          </el-col>
          <el-col :span="4" class="bg-blue"><span>单位代码/身份证号码</span></el-col>
          <el-col :span="8"
            ><span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .sellerIdCard
            }}</span></el-col
          >
        </el-row>
        <el-row>
          <el-col :span="4" class="bg-blue">
            <span>卖车单位/个人住址</span>
          </el-col>
          <el-col :span="8">
            <span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .sellerAddress
            }}</span>
          </el-col>
          <el-col :span="4" class="bg-blue"><span>电话</span></el-col>
          <el-col :span="8"
            ><span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .sellerTel
            }}</span></el-col
          >
        </el-row>
        <el-row>
          <el-col :span="4" class="bg-blue">
            <span>车牌号</span>
          </el-col>
          <el-col :span="4">
            <span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .plateNum
            }}</span>
          </el-col>
          <el-col :span="4" class="bg-blue"><span>登记证书</span></el-col>
          <el-col :span="4"
            ><span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .certificateNo
            }}</span></el-col
          >
          <el-col :span="4" class="bg-blue"><span>车辆类型</span></el-col>
          <el-col :span="4"
            ><span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .carType
            }}</span></el-col
          >
        </el-row>
        <el-row>
          <el-col :span="4" class="bg-blue">
            <span>车架号/车辆识别代码</span>
          </el-col>
          <el-col :span="4">
            <span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO.vin
            }}</span>
          </el-col>
          <el-col :span="4" class="bg-blue"><span>厂牌型号</span></el-col>
          <el-col :span="4"
            ><span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .model
            }}</span></el-col
          >
          <el-col :span="4" class="bg-blue"
            ><span><span style="color: red">*</span>转入地车辆管理所名称</span></el-col
          >
          <el-col :span="4">
            <el-input
              v-if="!completedVisible"
              v-model="
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                  .sellTransManageName
              "
              class="car-input"
            ></el-input>
            <span v-else>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .transManageName
            }}</span>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="4" class="bg-blue">
            <span>车辆价款</span>
          </el-col>
          <el-col :span="20">
            <span>
              {{
                moneyFormat(
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                    .sellAmount
                )
              }}元
            </span>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="4" class="bg-blue">
            <span>二手车市场</span>
          </el-col>
          <el-col :span="8">
            <span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .marketName
            }}</span>
          </el-col>
          <el-col :span="4" class="bg-blue"
            ><div class="flex-column">
              <span>纳税人识别号</span>
              <span>地址</span>
            </div></el-col
          >
          <el-col :span="8"
            ><div class="flex-column flex-content">
              <span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                  .taxNum
              }}</span>
              <span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                  .marketAddress
              }}</span>
            </div></el-col
          >
        </el-row>
        <el-row>
          <el-col :span="4" class="bg-blue">
            <span>开户行账号</span>
          </el-col>
          <el-col :span="8">
            <span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .marketBankNum
            }}</span>
          </el-col>
          <el-col :span="4" class="bg-blue"><span>电话</span></el-col>
          <el-col :span="8"
            ><span>{{
              baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                .marketTel
            }}</span></el-col
          >
        </el-row>
      </div>
      <p style="font-weight: bold; margin: 10px 0">增值税通用发票</p>
      <div class="prompt">
        <div class="prompt-box">
          <el-row>
            <el-col :span="8">
              <span>个人开票:</span>
              <span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceDetailVO
                  .buyerName
              }}</span>
            </el-col>
            <el-col :span="8">
              <span>税率：</span>
              <span> 0.5%</span>
            </el-col>
            <el-col :span="8">
              <span>价税合计：</span>
              <span>取卖车款</span>
            </el-col>
          </el-row>
        </div>
        <div>
          <span
            >提示：开票的时候请先咨询对方开票抬头，如果个人开票确认下姓名，进行开票，如果公司抬头开票，请线下询问开票内容进行开票。</span
          >
        </div>
      </div>
      <p style="font-weight: bold; margin: 10px 0">合同信息</p>
      <div class="content-box">
        <el-row>
          <template
            v-for="item in baseInfoData.data.variables.formDataJson.formMain.formDataJson
              .contractList"
            :key="item.contractFileId"
          >
            <el-col :span="4" class="bg-blue">
              <span>{{ item.contractName ? item.contractName + ':' : '' }}</span>
            </el-col>
            <el-col :span="4">
              <span><button class="colr159" @click="viewContract(item)">查看</button></span>
            </el-col>
          </template>
        </el-row>
      </div>
    </el-card>
    <AgreementFrame
      :visible="contractVisible"
      :src="contractFileUrl"
      @handle-cancel="handleCancel"
    />
  </div>
</template>

<script setup lang="ts" name="forwardDirection">
import { AgreementFrame } from './index'
import { baseInfoData, completedVisible } from '@/views/workbench/basInfoValue'
// 合同弹框
const contractVisible = ref(false)
const contractFileUrl = ref('')
const message = useMessage() // 消息弹窗
// 查看合同
const viewContract = (item: any) => {
  if (item.contractFileUrl) {
    contractFileUrl.value = item.contractFileUrl
    contractVisible.value = true
  } else {
    message.error('改合同暂无预览')
  }
}
//千分位校验
const moneyFormat = (num) => {
  return Number(num)
    .toFixed(2)
    .replace(/(\d)(?=(\d{3})+\.)/g, '$1,')
}
// 关闭合同弹框
const handleCancel = () => {
  contractVisible.value = false
}
</script>

<style lang="scss" scoped>
.forwardDirection {
  .title {
    > span {
      margin-right: 20px;
      font-weight: 600;
      color: #333333;
    }
    margin-bottom: 16px;
  }
}
p {
  font-size: 16px;
}

.colr159 {
  color: #1592c9;
}
.flex-column {
  width: 100%;
  display: flex;
  flex-direction: column;
  span {
    width: 100%;
    text-align: right;
  }
  > span:nth-child(1) {
    border-bottom: 1px solid #eaeaea;
  }
}
:deep(.el-col) {
  box-sizing: border-box;
  border-right: 1px solid #eaeaea;
  border-bottom: 1px solid #eaeaea;
  display: flex;
  height: 40px;
  align-items: center;
  span {
    padding-left: 15px;
  }
}
.flex-content {
  padding: 0;
  span {
    height: 20px;
    text-align: left;
  }
}
.content .bg-blue {
  background: #f5f5f5;
  display: flex;
  justify-content: flex-end;
  span {
    padding-right: 14px;
  }
}
.content .el-row:first-child {
  border-top: 1px solid #eaeaea;
}
.content .el-row .el-col:nth-child(1) {
  border-left: 1px solid #eaeaea;
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
.car-input {
  height: 100%;
}
.prompt {
  border: 1px solid #eaeaea;
  padding: 20px;
  > div:first-child {
    margin-bottom: 20px;
  }
  > div:last-child {
    color: #f59a23;
  }
  .prompt-box {
    :deep(.el-col) {
      border: none;
    }
  }
}
.content-box {
  .el-row {
    border-left: 1px solid #eaeaea;
  }
  .el-row:first-child {
    border-top: none;
    .el-col {
      border-top: 1px solid #eaeaea;
    }
  }
  .bg-blue {
    background: #f5f5f5;
  }
}
</style>
