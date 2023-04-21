<template>
  <ContentWrap style="height: 100%">
    <!-- <XModal
      v-model="visible"
      :title="dialogTitle"
      :fullscreen="true"
      :showFooter="false"
      @close="closeDialog"
    > -->
    <el-container>
      <!-- <el-header class="header">商户张三卖车价格超公允值审批待办</el-header> -->
      <el-main>
        <div style="font-size: 16px" class="title">
          <span>单号：{{ baseInfoData.data.serialNo }}</span>
          <span>商户经办人：{{ '张三' }}</span>
          <span>商户电话：{{ '13333333333' }}</span>
        </div>
        <!-- <div class="btn"> -->
        <!-- <el-button type="danger" @click="closeDialog">关闭</el-button> -->
        <!-- <el-button type="primary" v-if="type == 'need'" @click="passBtn">通过</el-button>
          <el-button v-if="type == 'need'" @click="returnBtn">退回</el-button> -->
        <!-- </div> -->
        <!-- <el-card class="content-box"> -->
        <div class="content-box">
          <h3 style="font-weight: bold; color: #333333">车辆基础信息</h3>
          <div>
            <el-row>
              <el-col :span="2" class="bg-yell">行驶证：</el-col>
              <el-col :span="4">
                <div>
                  <span
                    >{{
                      baseInfoData.data.variables.formDataJson.formMain.formDataJson.fileB.length
                    }}张</span
                  >
                  <el-image
                    v-for="url in baseInfoData.data.variables.formDataJson.formMain.formDataJson
                      .fileB"
                    :key="url.id"
                    style="width: 34px; height: 34px; margin: 0 5px; vertical-align: top"
                    :src="url.url"
                    fit="fill"
                    :zoom-rate="1.2"
                    :preview-src-list="fileB"
                  />
                </div>
              </el-col>
              <el-col :span="2" class="bg-yell">机动车登记证书：</el-col>
              <el-col :span="4">
                <div>
                  <span
                    >{{
                      baseInfoData.data.variables.formDataJson.formMain.formDataJson.fileC.length
                    }}张</span
                  >
                  <el-image
                    v-for="url in baseInfoData.data.variables.formDataJson.formMain.formDataJson
                      .fileC"
                    :key="url.id"
                    style="width: 34px; height: 34px; margin: 0 5px; vertical-align: top"
                    :src="url.url"
                    fit="fill"
                    :zoom-rate="1.2"
                    :preview-src-list="fileC"
                  />
                </div>
              </el-col>
              <el-col :span="2" class="bg-yell">车辆图片：</el-col>
              <el-col :span="10">
                <div>
                  <span
                    >{{
                      baseInfoData.data.variables.formDataJson.formMain.formDataJson.fileA.length
                    }}张</span
                  >
                  <el-image
                    v-for="url in baseInfoData.data.variables.formDataJson.formMain.formDataJson
                      .fileA"
                    :key="url.id"
                    style="width: 34px; height: 34px; margin: 0 5px; vertical-align: top"
                    :src="url.url"
                    fit="fill"
                    :zoom-rate="1.2"
                    :preview-src-list="fileA"
                  />
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="2" class="bg-yell">车架号：</el-col>
              <el-col :span="4">
                <div>
                  {{ baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.vin }}
                </div>
              </el-col>
              <el-col :span="2" class="bg-yell">发动机编号：</el-col>
              <el-col :span="4">
                <div>
                  {{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.engineNum
                  }}</div
                >
              </el-col>
              <el-col :span="2" class="bg-yell">首次登记日期：</el-col>
              <el-col :span="4">
                <div>
                  {{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                      .firstRegistDate
                  }}</div
                >
              </el-col>
              <el-col :span="2" class="bg-yell">车牌号：</el-col>
              <el-col :span="4">
                <div>
                  {{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.plateNum
                  }}</div
                >
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="2" class="bg-yell">使用性质：</el-col>
              <el-col :span="4">
                <div>
                  {{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                      .natureOfOperat
                  }}</div
                >
              </el-col>
              <el-col :span="2" class="bg-yell">品牌/车型：</el-col>
              <el-col :span="4">
                <el-tooltip
                  class="box-item"
                  effect="dark"
                  :content="
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.model
                  "
                  placement="top-start"
                >
                  <div class="carInfo">
                    {{
                      baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.model
                    }}</div
                  >
                </el-tooltip>
              </el-col>
              <el-col :span="2" class="bg-yell">车辆类型：</el-col>
              <el-col :span="4">
                <div>
                  {{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.carType
                  }}</div
                >
              </el-col>
              <el-col :span="2" class="bg-yell">品牌型号：</el-col>
              <el-col :span="4">
                <div>
                  {{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.brandType
                  }}</div
                >
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="2" class="bg-yell">登记证号：</el-col>
              <el-col :span="4">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .certificateNo
                }}</div>
              </el-col>
              <el-col :span="2" class="bg-yell">颜色：</el-col>
              <el-col :span="4">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .colour
                }}</div>
              </el-col>
              <el-col :span="2" class="bg-yell">里程数：</el-col>
              <el-col :span="4">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .mileage
                }}</div>
              </el-col>
              <el-col :span="2" class="bg-yell">使用年限至：</el-col>
              <el-col :span="4">
                <div>{{
                  formatDate(
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.scrapDate
                  )
                }}</div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="2" class="bg-yell">年检签证有效期至：</el-col>
              <el-col :span="4">
                <div>{{
                  formatDate(
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo
                      .annualInspectionDate
                  )
                }}</div>
              </el-col>
              <el-col :span="2" class="bg-yell">保险险种：</el-col>
              <el-col :span="4">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.insurance
                }}</div>
              </el-col>
              <el-col :span="2" class="bg-yell">保险期至：</el-col>
              <el-col :span="10">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo
                    .insuranceEndData
                }}</div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="2" class="bg-yell">特殊约定：</el-col>
              <el-col :span="22">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.remarks
                }}</div>
              </el-col>
            </el-row>
          </div>
          <h3 style="font-weight: bold; color: #333333">交易信息</h3>
          <div>
            <el-row>
              <el-col :span="2" class="bg-yell">收车金额：</el-col>
              <el-col :span="4">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo
                    .vehicleReceiptAmount
                }}</div>
              </el-col>
              <el-col :span="2" class="bg-yell">卖车金额：</el-col>
              <el-col :span="4">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.sellAmount
                }}</div>
              </el-col>
              <el-col :span="2" class="bg-yell">公允值范围：</el-col>
              <el-col :span="4">
                <div
                  >{{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.fairValue
                      ?.value1
                  }}元—{{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.fairValue
                      ?.value2
                  }}元</div
                >
              </el-col>
              <el-col :span="2" class="bg-yell">付款方式：</el-col>
              <el-col :span="4">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .payType == '0'
                    ? '全款'
                    : '定金+尾款'
                }}</div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="2" class="bg-yell">身份证</el-col>
              <el-col :span="4">
                <div style="display: flex; align-items: center">
                  <span class="identify" v-if="identifyShow">
                    {{
                      baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails.sellerIdCard.substr(
                        0,
                        1
                      )
                    }}*****************{{
                      baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails.sellerIdCard.substr(
                        baseInfoData.data.variables.formDataJson.formMain.formDataJson
                          .carInfoDetails.sellerIdCard.length - 1,
                        1
                      )
                    }}</span
                  >
                  <span class="identify" v-else>{{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                      .sellerIdCard
                  }}</span>
                  <Icon
                    style="margin-right: 3px"
                    icon="ep:view"
                    v-if="identifyShow"
                    @click="identifyShow = !identifyShow"
                  />
                  <Icon
                    style="margin-right: 3px"
                    icon="ep:hide"
                    v-else
                    @click="identifyShow = !identifyShow"
                  />
                </div>
                <el-image
                  v-for="item in baseInfoData.data.variables.formDataJson.formMain.formDataJson
                    .fileD"
                  :key="item.id"
                  style="width: 34px; height: 34px; margin-right: 5px"
                  :src="item.url"
                  fit="cover"
                  :zoom-rate="1.2"
                  :initial-index="0"
                  :preview-src-list="fileD"
                />
              </el-col>
              <el-col :span="2" class="bg-yell">姓名：</el-col>
              <el-col :span="4">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .sellerName
                }}</div>
              </el-col>
              <el-col :span="2" class="bg-yell">联系地址：</el-col>
              <el-col :span="4">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .sellerAdder
                }}</div>
              </el-col>
              <el-col :span="2" class="bg-yell">联系电话：</el-col>
              <el-col :span="4">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .sellerTel
                }}</div>
              </el-col>
            </el-row>

            <h3 style="font-weight: bold" class="mb20">合同信息</h3>
            <el-row>
              <el-form-item label="XXX收车委托合同">
                <button class="colr159" @click="viewContract">查看</button>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="XXX收车合同">
                <button type="button" class="colr159" @click="viewContract">查看</button>
              </el-form-item>
            </el-row>
          </div>
        </div>
        <!-- </el-card> -->
        <!-- 查看合同 -->
        <!-- <AgreementFrame :visible="contractVisible" @handle-cancel="handleCancel" /> -->
      </el-main>
    </el-container>
    <!-- </XModal> -->
  </ContentWrap>
