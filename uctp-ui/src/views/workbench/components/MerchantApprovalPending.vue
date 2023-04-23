<template>
  <ContentWrap>
    <!-- <XModal
      v-model="dialogVisible"
      :title="dialogTitle"
      :fullscreen="true"
      :showFooter="false"
      @close="closeDialog"
    > -->
    <el-container>
      <!-- <el-header class="header">山西万国市场商户张三账号申请待办</el-header> -->
      <!-- <el-header class="header">{{
        (baseInfoData.data.variables.marketName || '') +
        (baseInfoData.data.variables.merchantName || '') +
        '账号申请流程'
      }}</el-header> -->
      <el-main>
        <div>
          <div style="font-size: 16px" class="title">
            <span>单号：{{ baseInfoData.data.serialNo }}</span>
            <span>商户经办人：{{ mainValue.formDataJson.name }}</span>
            <span>商户电话：{{ mainValue.formDataJson.phone }}</span>
          </div>
          <!-- <div class="btn"> -->
          <!-- <el-button type="danger" @click="closeDialog">关闭</el-button> -->
          <!-- <el-button type="primary" v-if="type == 'need'" @submit="submitBtn">提交</el-button>
            <el-button v-if="type == 'need'" @click="terminationBtn">终止</el-button> -->
          <!-- </div> -->
        </div>
        <!-- <el-card class="content-box"> -->
        <div class="content-box">
          <el-row>
            <el-col :span="2" class="bg-yell">姓名：</el-col>
            <el-col :span="4"> {{ mainValue.formDataJson.name || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">手机号：</el-col>
            <el-col :span="4">{{ mainValue.formDataJson.phone || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">身份证号： </el-col>
            <el-col :span="10">{{ mainValue.formDataJson.idCard || '暂无数据' }}</el-col>
          </el-row>
          <el-row>
            <el-col :span="2" class="bg-yell">营业执照号： </el-col>
            <el-col :span="4">{{ mainValue.formDataJson.taxNum || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">公司名称： </el-col>
            <el-col :span="4">{{ mainValue.formDataJson.businessName || '暂无数据' }}</el-col>
            <el-col :span="2" class="bg-yell">法定代表人： </el-col>
            <el-col :span="4">{{
              mainValue.formDataJson.legal_representative || '暂无数据'
            }}</el-col>
            <el-col :span="2" class="bg-yell">市场所在地： </el-col>
            <el-col :span="4">{{
              mainValue.formDataJson.marketLocationValue || '暂无数据'
            }}</el-col>
          </el-row>
          <el-row>
            <el-col :span="2" class="bg-yell">开户行：</el-col>
            <el-col :span="4">
              <div>{{ mainValue.formDataJson.bankName || '暂无数据' }}</div>
            </el-col>
            <el-col :span="2" class="bg-yell">对公银行账号：</el-col>
            <el-col :span="4">
              <div>{{ mainValue.formDataJson.bankNumber || '暂无数据' }}</div>
            </el-col>
            <el-col :span="2" class="bg-yell">保证金充值卡号：</el-col>
            <el-col :span="10">
              <div>{{ mainValue.formDataJson.bondBankAcconut || '暂无数据' }}</div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="2" class="bg-yell">联系地址： </el-col>
            <el-col :span="22">{{ mainValue.formDataJson.address || '暂无数据' }}</el-col>
          </el-row>
          <div class="image">
            <el-image
              v-for="(img, index) in mainValue.formDataJson.idCardUrl"
              :key="img.id"
              style="width: 200px; height: 100px; margin-right: 10px"
              :src="img.url"
              :zoom-rate="1.2"
              :preview-src-list="idcardUrlArr"
              fit="cover"
              :initial-index="index"
            />
            <el-image
              v-for="item in mainValue.formDataJson.businessLicense"
              :key="item.id"
              style="width: 200px; height: 100px"
              :src="item.url"
              :zoom-rate="1.2"
              :preview-src-list="busUrlArr"
              fit="cover"
              :initial-index="0"
            />
          </div>
        </div>
        <!-- </el-card> -->
      </el-main>
    </el-container>
    <!-- </XModal> -->
  </ContentWrap>
</template>
<script lang="ts" setup name="MerchantApprovalPending">
import { allSchemas } from '../toDoList/toDoList.data'
import { defineProps } from 'vue'
import { propTypes } from '@/utils/propTypes'
// import type { FormExpose } from '@/components/Form'
// 详情
import { baseInfoData } from '@/views/workbench/basInfoValue'
console.log(baseInfoData.data)
const { t } = useI18n() // 国际化
// const message = useMessage() // 消息弹窗

const [] = useXTable({
  allSchemas: allSchemas
})

const actionType = ref('detail') // 操作按钮的类型
// const dialogVisible = ref(true) // 是否显示弹出层
const dialogTitle = ref('商户主账号审批待办') // 弹出层标题
// const formRef = ref<FormExpose>() // 表单 Ref

// 设置标题
const setDialogTile = (type: string) => {
  dialogTitle.value = t('action.' + type)
  actionType.value = type
  // dialogVisible.value = true
}

// 详情
let mainValue = reactive({
  formDataJson: { idCardUrl: [{ url: '' }], businessLicense: [{ url: '' }] }
})

nextTick(() => {
  mainValue.formDataJson = { ...baseInfoData.data.variables.formDataJson.formMain.formDataJson }
})

const idcardUrlArr = computed(() => {
  return mainValue.formDataJson.idCardUrl.map((item) => item.url)
})

const busUrlArr = computed(() => {
  return mainValue.formDataJson.businessLicense.map((item) => item.url)
})

// const emit = defineEmits(['closeDialog'])

const props = defineProps({
  // dialogVisible: propTypes.bool.def(false),
  type: propTypes.bool.def(undefined)
})

// const dialogVisible = computed(() => {
//   return props.dialogVisible
// })
console.log(props)
console.log(setDialogTile)

// 关闭弹框
// const closeDialog = () => {
//   emit('closeDialog')
// }
// 提交
// const submitBtn = () => {
//   emit('closeDialog')
// }
// 终止
// const terminationBtn = () => {
//   emit('closeDialog')
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
