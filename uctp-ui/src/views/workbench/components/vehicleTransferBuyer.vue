<template>
  <div>
    <el-card>
      <div style="font-size: 16px" class="title">
        <span>单号：{{ baseInfoData.data.serialNo }}</span>
        <span>商户经办人：{{ baseInfoData.data.variables.startUserName }}</span>
        <span>商户电话：{{ baseInfoData.data.variables.startUserMobile }}</span>
      </div>
      <div class="content-box">
        <h3 style="font-weight: bold; color: #333333; line-height: 36px">车辆基础信息</h3>
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
                  v-for="(url, index) in baseInfoData.data.variables.formDataJson.formMain
                    .formDataJson.fileB"
                  :key="url.id"
                  style="width: 34px; height: 34px; margin: 0 5px; vertical-align: top"
                  :src="url.url"
                  fit="fill"
                  :zoom-rate="1.2"
                  :initial-index="index"
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
                  v-for="(url, index) in baseInfoData.data.variables.formDataJson.formMain
                    .formDataJson.fileC"
                  :key="url.id"
                  style="width: 34px; height: 34px; margin: 0 5px; vertical-align: top"
                  :src="url.url"
                  fit="fill"
                  :zoom-rate="1.2"
                  :initial-index="index"
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
                  v-for="(url, index) in baseInfoData.data.variables.formDataJson.formMain
                    .formDataJson.fileA"
                  :key="url.id"
                  style="width: 34px; height: 34px; margin: 0 5px; vertical-align: top"
                  :src="url.url"
                  fit="fill"
                  :zoom-rate="1.2"
                  :initial-index="index"
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
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails.colour
              }}</div>
            </el-col>
            <el-col :span="2" class="bg-yell">里程数：</el-col>
            <el-col :span="4">
              <div
                >{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .mileage
                }}万公里</div
              >
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
                  ?.insuranceEndData
                  ? baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo?.insuranceEndData.split(
                      ' '
                    )[0]
                  : ''
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
        <h3 style="font-weight: bold; color: #333333; line-height: 36px">交易信息</h3>
        <div>
          <el-row>
            <el-col :span="2" class="bg-yell">收车金额：</el-col>
            <el-col :span="4">
              <div
                >{{
                  moneyFormat(
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo
                      .vehicleReceiptAmount
                  )
                }}元</div
              >
            </el-col>
            <el-col :span="2" class="bg-yell">卖车金额：</el-col>
            <el-col :span="4">
              <div>
                {{
                  moneyFormat(
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo
                      .sellAmount
                  )
                }}元
              </div>
            </el-col>
            <el-col :span="2" class="bg-yell">公允值范围：</el-col>
            <el-col :span="4">
              <div>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.sellCarFair
              }}</div>
            </el-col>
            <el-col :span="2" class="bg-yell">定金：</el-col>
            <el-col :span="4">
              <div
                >{{
                  moneyFormat(
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfo.deposit
                  )
                }}元</div
              >
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="2" class="bg-yell">付款方式：</el-col>
            <el-col :span="4">
              <div>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                  .payType == '0'
                  ? '全款'
                  : '定金+尾款'
              }}</div>
            </el-col>
            <el-col :span="2" class="bg-yell">姓名：</el-col>
            <el-col :span="4">
              <div>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                  .buyerName
              }}</div>
            </el-col>
            <el-col :span="2" class="bg-yell">身份证：</el-col>
            <el-col :span="10">
              <div style="display: flex; align-items: center">
                <span class="identify identify-eye" v-if="identifyShow">
                  {{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails.buyerIdCard.substr(
                      0,
                      1
                    )
                  }}*****************{{
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails.buyerIdCard.substr(
                      baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                        .buyerIdCard.length - 1,
                      1
                    )
                  }}</span
                >
                <span class="identify" v-else>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .buyerIdCard
                }}</span>
                <Icon
                  style="margin-right: 5px"
                  icon="ep:view"
                  v-if="identifyShow"
                  @click="identifyShow = !identifyShow"
                />
                <Icon
                  style="margin-right: 5px"
                  icon="ep:hide"
                  v-else
                  @click="identifyShow = !identifyShow"
                />
              </div>
              <el-image
                v-for="(item, index) in baseInfoData.data.variables.formDataJson.formMain
                  .formDataJson.fileD"
                :key="item.id"
                style="width: 34px; height: 34px; margin-right: 5px"
                :src="item.url"
                fit="cover"
                :zoom-rate="1.2"
                :initial-index="index"
                :preview-src-list="fileD"
              />
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="2" class="bg-yell">联系电话：</el-col>
            <el-col :span="4">
              <div>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                  .buyerTel
              }}</div>
            </el-col>
            <el-col :span="2" class="bg-yell">联系地址：</el-col>
            <el-col :span="16">
              <div>
                {{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInfoDetails
                    .buyerAdder
                }}</div
              >
            </el-col>
          </el-row>
        </div>
        <p style="font-weight: bold; margin: 10px 0">发票信息</p>
        <div>
          <el-row>
            <el-col :span="4" class="bg-yell">
              <span>买车单位/个人</span>
            </el-col>
            <el-col :span="8">
              <span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.buyerName
              }}</span>
            </el-col>
            <el-col :span="4" class="bg-yell"><span>单位代码/身份证号码</span></el-col>
            <el-col :span="8"
              ><span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.buyerIdCard
              }}</span></el-col
            >
          </el-row>
          <el-row>
            <el-col :span="4" class="bg-yell">
              <span>买车单位/个人住址</span>
            </el-col>
            <el-col :span="8">
              <span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.buyerAddress
              }}</span>
            </el-col>
            <el-col :span="4" class="bg-yell"><span>电话</span></el-col>
            <el-col :span="8"
              ><span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.buyerTel
              }}</span></el-col
            >
          </el-row>
          <el-row>
            <el-col :span="4" class="bg-yell">
              <span>卖方单位/个人</span>
            </el-col>
            <el-col :span="8">
              <span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.sellerName
              }}</span>
            </el-col>
            <el-col :span="4" class="bg-yell"><span>单位代码/身份证号码</span></el-col>
            <el-col :span="8"
              ><span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.sellerIdCard
              }}</span></el-col
            >
          </el-row>
          <el-row>
            <el-col :span="4" class="bg-yell">
              <span>卖车单位/个人住址</span>
            </el-col>
            <el-col :span="8">
              <span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.sellerAddress
              }}</span>
            </el-col>
            <el-col :span="4" class="bg-yell"><span>电话</span></el-col>
            <el-col :span="8"
              ><span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.sellerTel
              }}</span></el-col
            >
          </el-row>
          <el-row>
            <el-col :span="4" class="bg-yell">
              <span>车牌号</span>
            </el-col>
            <el-col :span="4"
              ><span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.plateNum
              }}</span>
            </el-col>
            <el-col :span="4" class="bg-yell"><span>登记证书</span></el-col>
            <el-col :span="4"
              ><span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.certificateNo
              }}</span></el-col
            >
            <el-col :span="4" class="bg-yell"><span>车辆类型</span></el-col>
            <el-col :span="4"
              ><span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.carType
              }}</span></el-col
            >
          </el-row>
          <el-row>
            <el-col :span="4" class="bg-yell">
              <span>车架号/车辆识别代码</span>
            </el-col>
            <el-col :span="4">
              <span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.vin
              }}</span>
            </el-col>
            <el-col :span="4" class="bg-yell"><span>厂牌型号</span></el-col>
            <el-col :span="4"
              ><span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.model
              }}</span></el-col
            >
            <el-col :span="4" class="bg-yell"><span>转入地车辆管理所名称</span></el-col>
            <el-col :span="4">
              <span>
                {{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                    .carInvoiceDetailVO.transManageName
                }}
              </span>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4" class="bg-yell">
              <span>车辆价款</span>
            </el-col>
            <el-col :span="20">
              <span>
                {{
                  moneyFormat(
                    baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                      .carInvoiceDetailVO.sellAmount
                  )
                }}元
              </span>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4" class="bg-yell">
              <span>二手车市场</span>
            </el-col>
            <el-col :span="8">
              <span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.marketName
              }}</span>
            </el-col>
            <el-col :span="4" class="bg-yell padding-0"
              ><div class="flex-column">
                <span>纳税人识别号</span>
                <span>地址</span>
              </div></el-col
            >
            <el-col :span="8" class="padding-0"
              ><div class="flex-column flex-content">
                <span>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                    .carInvoiceDetailVO.taxNum
                }}</span>
                <span>{{
                  baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                    .carInvoiceDetailVO.marketAddress
                }}</span>
              </div></el-col
            >
          </el-row>
          <el-row>
            <el-col :span="4" class="bg-yell">
              <span>开户行账号</span>
            </el-col>
            <el-col :span="8">
              <span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.marketBankNum
              }}</span>
            </el-col>
            <el-col :span="4" class="bg-yell"><span>电话</span></el-col>
            <el-col :span="8"
              ><span>{{
                baseInfoData.data.variables.formDataJson.formMain.formDataJson.carInvoiceInfoVO
                  .carInvoiceDetailVO.marketTel
              }}</span></el-col
            >
          </el-row>
        </div>
        <p style="font-weight: bold; margin: 10px 0">合同信息</p>
        <div class="content">
          <el-row>
            <template
              v-for="item in baseInfoData.data.variables.formDataJson.formMain.formDataJson
                .carInvoiceInfoVO.contractList"
              :key="item.contractFileId"
            >
              <el-col :span="4" class="bg-yell">
                <span>{{ item.contractName ? item.contractName + ':' : '' }}</span>
              </el-col>
              <el-col :span="4">
                <span><button class="colr159" @click="viewContract(item)">查看</button></span>
              </el-col>
            </template>
          </el-row>
        </div>
      </div>
    </el-card>
    <AgreementFrame
      :visible="contractVisible"
      :src="contractFileUrl"
      @handle-cancel="handleCancel"
    />
  </div>
