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
        <div class="btn">
          <!-- <el-button type="danger" @click="closeDialog">关闭</el-button> -->
          <!-- <el-button type="primary" v-if="type == 'need'" @click="passBtn">通过</el-button>
          <el-button v-if="type == 'need'" @click="returnBtn">退回</el-button> -->
        </div>
        <!-- <el-card class="content-box"> -->
        <div>单号：{{ baseInfoData.data.serialNo }}</div>
        <h3 style="font-weight: bold" class="mb20">车辆基础信息</h3>
        <el-form>
          <el-row>
            <el-col :span="6">
              <el-form-item label="行驶证：">
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
                    style="width: 40px; height: 40px; margin: 0 5px; vertical-align: top"
                    :src="url.url"
                    fit="fill"
                    :zoom-rate="1.2"
                  />
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="机动车登记证书：">
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
                    style="width: 40px; height: 40px; margin: 0 5px; vertical-align: top"
                    :src="url.url"
                    fit="fill"
                    :zoom-rate="1.2"
                  />
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="车辆图片：">
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
                    style="width: 40px; height: 40px; margin: 0 5px; vertical-align: top"
                    :src="url.url"
                    fit="fill"
                    :zoom-rate="1.2"
                  />
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="车架号：">
                <div>
                  {{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.vin
                  }}</div
                >
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="发动机编号：">
                <div>
                  {{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.engineNum
                  }}</div
                >
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="首次登记日期：">
                <div>
                  {{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                      .firstRegistDate
                  }}</div
                >
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="车牌号：">
                <div>
                  {{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.plateNum
                  }}</div
                >
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="使用性质：">
                <div>
                  {{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                      .natureOfOperat
                  }}</div
                >
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="品牌/车型：">
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
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="车辆类型：">
                <div>
                  {{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.carType
                  }}</div
                >
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="品牌型号：">
                <div>
                  {{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.brandType
                  }}</div
                >
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="登记证号：">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .certificateNo
                }}</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="颜色：">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .colour
                }}</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="里程数：">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .mileage
                }}</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="使用年限至：">
                <div>{{
                  formatDate(
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.scrapDate
                  )
                }}</div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="年检签证有效期至：">
                <div>{{
                  formatDate(
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo
                      .annualInspectionDate
                  )
                }}</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="保险险种：">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.insurance
                }}</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="保险期至：">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo
                    .insuranceEndData
                }}</div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="特殊约定：">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.remarks
                }}</div>
              </el-form-item>
            </el-col>
          </el-row>
          <h3 style="font-weight: bold" class="mb20">交易信息</h3>
          <el-row>
            <el-col :span="6">
              <el-form-item label="收车金额：">
                <div style="display: flex; flex-direction: column">
                  <div>{{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo
                      .vehicleReceiptAmount
                  }}</div>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="卖车金额：">
                <div style="display: flex; flex-direction: column">
                  <div>{{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo
                      .sellAmount
                  }}</div>
                  <el-tag class="ml-2" type="danger"
                    >公允值范围：{{
                      baseInfoData.data.variables.formDataJson.formMain.formDataJson.fairValue
                        ?.value1
                    }}元—{{
                      baseInfoData.data.variables.formDataJson.formMain.formDataJson.fairValue
                        ?.value2
                    }}元</el-tag
                  >
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="付款方式 ：">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .payType == '0'
                    ? '全款'
                    : '定金+尾款'
                }}</div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="身份证">
                <div style="display: flex; flex-direction: column">
                  <div>
                    <span class="identify" v-if="identifyShow"> 5*****************1</span>
                    <span class="identify" v-else>{{
                      baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                        .sellerIdCard
                    }}</span>
                    <Icon
                      style="margin: 0 6px"
                      icon="ep:view"
                      v-if="identifyShow"
                      @click="identifyShow = !identifyShow"
                    />
                    <Icon
                      style="margin: 0 6px"
                      icon="ep:hide"
                      v-else
                      @click="identifyShow = !identifyShow"
                    />
                  </div>
                  <div>
                    <el-image
                      v-for="item in baseInfoData.data.variables.formDataJson.formMain.formDataJson
                        .fileD"
                      :key="item.id"
                      style="width: 70px; height: 70px; margin-right: 5px"
                      :src="item.src"
                      fit="cover"
                      :zoom-rate="1.2"
                      :initial-index="0"
                    />
                  </div>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="姓名：">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .sellerName
                }}</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="联系地址：">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .sellerAdder
                }}</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="电话：">
                <div>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .sellerTel
                }}</div>
              </el-form-item>
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
        </el-form>
        <!-- </el-card> -->

        <!-- 查看合同 -->
        <AgreementFrame :visible="contractVisible" @handle-cancel="handleCancel" />
      </el-main>
    </el-container>
    <!-- </XModal> -->
  </ContentWrap>
</template>
<script lang="ts" setup name="MerchantApprovalPending">
import { allSchemas } from '../toDoList/toDoList.data'
import { defineProps } from 'vue'
import { propTypes } from '@/utils/propTypes'
import { AgreementFrame } from './index'
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
const handleCancel = () => {
  contractVisible.value = false
}

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
.header {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
}

.btn {
  text-align: right;
  margin-bottom: 20px;
}

.content-box {
  line-height: 36px;
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