</template>
<script lang="ts" setup name="MerchantApprovalPending">
import { allSchemas } from '../toDoList/toDoList.data'
import { defineProps } from 'vue'
import { propTypes } from '@/utils/propTypes'
// import { AgreementFrame } from './index'
import { baseInfoData } from '@/views/workbench/basInfoValue'

// import type { FormExpose } from '@/components/Form'
const { t } = useI18n() // 国际化
// const message = useMessage() // 消息弹窗
const [] = useXTable({
  allSchemas: allSchemas
})
//时间戳转日期
const formatDate = (time: string) => {
  let date = ''
  if (time) {
    const y = new Date(time).getFullYear()
    const m = new Date(time).getMonth()
    const d = new Date(time).getDate()
    date = y + '-' + m + '-' + d
    return date
  } else {
    return ''
  }
}
const actionType = ref('detail') // 操作按钮的类型
// const dialogVisible = ref(true) // 是否显示弹出层
const dialogTitle = ref('卖车价格超公允值待办') // 弹出层标题
// const formRef = ref<FormExpose>() // 表单 Ref

// 设置标题
const setDialogTile = (type: string) => {
  dialogTitle.value = t('action.' + type)
  actionType.value = type
  // dialogVisible.value = true
}
const fileB = computed(() => {
  return baseInfoData.data.variables.formDataJson.formMain.formDataJson.fileB.map(
    (item) => item.url
  )
})
const fileC = computed(() => {
  return baseInfoData.data.variables.formDataJson.formMain.formDataJson.fileC.map(
    (item) => item.url
  )
})
const fileA = computed(() => {
  return baseInfoData.data.variables.formDataJson.formMain.formDataJson.fileA.map(
    (item) => item.url
  )
})
const fileD = computed(() => {
  return baseInfoData.data.variables.formDataJson.formMain.formDataJson.fileD.map(
    (item) => item.url
  )
})
const identifyShow = ref(true)
// const idCardShow = ref(false)