</template>
<script lang="ts" setup name="MerchantApprovalPending">
import { AgreementFrame } from './index'
import { baseInfoData } from '@/views/workbench/basInfoValue'

//时间戳转日期
const formatDate = (time: any) => {
  let date = ''
  if (time) {
    if (time.includes('T')) {
      return time.split('T')[0]
    }
    const y = new Date(time * 1).getFullYear()
    const m = (new Date(time * 1).getMonth() + 1 + '').padStart(2, '0')
    const d = (new Date(time * 1).getDate() + '').padStart(2, '0')
    date = y + '-' + m + '-' + d
    return date
  } else {
    return ''
  }
}
//千分位校验
const moneyFormat = (num) => {
  if (num || num == '0') {
    return Number(num)
      .toFixed(2)
      .replace(/(\d)(?=(\d{3})+\.)/g, '$1,')
  } else {
    return ''
  }
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

.btn {
  text-align: right;
  margin-bottom: 20px;
}

.content-box {
  color: #606266;
  .bg-yell {
    background: #f5f5f5;
    display: flex;
    text-align: right;
    padding-right: 5px;
    justify-content: flex-end;
  }
  .padding-0 {
    padding: 0;
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
  .flex-column {
    width: 100%;
    display: flex;
    flex-direction: column;
    padding: 0;
    span {
      padding-right: 5px;
      width: 100%;
      text-align: right;
    }
    > span:nth-child(1) {
      border-bottom: 1px solid #eaeaea;
    }
  }
  .flex-content {
    span {
      padding: 0;
      padding-left: 15px;
      height: 20px;
      text-align: left;
    }
  }
}
.content-box .el-row:first-child {
  border-top: 1px solid #eaeaea;
}
.content-box .el-row .el-col:nth-child(1) {
  border-left: 1px solid #eaeaea;
}
.content-box .el-row .el-col:nth-child(even) {
  padding-left: 15px;
  &.padding-0 {
    padding: 0;
  }
}
.carInfo {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.identify {
  display: inline-block;
  width: 140px;
}
.content {
  .el-row {
    border-left: 1px solid #eaeaea;
  }
  .el-row:first-child {
    border-top: none;
    .el-col {
      border-top: 1px solid #eaeaea;
    }
  }
}
.colr159 {
  color: #1592c9;
}
</style>