// 合同弹框
const contractVisible = ref(false)
// const emit = defineEmits(['cancleSellCar'])
const props = defineProps({
  // visible: propTypes.bool.def(false),
  type: propTypes.bool.def(undefined)
})

// const visible = computed(() => {
//   return props.visible
// })

console.log(setDialogTile, props)

// 查看身份证
// const viewIdCard = () => {
//   idCardShow.value = !idCardShow.value
// }

// 查看合同
const viewContract = () => {
  contractVisible.value = true
}

// 关闭合同弹框
// const handleCancel = () => {
//   contractVisible.value = false
// }

// 关闭弹框
// const closeDialog = () => {
//   emit('cancleSellCar')
// }

// 通过
// const passBtn = () => {
//   emit('cancleSellCar')
// }

// 退回
// const returnBtn = () => {
//   emit('cancleSellCar')
// }
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

.btn {
  text-align: right;
  margin-bottom: 20px;
}

.content-box {
  line-height: 36px;
  color: #606266;
  .bg-yell {
    background: #f5f5f5;
    display: flex;
    justify-content: flex-end;
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
.content-box {
  line-height: 36px;
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
.carInfo {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.identify {
  display: inline-block;
  width: 136px;
}
.mb20 {
  margin-bottom: 20px;
}

.colr159 {
  color: #1592c9;
}
</style>
